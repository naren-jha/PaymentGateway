package com.phonepe.paymentgateway;

import com.phonepe.paymentgateway.client.Client;
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
import java.util.stream.Collectors;

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
		modeOfPayments.add(Mode.DEBIT_CARD);
		Client flipkartClient = clientService.addClient("flipkart", modeOfPayments);
		log.info("client added: {} ", flipkartClient);

		Client snapDealClient = clientService.addClient("snapdeal", Arrays.stream(Mode.values()).collect(Collectors.toSet()));
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


		// make payment
		PaymentIssuingAccount issuer = PaymentIssuingAccount
				.builder()
				.nbUserName("testuser")
				.nbPassword("abc123")
				.build();
		paymentService.makePayment(Mode.NET_BANKING, issuer, 100.50, flipkartClient.getId());
	}

}
