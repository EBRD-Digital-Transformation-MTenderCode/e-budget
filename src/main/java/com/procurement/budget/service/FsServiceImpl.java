package com.procurement.budget.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.budget.dao.FsDao;
import com.procurement.budget.exception.ErrorException;
import com.procurement.budget.model.dto.bpe.ResponseDto;
import com.procurement.budget.model.dto.fs.FsDto;
import com.procurement.budget.model.dto.fs.FsResponseDto;
import com.procurement.budget.model.dto.fs.FsTenderDto;
import com.procurement.budget.model.dto.ocds.TenderStatus;
import com.procurement.budget.model.entity.FsEntity;
import com.procurement.budget.utils.DateUtil;
import com.procurement.budget.utils.JsonUtil;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class FsServiceImpl implements FsService {

    private static final String DATA_NOT_FOUND_ERROR = "Data not found.";
    private static final String INVALID_OWNER_ERROR = "Invalid owner.";
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;
    private final FsDao fsDao;

    public FsServiceImpl(final JsonUtil jsonUtil,
                         final DateUtil dateUtil,
                         final FsDao fsDao) {
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
        this.fsDao = fsDao;
    }

    @Override
    public ResponseDto createFs(final String cpId,
                                final String owner,
                                final FsDto fs) {
        fs.setOcId(getOcId(cpId));
        fs.setDate(dateUtil.getNowUTC());
        setTender(fs, cpId);
        final FsEntity entity = getEntity(cpId, owner, fs);
        fsDao.save(getEntity(cpId, owner, fs));
        return getResponseDto(entity.getToken().toString(), fs);
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
        fs.setParties(fsDto.getParties());
        entity.setJsonData(jsonUtil.toJson(fs));
        fsDao.save(entity);
        return getResponseDto(entity.getToken().toString(), fs);
    }

    private void setTender(final FsDto fs, final String cpId) {
        fs.setTender(new FsTenderDto(cpId, TenderStatus.PLANNING));
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
        fsEntity.setToken(UUIDs.timeBased());
        fsEntity.setOwner(owner);
        fsEntity.setJsonData(jsonUtil.toJson(fs));
        fsEntity.setAmount(getAmount(fs));
        return fsEntity;
    }

    private ResponseDto getResponseDto(final String token, final FsDto fs) {
        final FsResponseDto responseDto = new FsResponseDto(
                token,
                fs.getOcId(),
                fs.getId(),
                fs.getDate(),
                fs.getTender(),
                fs.getPlanning(),
                fs.getParties()
        );
        return new ResponseDto<>(true, null, responseDto);
    }

//    private String getIdentifier(final FsDto fs) {
//        final RelatedProcess relatedProcess = fs.getRelatedProcesses()
//                .stream()
//                .filter(rp -> rp.getRelationship().contains(RelatedProcess.RelatedProcessType.PARENT))
//                .filter(rp -> !rp.getIdentifier().isEmpty())
//                .findFirst()
//                .orElse(null);
//        if (Objects.isNull(relatedProcess)) {
//            throw new ErrorException("ocid in related processes not found.");
//        } else {
//            return relatedProcess.getIdentifier();
//        }
//    }
}
