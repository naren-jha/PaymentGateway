package com.phonepe.paymentgateway.bank.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BankSelectionStrategyFactory {

    @Autowired
    private Map<BankSelectionStrategyType, BankSelectionStrategy> bankSelectionStrategies;

    public BankSelectionStrategy getBankSelectionStrategy(BankSelectionStrategyType type) {
        return bankSelectionStrategies.get(type);
    }
}
