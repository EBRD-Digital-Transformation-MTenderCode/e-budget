package com.procurement.budget.dao;

import com.procurement.budget.model.entity.FsEntity;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface FsDao {

    void save(FsEntity entity);

    FsEntity getByCpIdAndToken(String cpId, UUID token);

    List<FsEntity> getAllByCpId(String cpId);

    List<FsEntity> getAllByCpIds(Set<String> cpIds);
}

