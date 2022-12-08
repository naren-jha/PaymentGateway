package com.phonepe.paymentgateway.client;

import com.phonepe.paymentgateway.mode.Mode;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class Client {
    private Long id;
    private String name;
    private Set<Mode> modeOfPayments;
    private List<ClientBankAccount> acquiringBankAccounts;
    // other fields as needed
}


