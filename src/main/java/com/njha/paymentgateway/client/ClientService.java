package com.njha.paymentgateway.client;

import com.njha.paymentgateway.mode.Mode;

import java.util.List;
import java.util.Set;

public interface ClientService {
    Client addClient(String name, Set<Mode> modes, List<ClientBankAccount> acquiringAccounts);
    boolean removeClient(Long id);
    boolean hasClient(Long id);
    Client getById(Long id);
}
