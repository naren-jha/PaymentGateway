package com.phonepe.paymentgateway.client;

import com.phonepe.paymentgateway.BankType;
import com.phonepe.paymentgateway.bank.Bank;
import com.phonepe.paymentgateway.mode.Mode;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private Map<Long, Client> clients = new HashMap<>();

    @Override
    public Client addClient(String name, Set<Mode> modes) {
        Long clientId = Long.valueOf(clients.size());
        Client client = Client
                .builder()
                .id(clientId)
                .modeOfPayments(modes)
                .name(name)
                .build();


        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );
        client.setAcquiringBankAccounts(clientAccounts);

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
