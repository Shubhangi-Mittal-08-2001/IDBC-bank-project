package com.company;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main<numberOfAccountsToUpdate> {

    public static void main(String[] args) {
        try {
            Scanner scan = new Scanner(System.in);
            boolean result;
            String firstName, lastName, address, contact, account_number,date;
            int age, customerID;
            char ch;
            do {
                System.out.println("Welcome to IDBC Bank what would you like to do:\n1.Open an account\n2.Access existing account");
                int choice1 = scan.nextInt();
                scan.nextLine();
                switch (choice1) {
                    case 1:
                        System.out.println("which type of account would you like to open:\n1.Save Account\n2.Pay Account\nenter your choice(1/2)");
                        int choice2 = scan.nextInt();
                        scan.nextLine();
                        switch (choice2) {
                            case 1:
                                do {
                                    System.out.println("-------Customer Registration-----");
                                    System.out.println("enter the First Name");
                                    firstName = scan.nextLine();
                                    System.out.println("Enter the Last Name");
                                    lastName = scan.nextLine();
                                    System.out.println("Enter the Address");
                                    address = scan.nextLine();
                                    System.out.println("Enter the contact");
                                    contact = scan.nextLine();
                                    System.out.println("Enter the age");
                                    age = scan.nextInt();
                                    scan.nextLine();
                                    result = Operations.registerCustomer(firstName, lastName, address, contact, age);
                                } while (result != true);
                                //generate an account number
                                account_number = Operations.generateAccountNumber();
                                System.out.println("The Account Number is: " + account_number);
                                customerID = Operations.returnCustomerId(firstName, lastName, address, contact, age);
                                System.out.println("The customerId is: " + customerID);
                                //System.out.println("enter the date in (yyyy-mm-dd) format");
                                //date = scan.nextLine();
                                System.out.println("enter the minimum amount you want to deposit(no minimum balance required)");
                                float amount = scan.nextFloat();
                                final String type = "save";
                                boolean result1 = Operations.registerAccount(account_number, customerID, amount, type);
                                if (result1 == true)
                                    System.out.println("----------You have successfully created a Save Account-----------");
                                else {
                                    System.out.println("There is some error in the input");
                                    while (result1 != true) {
                                        account_number = Operations.generateAccountNumber();
                                        System.out.println("The New Account Number is: " + account_number);
                                        customerID = Operations.returnCustomerId(firstName, lastName, address, contact, age);
                                        System.out.println("The New customerId is: " + customerID);
                                        result1 = Operations.registerAccount(account_number, customerID, amount, type);
                                    }
                                }
                                System.out.println("Thank you for choosing us,have a good day");
                                return;

                            case 2://pay account
                                do {
                                    System.out.println("-------Customer Registration-----");
                                    System.out.println("enter the First Name");
                                    firstName = scan.nextLine();
                                    System.out.println("Enter the Last Name");
                                    lastName = scan.nextLine();
                                    System.out.println("Enter the Address");
                                    address = scan.nextLine();
                                    System.out.println("Enter the contact");
                                    contact = scan.nextLine();
                                    System.out.println("Enter the age");
                                    age = scan.nextInt();
                                    scan.nextLine();
                                    result = Operations.registerCustomer(firstName, lastName, address, contact, age);
                                } while (result != true);
                                //generate an account number
                                System.out.println("enter the minimum amount you want to deposit(minimum balance required=1000)");
                                float amount2 = scan.nextFloat();
                                if (amount2 < 1000) {
                                    System.out.println("Minimum Balance requirement not fulfilled");
                                } else {
                                    account_number = Operations.generateAccountNumber();
                                    System.out.println("The Account Number is: " + account_number);
                                    customerID = Operations.returnCustomerId(firstName, lastName, address, contact, age);
                                    System.out.println("The customerId is: " + customerID);
                                    //scan.nextLine();
                                    final String type2 = "Pay";
                                    boolean result2 = Operations.registerAccount(account_number, customerID, amount2, type2);
                                    if (result2 == true)
                                        System.out.println("----------You have successfully created a Pay Account-----------");
                                    else {
                                        System.out.println("There is some error in the input");
                                        while (result2 != true) {
                                            account_number = Operations.generateAccountNumber();
                                            System.out.println("The New Account Number is: " + account_number);
                                            customerID = Operations.returnCustomerId(firstName, lastName, address, contact, age);
                                            System.out.println("The New customerId is: " + customerID);
                                            result2 = Operations.registerAccount(account_number, customerID, amount2, type2);
                                        }
                                    }
                                    System.out.println("Thank you for choosing us,have a good day");
                                    return;
                                }
                                break;
                            default:
                                System.out.println("Invalid choice");
                        }
                        break;
                    case 2://access existing account
                        int i = 0;

                        do {
                            System.out.println("Enter your Account number:");
                            account_number = scan.nextLine();
                            result = Operations.checkAccountExists(account_number);
                            if (result == false && i < 2)
                                System.out.println("The AccountNumber does no exists,try again u have " + (2 - i) + " attempt(s)");
                            i++;
                        } while (result == false && i < 3);
                        if (result == true && i < 3) {
                            System.out.println("What operation do u want to perform?\n1.View Account Details\n2.Transfer Funds\n3.Check Balance\n4.Calculate Interest\n5.Withdraw Money\n6.Make Payment\n7.View Previous transactions\n8.Deposit Money\n9.Exit");
                            int choice3 = scan.nextInt();
                            scan.nextLine();
                            boolean res;
                            switch (choice3) {
                                case 1://view account details
//                                    System.out.println("enter the account number");
//                                    account_number = scan.nextLine();
                                    //res = Operations.checkAccountExists(account_number);
//                                    if (res == true)
//                                        Operations.showAccountDetails(account_number);
//                                    else
//                                        System.out.println("wrong input");
                                    Operations.showAccountDetails(account_number);

                                    break;
                                case 2://transfer funds
                                    System.out.println("Enter the account number to which you want to transfer money");
                                    String to_accountNumber = scan.nextLine();
                                    System.out.println("Enter remarks if any: ");
                                    String remarks = scan.nextLine();
                                    System.out.println("enter the amount to be transferred");
                                    float amount = scan.nextFloat();
                                    final String transaction_type = "fund transfer";
                                    Operations.transferFunds(account_number, to_accountNumber, amount, remarks, transaction_type);
                                    break;
                                case 3://check balance
                                    System.out.println("current account Balance is: " + Operations.checkBalance(account_number));
                                    break;
                                case 4://calculate interest on current account balance if account type="save"
                                    String type = Operations.typeOfAccount(account_number);
                                    //System.out.println("type os account is: "+type);
                                    if (type.equalsIgnoreCase("pay")) {
                                        System.out.println("Cannot calculate interest on pay account");
                                    } else {
                                        Operations.calculateInterest(account_number);
                                    }
                                    break;
                                case 5://Withdraw money
                                    System.out.println("enter the money to withdraw");
                                    float money_to_withdraw = scan.nextFloat();
                                    scan.nextLine();
                                    String transaction_type2 = "Withdraw";
                                    Operations.withdrawMoney(account_number, money_to_withdraw, transaction_type2);
                                    break;
                                case 6://make payment
                                    System.out.println("Enter the account number to which you want to transfer money");
                                    String to_accountNumber1 = scan.nextLine();
                                    System.out.println("Enter remarks if any: ");
                                    String remarks1 = scan.nextLine();
                                    System.out.println("enter the amount to  be transferred");
                                    float amount1 = scan.nextFloat();
                                    final String transaction_type1 = "Make Payment";
                                    Operations.makePayment(account_number, to_accountNumber1, amount1, remarks1, transaction_type1);
                                    break;
                                case 7://view previous transaction log
                                    int number=Operations.showPreviousTransactions(account_number);
                                    if(number==0)
                                        System.out.println("No transactions done nothing to show");
                                    break;
                                case 8://deposit money
                                    System.out.println("enter the amount to be deposited");
                                    amount=scan.nextFloat();
                                    String transaction_type3="Deposit";
                                    Operations.depositMoney(account_number,amount,transaction_type3);
                                    break;
                                case 9:
                                    return;
                                default:
                                    System.out.println("Invalid input");
                            }
                        } else
                            System.out.println("No User exists with the given account number");
                    break;
                    default:
                        System.out.println("Invalid choice");

                }
                System.out.println("do you want to continue?(y/n)");
                ch = scan.next().charAt(0);
            } while (ch == 'y' || ch == 'Y');
        }
        catch (InputMismatchException e)
        {
            System.out.println("Incorrect input given");
        }
        //updating accounts tht are save type
        Operations.updateSaveAccountsSatisfyingUpdateCondition();

    }
}
