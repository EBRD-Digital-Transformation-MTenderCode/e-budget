package com.procurement.budget.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.budget.dao.FsDao;
import com.procurement.budget.exception.ErrorException;
import com.procurement.budget.model.dto.bpe.ResponseDto;
import com.procurement.budget.model.dto.check.CheckRequestDto;
import com.procurement.budget.model.dto.ei.EiDto;
import com.procurement.budget.model.dto.ei.EiOrganizationReferenceDto;
import com.procurement.budget.model.dto.fs.*;
import com.procurement.budget.model.dto.ocds.Currency;
import com.procurement.budget.model.dto.ocds.Period;
import com.procurement.budget.model.dto.ocds.TenderStatus;
import com.procurement.budget.model.dto.ocds.TenderStatusDetails;
import com.procurement.budget.model.entity.FsEntity;
import com.procurement.budget.utils.DateUtil;
import com.procurement.budget.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class FsServiceImpl implements FsService {

    private static final String SEPARATOR = "-";
    private static final String DATA_NOT_FOUND_ERROR = "FS not found.";
    private static final String INVALID_OWNER_ERROR = "FS invalid owner.";
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;
    private final FsDao fsDao;
    private final EiService eiService;

    public FsServiceImpl(final JsonUtil jsonUtil,
                         final DateUtil dateUtil,
                         final FsDao fsDao,
                         final EiService eiService) {
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
        this.fsDao = fsDao;
        this.eiService = eiService;
    }

    @Override
    public ResponseDto createFs(final String cpId,
                                final String owner,
                                final LocalDateTime date,
                                final FsRequestDto fsDto) {
        final FsDto fs = new FsDto();
        fs.setOcId(getOcId(cpId));
        fs.setDate(date);
        final EiDto ei = eiService.getEi(cpId);
        /*checkCurrency*/
        checkCurrency(ei, fsDto);
        /*checkPeriod*/
        checkPeriod(ei, fsDto);
        /*planning*/
        fs.setPlanning(fsDto.getPlanning());
        /*tender*/
        fs.setTender(new FsTenderDto(
                fs.getOcId(),
                TenderStatus.PLANNING,
                TenderStatusDetails.EMPTY,
                null)
        );
        /*payer*/
        fs.setPayer(fsDto.getTender().getProcuringEntity());
        setIdOfOrganizationReference(fs.getPayer());
        /*funder and source parties*/
        FsOrganizationReferenceDto fsBuyer = fsDto.getBuyer();
        /*from buyer*/
        if (Objects.nonNull(fsBuyer)) {
            setIdOfOrganizationReference(fsBuyer);
            fs.setFunder(fsBuyer);
            setSourceEntity(fs.getPlanning().getBudget(), fs.getFunder());
            fs.getPlanning().getBudget().setVerified(true);
        }
        /*from EI buyer*/
        if (Objects.isNull(fsBuyer)) {
            setFounderFromEi(fs, ei.getBuyer());
            setSourceEntity(fs.getPlanning().getBudget(), fs.getFunder());
            fs.getPlanning().getBudget().setVerified(false);
        }
        /*save*/
        final FsEntity entity = getEntity(cpId, owner, fs);
        fsDao.save(entity);
        fs.setToken(entity.getToken().toString());
        return new ResponseDto<>(true, null, fs);
    }

    @Override
    public ResponseDto updateFs(final String cpId,
                                final String ocId,
                                final String token,
                                final String owner,
                                final FsDto fsDto) {
        final FsEntity entity = Optional.ofNullable(fsDao.getByCpIdAndOcIdAndToken(cpId, ocId, UUID.fromString(token)))
                .orElseThrow(() -> new ErrorException(DATA_NOT_FOUND_ERROR));
        if (!entity.getOwner().equals(owner)) throw new ErrorException(INVALID_OWNER_ERROR);
        final FsDto fs = jsonUtil.toObject(FsDto.class, entity.getJsonData());
        fs.setDate(dateUtil.getNowUTC());
        fs.setTender(fsDto.getTender());
        fs.setPlanning(fsDto.getPlanning());
        entity.setJsonData(jsonUtil.toJson(fs));
        fsDao.save(entity);
        return new ResponseDto<>(true, null, fs);
    }

    @Override
    public ResponseDto checkFs(String cpId, String ocId, String token, String owner, CheckRequestDto dto) {
        return null;
    }

    private void checkCurrency(EiDto ei, FsRequestDto fs) {
        final Currency eiCurrency = ei.getPlanning().getBudget().getAmount().getCurrency();
        final Currency fsCurrency = fs.getPlanning().getBudget().getAmount().getCurrency();
        if (!eiCurrency.equals(fsCurrency)) {
            throw new ErrorException("Currency of financial source not valid.");
        }
    }

    private void checkPeriod(EiDto ei, FsRequestDto fs) {
        final Period eiPeriod = ei.getPlanning().getBudget().getPeriod();
        final Period fsPeriod = fs.getPlanning().getBudget().getPeriod();
        boolean fsPeriodValid = fsPeriod.getStartDate().isAfter(eiPeriod.getStartDate()) &&
                fsPeriod.getEndDate().isBefore(eiPeriod.getEndDate());
        if (!fsPeriodValid) {
            throw new ErrorException("Period of financial source not valid.");
        }
    }

    private void setSourceEntity(final FsBudgetDto budget, final FsOrganizationReferenceDto funder) {
        if (Objects.nonNull(budget)) {
            final FsOrganizationReferenceDto se =
                    new FsOrganizationReferenceDto(
                            funder.getId(),
                            funder.getName(),
                            null,
                            null,
                            null,
                            null);
            budget.setSourceEntity(se);
        }
    }

    private void setFounderFromEi(final FsDto fs, final EiOrganizationReferenceDto buyer) {
        final FsOrganizationReferenceDto funder =
                new FsOrganizationReferenceDto(
                        buyer.getId(),
                        buyer.getName(),
                        buyer.getIdentifier(),
                        buyer.getAddress(),
                        new LinkedHashSet(buyer.getAdditionalIdentifiers()),
                        buyer.getContactPoint());
        fs.setFunder(funder);
    }

    private void setIdOfOrganizationReference(final FsOrganizationReferenceDto or) {
        or.setId(or.getIdentifier().getScheme() + SEPARATOR + or.getIdentifier().getId());
    }


    private String getOcId(final String cpId) {
        return cpId + "-fs-" + dateUtil.getMilliNowUTC();
    }

    private Double getAmount(final FsDto fs) {
        return fs.getPlanning().getBudget().getAmount().getAmount();
    }

    private FsEntity getEntity(final String cpId, final String owner, final FsDto fs) {
        final FsEntity fsEntity = new FsEntity();
        fsEntity.setCpId(cpId);
        fsEntity.setOcId(fs.getOcId());
        fsEntity.setToken(UUIDs.random());
        fsEntity.setOwner(owner);
        fsEntity.setJsonData(jsonUtil.toJson(fs));
        fsEntity.setAmount(getAmount(fs));
        return fsEntity;
    }
}
