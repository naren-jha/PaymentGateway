package com.njha.paymentgateway.mode;

import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class PaymentModeRepositoryImpl implements PaymentModeRepository {

    private Set<Mode> genericModeOfPayments = new HashSet<>();

    @Override
    public void addGenericModeOfPayments(Set<Mode> modes) {
        genericModeOfPayments.addAll(modes);
    }

    @Override
    public Set<Mode> getGenericModeOfPayments() {
        return genericModeOfPayments;
    }

    @Override
    public void removeGenericModeOfPayments(Mode mode) {
        genericModeOfPayments.remove(mode);
    }
}
