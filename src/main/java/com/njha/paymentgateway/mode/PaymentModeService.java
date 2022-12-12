package com.njha.paymentgateway.mode;

import java.util.Optional;
import java.util.Set;

public interface PaymentModeService {
    Set<Mode> listSupportedPayModes(Optional<Long> clientId);
    void addModeOfPayment(Set<Mode> modes, Optional<Long> clientId);
    boolean removePayMode(Mode mode, Optional<Long> clientId);
}
