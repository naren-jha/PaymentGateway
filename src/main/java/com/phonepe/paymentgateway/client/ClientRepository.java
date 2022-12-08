package com.phonepe.paymentgateway.client;

import com.phonepe.paymentgateway.mode.Mode;

import java.util.List;
import java.util.Set;

public interface ClientRepository {
    Client addClient(String name, Set<Mode> modes);
    boolean removeClient(Long id);
    boolean hasClient(Long id);
    Client getById(Long id);
}
