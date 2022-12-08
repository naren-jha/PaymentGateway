package com.phonepe.paymentgateway.bank.strategy;

import com.phonepe.paymentgateway.bank.BankType;
import com.phonepe.paymentgateway.bank.BankService;
import com.phonepe.paymentgateway.bank.HDFCBankService;
import com.phonepe.paymentgateway.bank.ICICIBankService;
import com.phonepe.paymentgateway.bank.SBIBankService;
import com.phonepe.paymentgateway.mode.Mode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class SelectorConfigs {
    @Bean
    public Map<Mode, BankType> modeToBankTypeMap() {
        Map<Mode, BankType> modeToBankTypeMap = new EnumMap<>(Mode.class);
        modeToBankTypeMap.put(Mode.NET_BANKING, BankType.ICICI); // All NET_BANKING transactions will go to ICICI Bank
        modeToBankTypeMap.put(Mode.CREDIT_CARD, BankType.HDFC); // All CREDIT_CARD transactions will go to ICICI Bank
        return modeToBankTypeMap;
    }

    @Bean
    public Map<BankType, Integer> bankToPercentageMap() {
        Map<BankType, Integer> bankToPercentageMap = new EnumMap<>(BankType.class);
        bankToPercentageMap.put(BankType.ICICI, 40); // 40% of the total traffic will go to ICICI Bank
        bankToPercentageMap.put(BankType.HDFC, 30); // 30% of the total traffic will go to HDFC Bank
        bankToPercentageMap.put(BankType.SBI, 30); // 30% of the total traffic will go to SBI Bank
        return bankToPercentageMap;
    }

    @Bean
    public Map<BankType, BankService> bankTypeToBankServiceMap() {
        Map<BankType, BankService> modeToBankServiceMap = new EnumMap<>(BankType.class);
        modeToBankServiceMap.put(BankType.ICICI, new ICICIBankService());
        modeToBankServiceMap.put(BankType.HDFC, new HDFCBankService());
        modeToBankServiceMap.put(BankType.SBI, new SBIBankService());
        return modeToBankServiceMap;
    }
}
