package com.procurement.budget.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.budget.dao.FsDao;
import com.procurement.budget.exception.ErrorException;
import com.procurement.budget.exception.ErrorType;
import com.procurement.budget.model.dto.bpe.ResponseDto;
import com.procurement.budget.model.dto.check.CheckBudgetBreakdownDto;
import com.procurement.budget.model.dto.check.CheckRequestDto;
import com.procurement.budget.model.dto.check.CheckResponseDto;
import com.procurement.budget.model.dto.check.CheckSourcePartyDto;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FsServiceImpl implements FsService {

    private static final String SEPARATOR = "-";
    private static final String FS_SEPARATOR = "-FS-";
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
                                final LocalDateTime dateTime,
                                final FsRequestDto fsDto) {
        final FsDto fs = new FsDto();
        fs.setOcId(getOcId(cpId));
        final EiDto ei = eiService.getEi(cpId);
        checkCurrency(ei, fsDto);
        checkPeriod(ei, fsDto);
        validatePeriod(fsDto);
        fs.setPlanning(fsDto.getPlanning());
        fs.setPayer(fsDto.getTender().getProcuringEntity());
        setIdOfOrganizationReference(fs.getPayer());
        final FsOrganizationReferenceDto fsBuyer = fsDto.getBuyer();
        TenderStatus tenderStatus = null;
        //buyer from EI
        if (Objects.isNull(fsBuyer)) {
            setFounderFromEi(fs, ei.getBuyer());
            setSourceEntity(fs.getPlanning().getBudget(), fs.getFunder());
            fs.getPlanning().getBudget().setVerified(false);
            tenderStatus = TenderStatus.PLANNING;
        }
        //from buyer fsDto
        if (Objects.nonNull(fsBuyer)) {
            setIdOfOrganizationReference(fsBuyer);
            fs.setFunder(fsBuyer);
            setSourceEntity(fs.getPlanning().getBudget(), fs.getFunder());
            fs.getPlanning().getBudget().setVerified(true);
            tenderStatus = TenderStatus.ACTIVE;
        }
        fs.setTender(new FsTenderDto(
                cpId,
                tenderStatus,
                TenderStatusDetails.EMPTY,
                null)
        );
        final FsEntity entity = getEntity(cpId, fs, owner, dateTime);
        fsDao.save(entity);
        fs.setToken(entity.getToken().toString());
        return new ResponseDto<>(true, null, fs);
    }

    @Override
    public ResponseDto updateFs(final String cpId,
                                final String token,
                                final String owner,
                                final FsDto fsDto) {
        final FsEntity entity = Optional.ofNullable(fsDao.getByCpIdAndToken(cpId, UUID.fromString(token)))
                .orElseThrow(() -> new ErrorException(ErrorType.FS_NOT_FOUND));
        if (!entity.getOwner().equals(owner)) throw new ErrorException(ErrorType.INVALID_OWNER);
        final FsDto fs = jsonUtil.toObject(FsDto.class, entity.getJsonData());
        fs.setTender(fsDto.getTender());
        fs.setPlanning(fsDto.getPlanning());
        entity.setJsonData(jsonUtil.toJson(fs));
        fsDao.save(entity);
        return new ResponseDto<>(true, null, fs);
    }

    @Override
    public ResponseDto checkFs(final CheckRequestDto dto) {
        final List<CheckBudgetBreakdownDto> budgetBreakdown = dto.getBudgetBreakdown();

        //check currency of Budget Breakdowns
        if (budgetBreakdown.stream().map(b -> b.getAmount().getCurrency()).collect(Collectors.toSet()).size() > 1) {
            throw new ErrorException(ErrorType.INVALID_CURRENCY);
        }
        final Set<String> cpIds = budgetBreakdown.stream()
                .map(b -> getCpIdFromOcId(b.getId()))
                .collect(Collectors.toSet());
        final HashSet<FsOrganizationReferenceDto> funders = new HashSet<>();
        final HashSet<FsOrganizationReferenceDto> payers = new HashSet<>();
        final HashSet<EiOrganizationReferenceDto> buyers = new HashSet<>();
        final List<FsEntity> entities = fsDao.getAllByCpIds(cpIds);
        if (entities.isEmpty()) throw new ErrorException(ErrorType.FS_NOT_FOUND);
        final Map<String, FsDto> fsMap = new HashMap<>();
        entities.stream()
                .map(e -> jsonUtil.toObject(FsDto.class, e.getJsonData()))
                .forEach(fsDto -> fsMap.put(fsDto.getOcId(), fsDto));
        for (final String cpId : cpIds) {
            final EiDto ei = eiService.getEi(cpId);
            checkCPV(ei, dto);
            buyers.add(ei.getBuyer());
            budgetBreakdown.forEach(br -> {
                final FsDto fs = fsMap.get(br.getId());
                if (Objects.isNull(fs)) throw new ErrorException(ErrorType.FS_NOT_FOUND);
                checkFsStatus(fs);
                checkTenderPeriod(fs, dto);
                checkFsAmount(fs, br);
                checkFsCurrency(fs, br);
                processBudgetBreakdown(br, fs);
                funders.add(fs.getFunder());
                payers.add(fs.getPayer());
            });
        }

        return new ResponseDto<>(
                true,
                null,
                new CheckResponseDto(cpIds, budgetBreakdown, funders, payers, buyers)
        );
    }

    private void checkCPV(final EiDto ei, final CheckRequestDto dto) {
        final String eiCPV = ei.getTender().getClassification().getId();
        final String dtoCPV = dto.getClassification().getId();
        if (!eiCPV.substring(0, 3).toUpperCase().equals(dtoCPV.substring(0, 3).toUpperCase()))
            throw new ErrorException(ErrorType.INVALID_CPV);
    }

    private void processBudgetBreakdown(final CheckBudgetBreakdownDto br, final FsDto fs) {
        final FsOrganizationReferenceDto fsSe = fs.getPlanning().getBudget().getSourceEntity();
        br.setSourceParty(new CheckSourcePartyDto(fsSe.getId(), fsSe.getName()));
        br.setPeriod(fs.getPlanning().getBudget().getPeriod());
    }

    private void validatePeriod(final FsRequestDto fs) {
        if (!fs.getPlanning().getBudget().getPeriod().getStartDate()
                .isBefore(fs.getPlanning().getBudget().getPeriod().getEndDate()))
            throw new ErrorException(ErrorType.INVALID_PERIOD);
    }

    private void checkTenderPeriod(final FsDto fs, final CheckRequestDto dto) {
        final LocalDate tenderStartDate = dto.getTenderPeriod().getStartDate().toLocalDate();
        final LocalDate fsEndDate = fs.getPlanning().getBudget().getPeriod().getEndDate().toLocalDate();
        if (!(tenderStartDate.isBefore(fsEndDate) || tenderStartDate.isEqual(fsEndDate))) {
            throw new ErrorException(ErrorType.INVALID_DATE);
        }
    }

    private void checkFsCurrency(final FsDto fs, final CheckBudgetBreakdownDto br) {
        final Currency fsCurrency = fs.getPlanning().getBudget().getAmount().getCurrency();
        final Currency brCurrency = br.getAmount().getCurrency();
        if (!fsCurrency.equals(brCurrency)) throw new ErrorException(ErrorType.INVALID_CURRENCY);
    }

    private void checkFsAmount(final FsDto fs, final CheckBudgetBreakdownDto br) {
        final Double fsAmount = fs.getPlanning().getBudget().getAmount().getAmount();
        final Double brAmount = br.getAmount().getAmount();
        if (brAmount > fsAmount) throw new ErrorException(ErrorType.INVALID_AMOUNT);
    }

    private void checkFsStatus(final FsDto fs) {
        final TenderStatus fsStatus = fs.getTender().getStatus();
        final TenderStatusDetails fsStatusDetails = fs.getTender().getStatusDetails();
        if (!((fsStatus.equals(TenderStatus.ACTIVE) ||
                fsStatus.equals(TenderStatus.PLANNING) ||
                fsStatus.equals(TenderStatus.PLANNED))
                && fsStatusDetails.equals(TenderStatusDetails.EMPTY)))
            throw new ErrorException(ErrorType.INVALID_STATUS);
    }

    private void checkCurrency(final EiDto ei, final FsRequestDto fs) {
        final Currency eiCurrency = ei.getPlanning().getBudget().getAmount().getCurrency();
        final Currency fsCurrency = fs.getPlanning().getBudget().getAmount().getCurrency();
        if (!eiCurrency.equals(fsCurrency)) throw new ErrorException(ErrorType.INVALID_CURRENCY);
    }

    private void checkPeriod(final EiDto ei, final FsRequestDto fs) {
        final Period eiPeriod = ei.getPlanning().getBudget().getPeriod();
        final Period fsPeriod = fs.getPlanning().getBudget().getPeriod();
        final boolean fsPeriodValid =
                (fsPeriod.getStartDate().isAfter(eiPeriod.getStartDate()) ||
                        fsPeriod.getStartDate().isEqual(eiPeriod.getStartDate()))
                        &&
                        (fsPeriod.getEndDate().isBefore(eiPeriod.getEndDate()) ||
                                fsPeriod.getEndDate().isEqual(eiPeriod.getEndDate()));
        if (!fsPeriodValid) throw new ErrorException(ErrorType.INVALID_PERIOD);
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
                        new HashSet(buyer.getAdditionalIdentifiers()),
                        buyer.getContactPoint());
        fs.setFunder(funder);
    }

    private void setIdOfOrganizationReference(final FsOrganizationReferenceDto or) {
        or.setId(or.getIdentifier().getScheme() + SEPARATOR + or.getIdentifier().getId());
    }

    private String getOcId(final String cpId) {
        return cpId + FS_SEPARATOR + dateUtil.milliNowUTC();
    }

    private String getCpIdFromOcId(final String ocId) {
        final int pos = ocId.indexOf(FS_SEPARATOR);
        return ocId.substring(0, pos);
    }

    private Double getAmount(final FsDto fs) {
        return fs.getPlanning().getBudget().getAmount().getAmount();
    }

    private FsEntity getEntity(final String cpId, final FsDto fs, final String owner, final LocalDateTime dateTime) {
        final FsEntity fsEntity = new FsEntity();
        fsEntity.setCpId(cpId);
        fsEntity.setOcId(fs.getOcId());
        fsEntity.setToken(UUIDs.random());
        fsEntity.setOwner(owner);
        fsEntity.setCreatedDate(dateUtil.localToDate(dateTime));
        fsEntity.setJsonData(jsonUtil.toJson(fs));
        fsEntity.setAmount(getAmount(fs));
        return fsEntity;
    }
}
