package com.njha.paymentgateway.client;

import com.njha.paymentgateway.bank.Bank;
import com.njha.paymentgateway.bank.BankType;
import com.njha.paymentgateway.mode.Mode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Test
    void shouldTestThatClientIsAddedSuccessfully() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.CREDIT_CARD);
        modeOfPayments.add(Mode.NET_BANKING);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );

        Client flipkartClient = clientService.addClient("flipkart", modeOfPayments, clientAccounts);
        assertThat(flipkartClient.getName()).isEqualTo("flipkart");
    }

    @Test
    void shouldTestThatClientIsFetchedSuccessfully() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.CREDIT_CARD);
        modeOfPayments.add(Mode.NET_BANKING);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );

        Client actual = clientService.addClient("flipkart", modeOfPayments, clientAccounts);
        Client expected = clientService.getById(actual.getId());
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @Test
    void shouldTestThatClientExist() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.CREDIT_CARD);
        modeOfPayments.add(Mode.NET_BANKING);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );

        Client actual = clientService.addClient("flipkart", modeOfPayments, clientAccounts);
        assertThat(clientService.hasClient(actual.getId())).isEqualTo(true);
    }

    @Test
    void shouldTestThatClientIsRemovedSuccessfully() {
        Set<Mode> modeOfPayments = new HashSet<>();
        modeOfPayments.add(Mode.CREDIT_CARD);
        modeOfPayments.add(Mode.NET_BANKING);

        List<ClientBankAccount> clientAccounts = Arrays.asList(
                new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
                new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
        );

        Client actual = clientService.addClient("flipkart", modeOfPayments, clientAccounts);
        assertThat(clientService.removeClient(actual.getId())).isEqualTo(true);
    }
}