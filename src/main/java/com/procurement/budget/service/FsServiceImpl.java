package com.procurement.budget.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.budget.dao.FsDao;
import com.procurement.budget.exception.ErrorException;
import com.procurement.budget.model.dto.bpe.ResponseDto;
import com.procurement.budget.model.dto.check.CheckBudgetBreakdownDto;
import com.procurement.budget.model.dto.check.CheckRequestDto;
import com.procurement.budget.model.dto.check.CheckResponseDto;
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
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class FsServiceImpl implements FsService {

    private static final String SEPARATOR = "-";
    private static final String FS_SEPARATOR = "-fs-";
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
        fs.setOcId(getOcId(cpId, date));
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
    public ResponseDto checkFs(CheckRequestDto dto) {
        final String cpId = getCpIdFromOcId(dto.getBudgetBreakdown().get(0).getId());
        final List<FsEntity> entities = fsDao.getAllByCpId(cpId);
        if (entities.isEmpty()) throw new ErrorException(DATA_NOT_FOUND_ERROR);
        Map<String, FsDto> fsMap = new HashMap<>();
        HashSet<FsOrganizationReferenceDto> funders = new HashSet<>();
        HashSet<FsOrganizationReferenceDto> payers = new HashSet<>();
        entities.stream()
                .map(e -> jsonUtil.toObject(FsDto.class, e.getJsonData()))
                .forEach(fsDto -> fsMap.put(fsDto.getOcId(), fsDto));
        dto.getBudgetBreakdown().forEach(bBr -> {
            final FsDto fs = fsMap.get(bBr.getId());
            if (Objects.isNull(fs)) throw new ErrorException(DATA_NOT_FOUND_ERROR);
            checkTenderPeriod(fs, dto);
            checkFsCurrency(fs, bBr);
            checkFsAmount(fs, bBr);
            checkFsStatus(fs);
            funders.add(fs.getFunder());
            payers.add(fs.getPayer());
        });
        final EiDto ei = eiService.getEi(cpId);
        CheckResponseDto responseDto = new CheckResponseDto(
                dto.getBudgetBreakdown(),
                funders,
                payers,
                ei.getBuyer()
        );
        return new ResponseDto<>(true, null, responseDto);
    }

    private void checkTenderPeriod(FsDto fs, CheckRequestDto dto) {
        final LocalDateTime tenderPeriodStartDate = dto.getTenderPeriod().getStartDate();
        final Period fsPeriod = fs.getPlanning().getBudget().getPeriod();
        boolean tenderPeriodValid = tenderPeriodStartDate.isAfter(fsPeriod.getStartDate()) &&
                tenderPeriodStartDate.isBefore(fsPeriod.getEndDate());
        if (!tenderPeriodValid) {
            throw new ErrorException("Tender period start date is not in financial source period.");
        }
    }

    private void checkFsCurrency(FsDto fs, CheckBudgetBreakdownDto breakdown) {
        final Currency fsCurrency = fs.getPlanning().getBudget().getAmount().getCurrency();
        final Currency brCurrency = breakdown.getAmount().getCurrency();
        if (!fsCurrency.equals(brCurrency)) {
            throw new ErrorException("Budget breakdown currency invalid.");
        }
    }

    private void checkFsAmount(FsDto fs, CheckBudgetBreakdownDto breakdown) {
        final Double fsAmount = fs.getPlanning().getBudget().getAmount().getAmount();
        final Double brAmount = breakdown.getAmount().getAmount();
        if (!fsAmount.equals(brAmount)) {
            throw new ErrorException("Budget breakdown amount invalid.");
        }
    }

    private void checkFsStatus(FsDto fs) {
        final TenderStatus fsStatus = fs.getTender().getStatus();
        final TenderStatusDetails fsStatusDetails = fs.getTender().getStatusDetails();
        if ((!fsStatus.equals(TenderStatus.ACTIVE) || !fsStatus.equals(TenderStatus.PLANNING))
                && !fsStatusDetails.equals(TenderStatusDetails.EMPTY)) {
            throw new ErrorException("Financial source status invalid.");
        }
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

    private String getOcId(final String cpId, final LocalDateTime date) {
        return cpId + FS_SEPARATOR + dateUtil.getMilliUTC(date);
    }

    private String getCpIdFromOcId(final String ocId) {
        int pos = ocId.indexOf(FS_SEPARATOR);
        return ocId.substring(0, pos);
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
