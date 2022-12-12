package com.njha.paymentgateway.mode;

import java.util.Set;

public interface PaymentModeRepository {
    void addGenericModeOfPayments(Set<Mode> modes);
    Set<Mode> getGenericModeOfPayments();
    void removeGenericModeOfPayments(Mode mode);
}
