package com.njha.paymentgateway.bank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bank {
    private Long id;
    private String name;
    private BankType type;
}
