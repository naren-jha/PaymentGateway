# Question

Payments is an integral component of any e-commerce or fintech.
With the advent of digital india, we have seen different types of payments ecosystem rising up - Payments gateways, UPI, Rupay Network etc. These are easy to integrate and any org can get started with this in a matter of days.

With the onset of different payment players, organizations integrate different payment gateways and shuffle between them to suit their use cases best.

Based on use cases, customers can opt for payment via card / upi / net banking etc. Below is the general runbook for making a payment.

* Select a pay mode
* Enter details
  * For netbanking - enter username and password
  * For credit / debit card - enter card details ( number, expiry, cvv etc )
  * UPI - enter vpa

Implement a payments gateway which help facilitate a payment for its client, below points should be kept in mind while implementation

## Real world example

Flipkart has integrated multiple PGs like Razorpay, Citrus, PaySafe, CCAvenue etc. They use these PGs based on different use cases. For example, divert all credit card transaction to RazorPay while Netbanking goes to CCAvenue.

These PGs internally have direct integration with different banks which facilitate the payments.

Expectation from this code is to build PG like a Razorpay / CCAvenue which allows onboarding clients like Flipkart and process a transaction.

Client in this case is Flipkart ( PG can have more than one clients )
PG is what candidate has to implement
PG can have more than one bank, candidates are free to implement a mock or full fledged class

## Feature Requirement:

* PG should support onboarding multiple clients.
* PG should have multiple bank integrations ( like HDFC, ICICI, SBI etc )
* PG should have facility to support different payment methods - UPI, Credit / Debit Card, Netbanking etc
* PG should have facility to divert to specific bank based on certain criteria - a router basically - (for e.g. all credit card transaction goes to HDFC and netbanking is redirected to ICICI )
* PG should have facility to allocate specific percentage between different banks - say bank1 takes 30% of total traffic while remaining 70% go to bank2
* Clients should have an option to opt for specific payment methods. - only UPI, everything except netbanking etc


## Assumptions:

* Banks can randomly return success / failure - candidates can create a random function to mock this behaviour.
* Payments should be processed using an instrument only if specific parameter for that payment is passed - netbanking might need user id / password but credit card will only work with card details
* Banks require OTP verification after instrument details are verified ( applicable for netbanking / card transaction ) for sake of simplicity, transactions will go through without OTP.


## Code Expectation:

* Everything has to be in memory.
* Candidate can opt for any language for implementation
* Simple and basic function are expected as entry point - no marks for providing fancy restful API or framework implementation
* Because this is a machine coding round, heavy focus would be given on code quality, candidate should not focus too much time on algo which compromises with implementation time


## Requirements:
below are various functions that should be supported with necessary parameters passed

* addClient() - add a client in PG
* removeClinet() - remove client from PG
* hasClient() - does a client exist in the PG?
* listSupportedPaymodes() - show paymodes support by PG. if a client is passed as parameter, all supported paymodes for the clients should be shown.
* addSupportForPaymode() - add paymodes support in the PG. If a client is passed as parameter, add paymode for the clients.
* removePaymode() - remove paymode ( both from PG or client basis parameter)
* showDistribution() - show active distribution percentage of router
* makePayment( //necessary payment details )


## Evaluation criteria:

* Working code
* Code readability
* Implementation of OOPs / OOD principles with proper Separation of concerns
* Testability - a TDD approach ( not to be mixed with test cases )
* Language proficiency
* Test Cases ( bonus points )


## Extension problem:

* Can a router dynamically switch the traffic basis success percentage of Bank?
* Can the router switch based on payment instruments as well instead of just Bank?
