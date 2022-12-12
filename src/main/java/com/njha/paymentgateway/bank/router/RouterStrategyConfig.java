package com.njha.paymentgateway.bank.router;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class RouterStrategyConfig {

    private final List<RouterStrategy> bankSelectionStrategies;

    @Bean
    public Map<RouterStrategyType, RouterStrategy> bankSelectionStrategyMap() {
        Map<RouterStrategyType, RouterStrategy> bankSelectionStrategyMap = new EnumMap<>(RouterStrategyType.class);
        bankSelectionStrategies.forEach(bankSelectionStrategy -> bankSelectionStrategyMap.put(bankSelectionStrategy.strategyType(), bankSelectionStrategy));
        return bankSelectionStrategyMap;
    }
}