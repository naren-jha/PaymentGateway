package com.phonepe.paymentgateway.payment;

import com.phonepe.paymentgateway.bank.Bank;
import com.phonepe.paymentgateway.bank.BankType;
import com.phonepe.paymentgateway.bank.router.RouterStrategyType;
import com.phonepe.paymentgateway.client.Client;
import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.client.ClientService;
import com.phonepe.paymentgateway.exception.InvalidPaymentInputException;
import com.phonepe.paymentgateway.exception.PayModeNotSupportedException;
import com.phonepe.paymentgateway.mode.Mode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ClientService clientService;

    @Test
    void shouldTestThatDistributionAvailableForBankRouter() {
        List<String> distribution = paymentService.showDistribution();
        assertThat(distribution.size()).isEqualTo(6);
    }

    @Test
    void shouldTestThatPaymentIsMadeSuccessfully() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.CREDIT_CARD);
        modeOfPayments.add(Mode.NET_BANKING);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );
        Client flipkartClient = clientService.addClient("flipkart", modeOfPayments, clientAccounts);

        PaymentIssuingAccount issuer = PaymentIssuingAccount
                .builder()
                .nbUserName("testuser")
                .nbPassword("abc123")
                .build();
        Transaction transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        assertThat(transaction.getAmount()).isEqualTo(100.50);
    }

    @Test
    void shouldTestThatPaymentFailsCozPayModeNotSupportedForClient() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.CREDIT_CARD);
        modeOfPayments.add(Mode.DEBIT_CARD);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );
        Client flipkartClient = clientService.addClient("flipkart", modeOfPayments, clientAccounts);

        PaymentIssuingAccount issuer = PaymentIssuingAccount
                .builder()
                .nbUserName("testuser")
                .nbPassword("abc123")
                .build();

        Throwable thrown = catchThrowable(() -> {
            paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        });

        assertThat(thrown)
                .isInstanceOf(PayModeNotSupportedException.class)
                .hasMessageContaining("NET_BANKING mode is not supported for this client");
    }

    @Test
    void shouldTestThatPaymentFailsCozOfInvalidAmount() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.CREDIT_CARD);
        modeOfPayments.add(Mode.NET_BANKING);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );
        Client flipkartClient = clientService.addClient("flipkart", modeOfPayments, clientAccounts);

        PaymentIssuingAccount issuer = PaymentIssuingAccount
                .builder()
                .nbUserName("testuser")
                .nbPassword("abc123")
                .build();

        Throwable thrown = catchThrowable(() -> {
            paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.MODE_BASED, issuer, 0.0, flipkartClient.getId());
        });

        assertThat(thrown)
                .isInstanceOf(InvalidPaymentInputException.class)
                .hasMessageContaining("amount should be positive!");
    }

    @Test
    void shouldTestThatPaymentFailsForNetBankingCozOfInvalidAccountDetails() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.CREDIT_CARD);
        modeOfPayments.add(Mode.NET_BANKING);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );
        Client flipkartClient = clientService.addClient("flipkart", modeOfPayments, clientAccounts);

        PaymentIssuingAccount issuer = PaymentIssuingAccount
                .builder()
                .nbUserName("")
                .nbPassword("abc123")
                .build();

        Throwable thrown = catchThrowable(() -> {
            paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        });

        assertThat(thrown)
                .isInstanceOf(InvalidPaymentInputException.class)
                .hasMessageContaining("username is required for net banking mode of payment!");

        issuer.setNbUserName("testuser");
        issuer.setNbPassword("");
        thrown = catchThrowable(() -> {
            paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        });

        assertThat(thrown)
                .isInstanceOf(InvalidPaymentInputException.class)
                .hasMessageContaining("password is required for net banking mode of payment!");
    }

    @Test
    void shouldTestThatPaymentFailsForCreditOrDebitCardCozOfInvalidAccountDetails() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.CREDIT_CARD);
        modeOfPayments.add(Mode.DEBIT_CARD);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );
        Client flipkartClient = clientService.addClient("flipkart", modeOfPayments, clientAccounts);

        PaymentIssuingAccount issuer = PaymentIssuingAccount
                .builder()
                .cardNum("")
                .cardExpiryDate("31/01/2025")
                .cardCvv("567")
                .upiId("testuser@okhdfcbank")
                .build();

        Throwable thrown = catchThrowable(() -> {
            paymentService.makePayment(Mode.CREDIT_CARD, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        });
        assertThat(thrown)
                .isInstanceOf(InvalidPaymentInputException.class)
                .hasMessageContaining("card number is required for credit/debit card mode of payment!");

        issuer.setCardNum("1230-4560-7890-8675");
        issuer.setCardExpiryDate("");
        thrown = catchThrowable(() -> {
            paymentService.makePayment(Mode.DEBIT_CARD, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        });
        assertThat(thrown)
                .isInstanceOf(InvalidPaymentInputException.class)
                .hasMessageContaining("card expiry date is required for credit/debit card mode of payment!");

        issuer.setCardExpiryDate("31/01/2025");
        issuer.setCardCvv("");
        thrown = catchThrowable(() -> {
            paymentService.makePayment(Mode.CREDIT_CARD, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        });
        assertThat(thrown)
                .isInstanceOf(InvalidPaymentInputException.class)
                .hasMessageContaining("card CVV is required for credit/debit card mode of payment!");

        issuer.setCardCvv("1234");
        thrown = catchThrowable(() -> {
            paymentService.makePayment(Mode.CREDIT_CARD, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        });
        assertThat(thrown)
                .isInstanceOf(InvalidPaymentInputException.class)
                .hasMessageContaining("invalid CVV!");
    }

    @Test
    void shouldTestThatPaymentFailsForUpiModeCozOfUpiId() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.CREDIT_CARD);
        modeOfPayments.add(Mode.UPI);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );
        Client flipkartClient = clientService.addClient("flipkart", modeOfPayments, clientAccounts);

        PaymentIssuingAccount issuer = PaymentIssuingAccount
                .builder()
                .nbUserName("testuser")
                .nbPassword("abc123")
                .build();

        Throwable thrown = catchThrowable(() -> {
            paymentService.makePayment(Mode.UPI, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        });
        assertThat(thrown)
                .isInstanceOf(InvalidPaymentInputException.class)
                .hasMessageContaining("upi id is required for UPI mode of payment!");
    }

    @Test
    void shouldTestThatPayModeBasedRouterSelectsCorrectBank() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.CREDIT_CARD);
        modeOfPayments.add(Mode.DEBIT_CARD);
        modeOfPayments.add(Mode.NET_BANKING);
        modeOfPayments.add(Mode.UPI);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );
        Client flipkartClient = clientService.addClient("flipkart", modeOfPayments, clientAccounts);

        PaymentIssuingAccount issuer = PaymentIssuingAccount
                .builder()
                .nbUserName("testuser")
                .nbPassword("abc123")
                .cardNum("1230-4560-7890-8675")
                .cardExpiryDate("31/01/2025")
                .cardCvv("567")
                .upiId("testuser@okhdfcbank")
                .build();

        Transaction transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        assertThat(transaction.getAcquiringAccount().getBank().getType()).isEqualTo(BankType.ICICI);

        transaction = paymentService.makePayment(Mode.CREDIT_CARD, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        assertThat(transaction.getAcquiringAccount().getBank().getType()).isEqualTo(BankType.HDFC);

        transaction = paymentService.makePayment(Mode.DEBIT_CARD, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        assertThat(transaction.getAcquiringAccount().getBank().getType()).isEqualTo(BankType.SBI);

        transaction = paymentService.makePayment(Mode.UPI, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        assertThat(transaction.getAcquiringAccount().getBank().getType()).isEqualTo(BankType.SBI);
    }

    @Test
    void shouldTestThatFixedPercentageRouterSelectsCorrectBank() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.NET_BANKING);
        modeOfPayments.add(Mode.CREDIT_CARD);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );
        Client flipkartClient = clientService.addClient("flipkart", modeOfPayments, clientAccounts);

        PaymentIssuingAccount issuer = PaymentIssuingAccount
                .builder()
                .nbUserName("testuser")
                .nbPassword("abc123")
                .build();

        Map<BankType, Integer> routerCount = new EnumMap<>(BankType.class);

        Transaction transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        BankType bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        assertThat(routerCount.getOrDefault(BankType.ICICI, 0)).isGreaterThanOrEqualTo(1);
        assertThat(routerCount.getOrDefault(BankType.HDFC, 0)).isGreaterThanOrEqualTo(1);
        assertThat(routerCount.getOrDefault(BankType.SBI, 0)).isGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldTestThatErrorPercentageRouterSelectsCorrectBank() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.NET_BANKING);
        modeOfPayments.add(Mode.CREDIT_CARD);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );
        Client flipkartClient = clientService.addClient("flipkart", modeOfPayments, clientAccounts);

        PaymentIssuingAccount issuer = PaymentIssuingAccount
                .builder()
                .nbUserName("testuser")
                .nbPassword("abc123")
                .build();

        Map<BankType, Integer> routerCount = new EnumMap<>(BankType.class);

        Transaction transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        BankType bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        bankType = transaction.getAcquiringAccount().getBank().getType();
        routerCount.put(bankType, routerCount.getOrDefault(bankType, 0) + 1);

        assertThat(routerCount.getOrDefault(BankType.ICICI, 0)).isGreaterThanOrEqualTo(0);
        assertThat(routerCount.getOrDefault(BankType.HDFC, 0)).isGreaterThanOrEqualTo(0);
        assertThat(routerCount.getOrDefault(BankType.SBI, 0)).isGreaterThanOrEqualTo(0);
    }

    @Test
    void shouldTestThatDefaultAccountIsSelectedForClientWhenAskedTypeIsNotAvailable() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.CREDIT_CARD);
        modeOfPayments.add(Mode.NET_BANKING);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );
        Client flipkartClient = clientService.addClient("flipkart", modeOfPayments, clientAccounts);

        PaymentIssuingAccount issuer = PaymentIssuingAccount
                .builder()
                .nbUserName("testuser")
                .nbPassword("abc123")
                .build();
        Transaction transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
        assertThat(transaction.getAcquiringAccount().getBank().getType()).isEqualTo(BankType.HDFC);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        assertThat(transaction.getAcquiringAccount().getBank().getType()).isEqualTo(BankType.HDFC);

        transaction = paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
        assertThat(transaction.getAcquiringAccount().getBank().getType()).isEqualTo(BankType.HDFC);
    }
}