package com.phonepe.paymentgateway.mode;

import com.phonepe.paymentgateway.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PaymentModeServiceImpl implements PaymentModeService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private PaymentModeRepository paymentModeRepository;

    @Override
    public Set<Mode> listSupportedPayModes(Optional<Long> clientId) {
        if (clientId.isPresent()) {
            return clientService.getById(clientId.get()).getModeOfPayments();
        }

        return paymentModeRepository.getGenericModeOfPayments();
    }

    @Override
    public void addModeOfPayment(Set<Mode> modes, Optional<Long> clientId) {
        if (clientId.isPresent()) {
            clientService.getById(clientId.get()).getModeOfPayments().addAll(modes);
        }

        paymentModeRepository.addGenericModeOfPayments(modes);
    }

    @Override
    public boolean removePayMode(Mode mode, Optional<Long> clientId) {
        if (clientId.isPresent()) {
            clientService.getById(clientId.get()).getModeOfPayments().remove(mode);
        }

        paymentModeRepository.removeGenericModeOfPayments(mode);
        return true;
    }

}
