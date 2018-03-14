package com.procurement.budget.dao;

import com.procurement.budget.model.entity.EiEntity;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface EiDao {

    void save(EiEntity entity);

    EiEntity getByCpIdAndToken(String cpId, UUID token);

    EiEntity getByCpId(String cpId);
}

