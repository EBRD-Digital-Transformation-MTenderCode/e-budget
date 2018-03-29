package com.procurement.budget.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.budget.config.properties.OCDSProperties;
import com.procurement.budget.dao.EiDao;
import com.procurement.budget.exception.ErrorException;
import com.procurement.budget.exception.ErrorType;
import com.procurement.budget.model.dto.bpe.ResponseDto;
import com.procurement.budget.model.dto.ei.EiDto;
import com.procurement.budget.model.dto.ei.EiOrganizationReferenceDto;
import com.procurement.budget.model.dto.ocds.TenderStatus;
import com.procurement.budget.model.dto.ocds.TenderStatusDetails;
import com.procurement.budget.model.entity.EiEntity;
import com.procurement.budget.utils.DateUtil;
import com.procurement.budget.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class EiServiceImpl implements EiService {

    private static final String SEPARATOR = "-";
    private final OCDSProperties ocdsProperties;
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;
    private final EiDao eiDao;

    public EiServiceImpl(final OCDSProperties ocdsProperties,
                         final JsonUtil jsonUtil,
                         final DateUtil dateUtil,
                         final EiDao eiDao) {
        this.ocdsProperties = ocdsProperties;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
        this.eiDao = eiDao;
    }

    @Override
    public ResponseDto createEi(final String owner,
                                final String country,
                                final LocalDateTime dateTime,
                                final EiDto ei) {
        final String cpId = getCpId(country);
        ei.setOcId(cpId);
        setTenderId(ei, cpId);
        setTenderStatus(ei);
        setBudgetId(ei);
        setIdOfOrganizationReference(ei.getBuyer());
        validatePeriod(ei);
        final EiEntity entity = getEntity(ei, owner, dateTime);
        eiDao.save(entity);
        ei.setToken(entity.getToken().toString());
        return new ResponseDto<>(true, null, ei);
    }

    @Override
    public ResponseDto updateEi(final String owner,
                                final String cpId,
                                final String token,
                                final EiDto eiDto) {

        final EiEntity entity = Optional.ofNullable(eiDao.getByCpIdAndToken(cpId, UUID.fromString(token)))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        if (!entity.getOwner().equals(owner)) throw new ErrorException(ErrorType.INVALID_OWNER);
        final EiDto ei = jsonUtil.toObject(EiDto.class, entity.getJsonData());
        ei.setPlanning(eiDto.getPlanning());
        ei.setTender(eiDto.getTender());
        entity.setJsonData(jsonUtil.toJson(ei));
        eiDao.save(entity);
        return new ResponseDto<>(true, null, ei);
    }

    @Override
    public EiDto getEi(final String cpId) {
        final EiEntity entity = Optional.ofNullable(eiDao.getByCpId(cpId))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        return jsonUtil.toObject(EiDto.class, entity.getJsonData());
    }

    private String getCpId(final String country) {
        return ocdsProperties.getPrefix() + SEPARATOR + country + SEPARATOR +
                dateUtil.milliNowUTC();
    }

    private void setIdOfOrganizationReference(final EiOrganizationReferenceDto or) {
        or.setId(or.getIdentifier().getScheme() + SEPARATOR + or.getIdentifier().getId());
    }

    private void setTenderId(final EiDto ei, final String cpId) {
        ei.getTender().setId(cpId);
    }

    private void setTenderStatus(final EiDto ei) {
        ei.getTender().setStatus(TenderStatus.PLANNING);
        ei.getTender().setStatusDetails(TenderStatusDetails.EMPTY);
    }

    private void setBudgetId(final EiDto ei) {
        ei.getPlanning().getBudget().setId(ei.getTender().getClassification().getId());
    }

    private void validatePeriod(final EiDto ei) {
        if (!ei.getPlanning().getBudget().getPeriod().getStartDate()
                .isBefore(ei.getPlanning().getBudget().getPeriod().getEndDate()))
            throw new ErrorException(ErrorType.INVALID_PERIOD);
    }

    private EiEntity getEntity(final EiDto ei, final String owner, final LocalDateTime dateTime) {
        final EiEntity eiEntity = new EiEntity();
        eiEntity.setCpId(ei.getOcId());
        eiEntity.setToken(UUIDs.random());
        eiEntity.setOwner(owner);
        eiEntity.setCreatedDate(dateUtil.localToDate(dateTime));
        eiEntity.setJsonData(jsonUtil.toJson(ei));
        return eiEntity;
    }

}
