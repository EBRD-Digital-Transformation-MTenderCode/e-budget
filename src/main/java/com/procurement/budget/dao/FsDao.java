package com.procurement.budget.dao;

import com.procurement.budget.model.entity.FsEntity;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface FsDao {

    void save(FsEntity entity);

    FsEntity getByCpIdAndOcIdAndToken(String cpId, String ocId, UUID token);
}

