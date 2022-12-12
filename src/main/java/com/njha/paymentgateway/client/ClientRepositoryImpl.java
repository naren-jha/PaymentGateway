package com.njha.paymentgateway.client;

import com.njha.paymentgateway.mode.Mode;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private Map<Long, Client> clients = new HashMap<>();

    @Override
    public Client addClient(String name, Set<Mode> modes, List<ClientBankAccount> acquiringAccounts) {
        Long clientId = Long.valueOf(clients.size());
        Client client = Client
                .builder()
                .id(clientId)
                .modeOfPayments(modes)
                .acquiringBankAccounts(acquiringAccounts)
                .name(name)
                .build();

        clients.put(clientId, client);
        return client;
    }

    @Override
    public boolean removeClient(Long id) {
        clients.remove(id);
        return true;
    }

    @Override
    public boolean hasClient(Long id) {
        return clients.containsKey(id);
    }

    @Override
    public Client getById(Long id) {
        return clients.get(id);
    }
}
