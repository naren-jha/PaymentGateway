package com.phonepe.paymentgateway.mode;

import com.phonepe.paymentgateway.client.Client;
import com.phonepe.paymentgateway.client.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class PaymentModeServiceTest {

    @Autowired
    private PaymentModeService paymentModeService;

    @Autowired
    private ClientService clientService;

    @Test
    void shouldTestThatGenericPayModeIsAddedSuccessfully() {
        Set<Mode> genericModesOfPayments = new HashSet<>();
        genericModesOfPayments.add(Mode.CREDIT_CARD);
        genericModesOfPayments.add(Mode.DEBIT_CARD);
        genericModesOfPayments.add(Mode.NET_BANKING);
        genericModesOfPayments.add(Mode.UPI);
        paymentModeService.addModeOfPayment(genericModesOfPayments, Optional.empty());

        Set<Mode> supportedPayModes = paymentModeService.listSupportedPayModes(Optional.empty());
        assertThat(supportedPayModes).isEqualTo(genericModesOfPayments);
    }

    @Test
    void shouldTestThatClientSpecificPayModeIsAddedSuccessfully() {
        Client flipkartClient = clientService.addClient("flipkart", new HashSet<>(), null);

        Set<Mode> flipkartModesOfPayments = new HashSet<>();
        flipkartModesOfPayments.add(Mode.CREDIT_CARD);
        flipkartModesOfPayments.add(Mode.DEBIT_CARD);
        paymentModeService.addModeOfPayment(flipkartModesOfPayments, Optional.of(flipkartClient.getId()));

        Set<Mode> supportedPayModes = paymentModeService.listSupportedPayModes(Optional.of(flipkartClient.getId()));
        assertThat(supportedPayModes).isEqualTo(flipkartModesOfPayments);
    }

    @Test
    void shouldTestThatGenericPayModeIsRemovedSuccessfully() {
        Set<Mode> genericModesOfPayments = new HashSet<>();
        genericModesOfPayments.add(Mode.CREDIT_CARD);
        genericModesOfPayments.add(Mode.DEBIT_CARD);
        genericModesOfPayments.add(Mode.NET_BANKING);
        genericModesOfPayments.add(Mode.UPI);
        paymentModeService.addModeOfPayment(genericModesOfPayments, Optional.empty());

        assertThat(paymentModeService.removePayMode(Mode.NET_BANKING, Optional.empty())).isEqualTo(true);
    }

    @Test
    void shouldTestThatClientSpecificPayModeIsRemovedSuccessfully() {
        Client flipkartClient = clientService.addClient("flipkart", new HashSet<>(), null);

        Set<Mode> flipkartModesOfPayments = new HashSet<>();
        flipkartModesOfPayments.add(Mode.CREDIT_CARD);
        flipkartModesOfPayments.add(Mode.DEBIT_CARD);
        paymentModeService.addModeOfPayment(flipkartModesOfPayments, Optional.of(flipkartClient.getId()));

        assertThat(paymentModeService.removePayMode(Mode.NET_BANKING, Optional.of(flipkartClient.getId()))).isEqualTo(true);
    }
}