package com.procurement.budget.model.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FsEntity {

    private String cpId;

    private String ocId;

    private UUID token;

    private String owner;

    private Double amount;

    private Double amountReserved;

    private String jsonData;
}
