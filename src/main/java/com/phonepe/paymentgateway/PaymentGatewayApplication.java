package com.phonepe.paymentgateway;

import com.phonepe.paymentgateway.bank.Bank;
import com.phonepe.paymentgateway.bank.BankType;
import com.phonepe.paymentgateway.bank.router.RouterStrategyType;
import com.phonepe.paymentgateway.client.Client;
import com.phonepe.paymentgateway.client.ClientBankAccount;
import com.phonepe.paymentgateway.client.ClientService;
import com.phonepe.paymentgateway.mode.Mode;
import com.phonepe.paymentgateway.mode.PaymentModeService;
import com.phonepe.paymentgateway.payment.PaymentIssuingAccount;
import com.phonepe.paymentgateway.payment.PaymentService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
@Slf4j
public class PaymentGatewayApplication {

	@Autowired
	private ClientService clientService;

	@Autowired
	private PaymentModeService paymentModeService;

	@Autowired
	private PaymentService paymentService;

	public static void main(String[] args) {
		SpringApplication.run(PaymentGatewayApplication.class, args);
	}

	@PostConstruct
	public void start() {
		// Client operations
		Set<Mode> modeOfPayments = new HashSet<>();
		modeOfPayments.add(Mode.CREDIT_CARD);
		modeOfPayments.add(Mode.NET_BANKING);

		List<ClientBankAccount> clientAccounts = Arrays.asList(
				new ClientBankAccount(0L, new Bank(0L, "HDFC Bank", BankType.HDFC), "123", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
				new ClientBankAccount(1L, new Bank(1L, "ICICI Bank", BankType.ICICI), "456", "Flipkart Corp", "Kormangala, Bangalore", "ABC123"),
				new ClientBankAccount(2L, new Bank(2L, "SBI Bank", BankType.SBI), "789", "Flipkart Corp", "Kormangala, Bangalore", "ABC123")
		);

		Client flipkartClient = clientService.addClient("flipkart", modeOfPayments, clientAccounts);
		log.info("client added: {} ", flipkartClient);

		Client snapDealClient = clientService.addClient("snapdeal", null, null);
		log.info("client added: {} ", snapDealClient);

		boolean res = clientService.hasClient(flipkartClient.getId());
		String msg = flipkartClient.getName() + (res ? " client exists" : " client doesn't exist");
		log.info(msg);

		res = clientService.hasClient(snapDealClient.getId());
		msg = snapDealClient.getName() + (res ? " client exists" : " client doesn't exist");
		log.info(msg);

		res = clientService.removeClient(snapDealClient.getId());
		log.info("{} client removed ", snapDealClient.getName());

		res = clientService.hasClient(flipkartClient.getId());
		msg = flipkartClient.getName() + (res ? " client exists" : " client doesn't exist");
		log.info(msg);

		res = clientService.hasClient(snapDealClient.getId());
		msg = snapDealClient.getName() + (res ? " client exists" : " client doesn't exist");
		log.info(msg);

		// Mode of payments operations
		Set<Mode> genericModesOfPayments = new HashSet<>();
		genericModesOfPayments.add(Mode.CREDIT_CARD);
		genericModesOfPayments.add(Mode.DEBIT_CARD);
		genericModesOfPayments.add(Mode.NET_BANKING);
		genericModesOfPayments.add(Mode.UPI);
		paymentModeService.addModeOfPayment(genericModesOfPayments, Optional.empty());

		Set<Mode> supportedPayModes = paymentModeService.listSupportedPayModes(Optional.empty());
		log.info("generic supported modes of payment are {}", supportedPayModes);

		supportedPayModes = paymentModeService.listSupportedPayModes(Optional.of(flipkartClient.getId()));
		log.info("supported modes of payment for client {} are {}", flipkartClient.getName(), supportedPayModes);

		Set<Mode> modeOfPaymentsFlipkart = new HashSet<>();
		modeOfPaymentsFlipkart.add(Mode.UPI);
		modeOfPaymentsFlipkart.add(Mode.CREDIT_CARD);
		log.info("adding UPI mode of payment for flipkart");
		paymentModeService.addModeOfPayment(modeOfPaymentsFlipkart, Optional.of(flipkartClient.getId()));

		supportedPayModes = paymentModeService.listSupportedPayModes(Optional.of(flipkartClient.getId()));
		log.info("updated supported modes of payment for client {} are {}", flipkartClient.getName(), supportedPayModes);

		paymentModeService.removePayMode(Mode.UPI, Optional.empty());
		log.info("removed UPI from generic modes of payment");
		supportedPayModes = paymentModeService.listSupportedPayModes(Optional.empty());
		log.info("updated generic supported modes of payment are {}", supportedPayModes);

		paymentModeService.removePayMode(Mode.UPI, Optional.of(flipkartClient.getId()));
		log.info("removed UPI from modes of payment for {}", flipkartClient.getName());
		supportedPayModes = paymentModeService.listSupportedPayModes(Optional.of(flipkartClient.getId()));
		log.info("updated supported modes of payment for client {} are {}", flipkartClient.getName(), supportedPayModes);

		// distribution
		List<String> distribution = paymentService.showDistribution();
		log.info("distribution:- ");
		distribution.forEach(log::info);

		// make payment
		PaymentIssuingAccount issuer = PaymentIssuingAccount
				.builder()
				.nbUserName("testuser")
				.nbPassword("abc123")
				.cardNum("1230-4560-7890-8675")
				.cardExpiryDate("31/01/2025")
				.cardCvv("567")
				.upiId("testuser@okhdfcbank")
				.build();
//		paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.MODE_BASED, issuer, 100.50, flipkartClient.getId());
//		paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
//		paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.FIXED_PERCENTAGE, issuer, 100.50, flipkartClient.getId());

		paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
		paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
		paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
		paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
		paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
		paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
		paymentService.makePayment(Mode.NET_BANKING, RouterStrategyType.ERROR_PERCENTAGE, issuer, 100.50, flipkartClient.getId());
	}

}
