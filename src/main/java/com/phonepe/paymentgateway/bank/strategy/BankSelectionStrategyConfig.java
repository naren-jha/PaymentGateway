package com.phonepe.paymentgateway.bank.strategy;

import com.phonepe.paymentgateway.bank.BankService;
import com.phonepe.paymentgateway.bank.HDFCBankService;
import com.phonepe.paymentgateway.bank.ICICIBankService;
import com.phonepe.paymentgateway.bank.strategy.BankSelectionStrategy;
import com.phonepe.paymentgateway.bank.strategy.BankSelectionStrategyType;
import com.phonepe.paymentgateway.mode.Mode;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class BankSelectionStrategyConfig {

    private final List<BankSelectionStrategy> bankSelectionStrategies;

    @Bean
    public Map<BankSelectionStrategyType, BankSelectionStrategy> bankSelectionStrategyMap() {
        Map<BankSelectionStrategyType, BankSelectionStrategy> bankSelectionStrategyMap = new EnumMap<>(BankSelectionStrategyType.class);
        bankSelectionStrategies.forEach(bankSelectionStrategy -> bankSelectionStrategyMap.put(bankSelectionStrategy.strategyType(), bankSelectionStrategy));
        return bankSelectionStrategyMap;
    }
}