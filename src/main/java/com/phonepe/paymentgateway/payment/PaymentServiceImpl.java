package com.phonepe.paymentgateway.payment;

import com.phonepe.paymentgateway.bank.BankService;
import com.phonepe.paymentgateway.bank.strategy.BankSelectionResponse;
import com.phonepe.paymentgateway.bank.strategy.BankSelectionStrategyFactory;
import com.phonepe.paymentgateway.bank.strategy.BankSelectionStrategyType;
import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.client.Client;
import com.phonepe.paymentgateway.client.ClientService;
import com.phonepe.paymentgateway.exception.ClientNotFoundException;
import com.phonepe.paymentgateway.exception.InvalidPaymentInputException;
import com.phonepe.paymentgateway.mode.Mode;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final BankSelectionStrategyType bankSelectionStrategyType = BankSelectionStrategyType.MODE_BASED;

    @Autowired
    private BankSelectionStrategyFactory bankSelectionStrategyFactory;

    @Autowired
    private ClientService clientService;

    @Override
    public void makePayment(Mode mode, PaymentIssuingAccount issuingAccount, double amount, Long clientId) {
        validateRequest(mode, issuingAccount, amount);
        Client client = clientService.getById(clientId);
        if (Objects.isNull(client)) {
            log.error("client not registered!");
            throw new ClientNotFoundException("client not found with id " + clientId);
        }

        log.info("initiating {} payment for client {}", mode, client.getName());
        BankSelectionResponse bankSelectionResponse = bankSelectionStrategyFactory
                .getBankSelectionStrategy(bankSelectionStrategyType)
                .selectBank(mode, client.getAcquiringBankAccounts());

        BankService bankService = bankSelectionResponse.getBankService();
        ClientBankAccount acquiringAccount = bankSelectionResponse.getSelectedAccount();
        bankService.makePayment(issuingAccount, acquiringAccount, amount);
    }

    private void validateRequest(Mode mode, PaymentIssuingAccount issuingAccount, double amount) {
        if (amount <= 0) {
            throw new InvalidPaymentInputException("amount should be positive!");
        }
        if (mode.equals(Mode.NET_BANKING)) {
            if (Strings.isBlank(issuingAccount.getNbUserName())) {
                throw new InvalidPaymentInputException("username is required for net banking mode of payment!");
            }
            if (Strings.isBlank(issuingAccount.getNbPassword())) {
                throw new InvalidPaymentInputException("password is required for net banking mode of payment!");
            }
        }
        if (mode.equals(Mode.CREDIT_CARD) || mode.equals(Mode.DEBIT_CARD)) {
            if (Strings.isBlank(issuingAccount.getCardNum())) {
                throw new InvalidPaymentInputException("card number is required for credit/debit card mode of payment!");
            }
            if (Strings.isBlank(issuingAccount.getCardExpiryDate())) {
                throw new InvalidPaymentInputException("card expiry date is required for credit/debit card mode of payment!");
            }
            if (Strings.isBlank(issuingAccount.getCardCvv())) {
                throw new InvalidPaymentInputException("card CVV is required for credit/debit card mode of payment!");
            }
            if (issuingAccount.getCardCvv().length() != 3 || is3DigitNumber(issuingAccount.getCardCvv())) {
                throw new InvalidPaymentInputException("invalid CVV!");
            }
        }
        if (mode.equals(Mode.UPI)) {
            if (Strings.isBlank(issuingAccount.getUpiId())) {
                throw new InvalidPaymentInputException("upi id is required for UPI mode of payment!");
            }
        }
    }

    public static boolean is3DigitNumber(String str) {
        return str.matches("\\d{3}");
    }
}
