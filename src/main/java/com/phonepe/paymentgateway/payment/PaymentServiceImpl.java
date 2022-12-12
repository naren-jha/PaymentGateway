package com.phonepe.paymentgateway.payment;

import com.phonepe.paymentgateway.bank.BankService;
import com.phonepe.paymentgateway.bank.BankType;
import com.phonepe.paymentgateway.bank.PaymentBankResponse;
import com.phonepe.paymentgateway.bank.router.RouterStrategyResponse;
import com.phonepe.paymentgateway.bank.router.RouterStrategyFactory;
import com.phonepe.paymentgateway.bank.router.RouterStrategyType;
import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.client.Client;
import com.phonepe.paymentgateway.client.ClientService;
import com.phonepe.paymentgateway.exception.ClientNotFoundException;
import com.phonepe.paymentgateway.exception.InvalidPaymentInputException;
import com.phonepe.paymentgateway.exception.PayModeNotSupportedException;
import com.phonepe.paymentgateway.mode.Mode;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final RouterStrategyType routerStrategy = RouterStrategyType.MODE_BASED;

    @Autowired
    private RouterStrategyFactory bankSelectionStrategyFactory;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionRepository transactionRepository;

    // distribution config
    @Autowired
    private Map<Mode, BankType> modeToBankTypeMap;

    @Autowired
    private Map<BankType, Integer> bankToPercentageMap;

    @Override
    public List<String> showDistribution() {
        List<String> distribution = new ArrayList<>();
        for (Mode mode : modeToBankTypeMap.keySet()) {
            distribution.add(String.format("All payments of %s will go to %s bank", mode, modeToBankTypeMap.get(mode)));
        }
        for (BankType bankType : bankToPercentageMap.keySet()) {
            distribution.add(String.format("%d percent of total payments will go to %s", bankToPercentageMap.get(bankType), bankType));
        }
        return distribution;
    }

    @Override
    public Transaction makePayment(Mode mode, RouterStrategyType routerType, PaymentIssuingAccount issuingAccount, double amount, Long clientId) {

        // set default router strategy if not provided by client
        if (Objects.isNull(routerType)) {
            log.info("opting for default router strategy MODE_BASED since one was not provided in client request");
            routerType = RouterStrategyType.MODE_BASED;
        }

        Client client = clientService.getById(clientId);
        if (Objects.isNull(client)) {
            log.error("client not registered!");
            throw new ClientNotFoundException("client not found with id " + clientId);
        }
        validateRequest(mode, client, issuingAccount, amount);

        log.info("initiating {} payment for client {}", mode, client.getName());
        RouterStrategyResponse bankSelectionResponse = bankSelectionStrategyFactory
                .getBankSelectionStrategy(routerType)
                .selectBank(mode, client.getAcquiringBankAccounts());

        BankService bankService = bankSelectionResponse.getBankService();
        ClientBankAccount acquiringAccount = bankSelectionResponse.getSelectedAccount();
        PaymentBankResponse paymentBankResponse = bankService.makePayment(issuingAccount, acquiringAccount, amount);

        // save transaction
        // TODO: maybe create a transactionService and then call transactionService.saveTransaction(transaction)
        Transaction transaction = Transaction
                .builder()
                .issuingAccount(issuingAccount)
                .acquiringAccount(acquiringAccount)
                .amount(amount)
                .status(paymentBankResponse.isStatus())
                .createdAt(LocalDate.now())
                .routerType(routerType)
                .build();
        transactionRepository.saveTransaction(transaction);
        return transaction;
    }

    private void validateRequest(Mode mode, Client client, PaymentIssuingAccount issuingAccount, double amount) {
        if (!client.getModeOfPayments().contains(mode)) {
            throw new PayModeNotSupportedException(mode + " mode is not supported for this client");
        }
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
            if (!is3DigitNumber(issuingAccount.getCardCvv())) {
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
