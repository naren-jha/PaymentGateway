package com.phonepe.paymentgateway.bank.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RouterStrategyFactory {

    @Autowired
    private Map<RouterStrategyType, RouterStrategy> bankSelectionStrategies;

    public RouterStrategy getBankSelectionStrategy(RouterStrategyType type) {
        return bankSelectionStrategies.get(type);
    }
}
