package com.phonepe.paymentgateway.bank;

import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.payment.PaymentIssuingAccount;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Data
@Slf4j
public class HDFCBankService implements BankService {
    @Override
    public PaymentBankResponse makePayment(PaymentIssuingAccount issuingAccount, ClientBankAccount acquiringAccount, double amount) {
        log.info("Making a payment using HDFC bank. API call will happen here.");
        int randomNumber = ThreadLocalRandom.current().nextInt(0, 5);
        boolean isPaymentSuccessful = randomNumber != 0;

        PaymentBankResponse paymentBankResponse = PaymentBankResponse.builder().build();
        if (isPaymentSuccessful) {
            log.info("Payment successful using HDFC bank from account {} to account {} for amount {}", issuingAccount, acquiringAccount, amount);
            paymentBankResponse.setStatus(true);
        }
        else {
            log.info("Payment failed using HDFC bank from account {} to account {} for amount {}", issuingAccount, acquiringAccount, amount);
            paymentBankResponse.setStatus(false);
        }
        return paymentBankResponse;
    }
}
