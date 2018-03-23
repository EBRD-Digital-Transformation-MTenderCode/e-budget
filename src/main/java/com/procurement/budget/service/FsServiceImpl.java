package com.procurement.budget.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.budget.dao.FsDao;
import com.procurement.budget.exception.ErrorException;
import com.procurement.budget.model.dto.bpe.ResponseDto;
import com.procurement.budget.model.dto.check.CheckBudgetBreakdownDto;
import com.procurement.budget.model.dto.check.CheckRequestDto;
import com.procurement.budget.model.dto.check.CheckResponseDto;
import com.procurement.budget.model.dto.check.CheckSourcePartyDto;
import com.procurement.budget.model.dto.ei.EiDto;
import com.procurement.budget.model.dto.ei.EiOrganizationReferenceDto;
import com.procurement.budget.model.dto.fs.*;
import com.procurement.budget.model.dto.ocds.*;
import com.procurement.budget.model.dto.ocds.Currency;
import com.procurement.budget.model.entity.FsEntity;
import com.procurement.budget.utils.DateUtil;
import com.procurement.budget.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FsServiceImpl implements FsService {

    private static final String SEPARATOR = "-";
    private static final String FS_SEPARATOR = "-FS-";
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
                                final LocalDateTime dateTime,
                                final FsRequestDto fsDto) {
        final FsDto fs = new FsDto();
        fs.setOcId(getOcId(cpId));
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
        final FsEntity entity = getEntity(cpId, fs, owner, dateTime);
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
        fs.setTender(fsDto.getTender());
        fs.setPlanning(fsDto.getPlanning());
        entity.setJsonData(jsonUtil.toJson(fs));
        fsDao.save(entity);
        return new ResponseDto<>(true, null, fs);
    }

    @Override
    public ResponseDto checkFs(CheckRequestDto dto) {
        final List<CheckBudgetBreakdownDto> budgetBreakdown = dto.getBudgetBreakdown();

        Set<String> eiIds = budgetBreakdown.stream()
                .map(b -> getCpIdFromOcId(b.getId()))
                .collect(Collectors.toSet());
        HashSet<FsOrganizationReferenceDto> funders = new HashSet<>();
        HashSet<FsOrganizationReferenceDto> payers = new HashSet<>();
        HashSet<EiOrganizationReferenceDto> buyers = new HashSet<>();
        for (String cpId : eiIds) {
            final List<FsEntity> entities = fsDao.getAllByCpId(cpId);
            if (entities.isEmpty()) throw new ErrorException(DATA_NOT_FOUND_ERROR);
            final Map<String, FsDto> fsMap = new HashMap<>();
            entities.stream()
                    .map(e -> jsonUtil.toObject(FsDto.class, e.getJsonData()))
                    .forEach(fsDto -> fsMap.put(fsDto.getOcId(), fsDto));

            final EiDto ei = eiService.getEi(cpId);
            checkCPV(ei, dto);
            buyers.add(ei.getBuyer());
            budgetBreakdown.forEach(br -> {
                final FsDto fs = fsMap.get(br.getId());
                if (Objects.isNull(fs)) throw new ErrorException(DATA_NOT_FOUND_ERROR);
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
                new CheckResponseDto(eiIds, budgetBreakdown, funders, payers, buyers)
        );
    }

    private void checkCPV(EiDto ei, CheckRequestDto dto) {
        final String eiCPV = ei.getTender().getClassification().getId();
        final String dtoCPV = dto.getClassification().getId();
        if (!eiCPV.substring(0, 2).toUpperCase().equals(dtoCPV.substring(0, 2).toUpperCase())) {
            throw new ErrorException("CPV invalid.");
        }
    }

    private void processBudgetBreakdown(CheckBudgetBreakdownDto br, FsDto fs) {
        FsOrganizationReferenceDto fsSe = fs.getPlanning().getBudget().getSourceEntity();
        br.setSourceParty(new CheckSourcePartyDto(fsSe.getId(), fsSe.getName()));
        br.setPeriod(fs.getPlanning().getBudget().getPeriod());
    }

    private void checkTenderPeriod(FsDto fs, CheckRequestDto dto) {
        final LocalDateTime tenderPeriodStartDate = dto.getTenderPeriod().getStartDate();
        final Period fsPeriod = fs.getPlanning().getBudget().getPeriod();
        boolean tenderPeriodValid =
                (tenderPeriodStartDate.isAfter(fsPeriod.getStartDate()) ||
                        tenderPeriodStartDate.isEqual(fsPeriod.getStartDate()))
                        &&
                        (tenderPeriodStartDate.isBefore(fsPeriod.getEndDate()) ||
                                tenderPeriodStartDate.isEqual(fsPeriod.getEndDate()));

        if (!tenderPeriodValid) {
            throw new ErrorException("Tender period start date is not in financial source period.");
        }
    }

    private void checkFsCurrency(FsDto fs, CheckBudgetBreakdownDto br) {
        final Currency fsCurrency = fs.getPlanning().getBudget().getAmount().getCurrency();
        final Currency brCurrency = br.getAmount().getCurrency();
        if (!fsCurrency.equals(brCurrency)) {
            throw new ErrorException("Budget breakdown currency invalid.");
        }
    }

    private void checkFsAmount(FsDto fs, CheckBudgetBreakdownDto br) {
        final Double fsAmount = fs.getPlanning().getBudget().getAmount().getAmount();
        final Double brAmount = br.getAmount().getAmount();
        if (!(brAmount <= fsAmount)) {
            throw new ErrorException("Budget breakdown amount invalid.");
        }
    }

    private void checkFsStatus(FsDto fs) {
        final TenderStatus fsStatus = fs.getTender().getStatus();
        final TenderStatusDetails fsStatusDetails = fs.getTender().getStatusDetails();
        if (!((fsStatus.equals(TenderStatus.ACTIVE) ||
                fsStatus.equals(TenderStatus.PLANNING) ||
                fsStatus.equals(TenderStatus.PLANNED))
                && fsStatusDetails.equals(TenderStatusDetails.EMPTY))) {
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
        boolean fsPeriodValid =
                (fsPeriod.getStartDate().isAfter(eiPeriod.getStartDate()) ||
                        fsPeriod.getStartDate().isEqual(eiPeriod.getStartDate()))
                        &&
                        (fsPeriod.getEndDate().isBefore(eiPeriod.getEndDate()) ||
                                fsPeriod.getEndDate().isEqual(eiPeriod.getEndDate()));
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
        return cpId + FS_SEPARATOR + dateUtil.getMilliNowUTC();
    }

    private String getCpIdFromOcId(final String ocId) {
        int pos = ocId.indexOf(FS_SEPARATOR);
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
