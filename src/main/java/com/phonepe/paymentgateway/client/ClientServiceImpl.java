package com.phonepe.paymentgateway.client;

import com.phonepe.paymentgateway.mode.Mode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client addClient(String name, Set<Mode> modes, List<ClientBankAccount> acquiringAccounts) {
        return clientRepository.addClient(name, modes, acquiringAccounts);
    }

    @Override
    public boolean removeClient(Long id) {
        return clientRepository.removeClient(id);
    }

    @Override
    public boolean hasClient(Long id) {
        return clientRepository.hasClient(id);
    }

    @Override
    public Client getById(Long id) {
        return clientRepository.getById(id);
    }
}
