package com.company;

import java.lang.ref.PhantomReference;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Date;

class Operations{
    static float interest = 0.025f;
    static int i=1;
    static String nextYear="2023";
    static boolean registerCustomer(String firstName,String lastName,String address,String contact,int age) {
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")){
            PreparedStatement ps=con.prepareStatement("INSERT INTO CUSTOMER(FIRST_NAME,LAST_NAME,ADDRESS,CONTACT,AGE) VALUES(?,?,?,?,?)");
            ps.setString(1,firstName);
            ps.setString(2,lastName);
            ps.setString(3,address);
            ps.setString(4,contact);
            ps.setInt(5,age);
            int result=ps.executeUpdate();
            if(result>0)
            {
                System.out.println("registration successful");
            }
            return true;
        }
        catch (SQLException e) {
            System.out.println("Sorry insertion unsuccessful,try again!");
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static String generateAccountNumber()
    {
        Random random=new Random();
        int accountNumber=random.nextInt(9999999);
        int accountNumber2=random.nextInt(99999);
        String str1=String.valueOf(accountNumber);
        String str2=String.valueOf(accountNumber2);
        return str1+str2;
    }
    public static int returnCustomerId(String firstName,String lastName,String address,String contact,int age)
    {
        int value=0;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")) {
            PreparedStatement ps= con.prepareStatement("select customerid from customer where first_name=? and last_name=? and address=? and contact=? and age=?");
            ps.setString(1,firstName);
            ps.setString(2,lastName);
            ps.setString(3,address);
            ps.setString(4,contact);
            ps.setInt(5,age);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                value=rs.getInt("customerid");

            }
            return value;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return value;
        }
        }
    public static boolean registerAccount(String account_number,int customerID,float amount,String type)
    {
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")){
            PreparedStatement ps=con.prepareStatement("INSERT INTO accounts(ACCOUNT_NUMBER,CUSTOMERID,BALANCE,account_type) VALUES(?,?,?,?)");
            ps.setString(1,account_number);
            ps.setInt(2,customerID);
            ps.setFloat(3,amount);
            ps.setString(4,type);
            int result=ps.executeUpdate();
            if(result>0)
            {
                System.out.println("account  registered successfully");
                return true;
            }
            else
                return false;

        }
        catch (SQLException e) {
            System.out.println("Sorry insertion unsuccessful,try again!");
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static boolean showAccountDetails(String account_number)
    {
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")) {
            PreparedStatement ps = con.prepareStatement("select * from accounts where account_number=?");
            ps.setString(1,account_number);
            ResultSet rs=ps.executeQuery();
            System.out.format("%s %15s %15s %10s %15s","Account_number","CustomerId","Date of Opening","Balance","Account Type");
            System.out.println();
            while(rs.next())
            {
                System.out.format("%s %15d %15s %15f %11s",rs.getString(1),rs.getInt(2),rs.getDate(3),rs.getFloat(4),rs.getString(5));
                System.out.println();
            }
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("No matching records found,try again");
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static void transferFunds(String from_accountNumber,String to_accountNumber,float amount,String remarks,String transactionType) {
        float value = 0;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")) {
            //first check if balance is enough or not for transaction
            PreparedStatement ps3 = con.prepareStatement("select balance from accounts where account_number=?");
            ps3.setString(1, from_accountNumber);
            ResultSet rs = ps3.executeQuery();
            while (rs.next()) {
                value = rs.getInt("balance");
            }
            if (value > amount) {
                PreparedStatement ps = con.prepareStatement("update accounts set balance=balance-? where account_number=?");
                ps.setFloat(1, amount);
                ps.setString(2, from_accountNumber);
                int result = ps.executeUpdate();
                if (result > 0) {
                    System.out.println("Updated the account balance");
                    //updating the transaction log
                    ps = con.prepareStatement("insert into transaction_log(from_account,to_account,amount,debit,remarks,transaction_type) values(?,?,?,?,?,?)");
                    ps.setString(1, from_accountNumber);
                    ps.setString(2, to_accountNumber);
                    ps.setFloat(3, amount);
                    ps.setFloat(4, amount);
                    ps.setString(5, remarks);
                    ps.setString(6, transactionType);
                    ps.executeUpdate();
                    boolean r = checkAccountExists(to_accountNumber);
                    if (r == true) {
                        PreparedStatement ps2 = con.prepareStatement("insert into transaction_log(from_account,to_account,amount,credit,remarks,transaction_type) values(?,?,?,?,?,?)");
                        ps2.setString(1, from_accountNumber);
                        ps2.setString(2, to_accountNumber);
                        ps2.setFloat(3, amount);
                        ps2.setFloat(4, amount);
                        ps2.setString(5, remarks);
                        ps2.setString(6, transactionType);
                        ps2.executeUpdate();
                        PreparedStatement ps4= con.prepareStatement("update accounts set balance=balance+? where account_number=?");
                        ps4.setFloat(1, amount);
                        ps4.setString(2, to_accountNumber);
                        ps4.executeUpdate();
                    }
                }
            }
            else
            {
                System.out.println("amount insufficient");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Update unsuccessful");
            System.out.println(e.getMessage());

        }
    }
    public  static boolean checkAccountExists(String account_number)
    {
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")) {
            PreparedStatement ps = con.prepareStatement("select * from accounts where account_number=?");
            ps.setString(1, account_number);
            ResultSet rs = ps.executeQuery();
            boolean r=rs.next();
            if(r==true)
            {
                System.out.println("User exists");
                return true;
            }
            else
                return false;

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static float checkBalance(String accountNumber)
    {
        float account_balance=0;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")) {
            PreparedStatement ps = con.prepareStatement("select balance from accounts where account_number=?");
            ps.setString(1, accountNumber);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                account_balance=rs.getFloat("balance");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return account_balance;
    }
    public static String typeOfAccount(String accountNumber)
    {
        String type=null;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")) {
            PreparedStatement ps = con.prepareStatement("select account_type from accounts where account_number=?");
            ps.setString(1, accountNumber);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                type=rs.getString("account_type");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return type;
    }
    public static void calculateInterest(String accountNumber)
    {
        float interestAmount,balance=0;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")) {
            PreparedStatement ps = con.prepareStatement("select balance from accounts where account_number=?");
            ps.setString(1, accountNumber);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                balance=rs.getFloat("balance");
            }
            interestAmount=balance*interest;
            System.out.println("Interest on Balance Amount is: "+interestAmount);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static boolean withdrawMoney(String accountNumber,float amountToWithdraw,String transactionType)
    {
        float value = 0;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")) {
            //first check if balance is enough or not for transaction
            PreparedStatement ps3 = con.prepareStatement("select balance from accounts where account_number=?");
            ps3.setString(1,accountNumber);
            ResultSet rs = ps3.executeQuery();
            while (rs.next()) {
                value = rs.getInt("balance");
            }
            if (value > amountToWithdraw) {
                PreparedStatement ps = con.prepareStatement("update accounts set balance=balance-? where account_number=?");
                ps.setFloat(1, amountToWithdraw);
                ps.setString(2, accountNumber);
                int result = ps.executeUpdate();
                if (result > 0) {
                    System.out.println("Updated the account balance");
                    //updating the transaction log
                    ps = con.prepareStatement("insert into transaction_log(from_account,amount,debit,transaction_type) values(?,?,?,?)");
                    ps.setString(1, accountNumber);
                    ps.setFloat(2, amountToWithdraw);
                    ps.setFloat(3, amountToWithdraw);
                    ps.setString(4, transactionType);
                    ps.executeUpdate();
                }
                return true;
            }
            else {
                System.out.println("Insufficient funds");
                return false;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static void makePayment(String from_accountNumber,String to_accountNumber,float amount,String remarks,String transactionType) {
        float value = 0;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")) {
            //first check if balance is enough or not for transaction
            PreparedStatement ps3 = con.prepareStatement("select balance from accounts where account_number=?");
            ps3.setString(1, from_accountNumber);
            ResultSet rs = ps3.executeQuery();
            while (rs.next()) {
                value = rs.getInt("balance");
            }
            if (value > amount) {
                PreparedStatement ps = con.prepareStatement("update accounts set balance=balance-? where account_number=?");
                ps.setFloat(1, amount);
                ps.setString(2, from_accountNumber);
                int result = ps.executeUpdate();
                if (result > 0) {
                    System.out.println("Updated the account balance");
                    //updating the transaction log
                    ps = con.prepareStatement("insert into transaction_log(from_account,to_account,amount,debit,remarks,transaction_type) values(?,?,?,?,?,?)");
                    ps.setString(1, from_accountNumber);
                    ps.setString(2, to_accountNumber);
                    ps.setFloat(3, amount);
                    ps.setFloat(4, amount);
                    ps.setString(5, remarks);
                    ps.setString(6, transactionType);
                    ps.executeUpdate();
                    boolean r = checkAccountExists(to_accountNumber);
                    if (r == true) {
                        PreparedStatement ps2 = con.prepareStatement("insert into transaction_log(from_account,to_account,amount,credit,remarks,transaction_type) values(?,?,?,?,?,?)");
                        ps2.setString(1, from_accountNumber);
                        ps2.setString(2, to_accountNumber);
                        ps2.setFloat(3, amount);
                        ps2.setFloat(4, amount);
                        ps2.setString(5, remarks);
                        ps2.setString(6, transactionType);
                        ps2.executeUpdate();
                        PreparedStatement ps4= con.prepareStatement("update accounts set balance=balance+? where account_number=?");
                        ps4.setFloat(1, amount);
                        ps4.setString(2, to_accountNumber);
                        ps4.executeUpdate();
                    }
                }
            }
            else
            {
                System.out.println("amount insufficient");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Update unsuccessful");
            System.out.println(e.getMessage());

        }
    }
    public static int showPreviousTransactions(String account_number)
    {
        int i=0;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")) {
            PreparedStatement ps = con.prepareStatement("select * from transaction_log where from_account=?");
            ps.setString(1,account_number);
            ResultSet rs=ps.executeQuery();
            System.out.format("%s %15s %15s %15s %17s %15s %15s %20s %20s","TransactionID","From_Account","To_Account","Amount","Transaction_Date","Debit","Credit","Remarks","Transaction_type");
            System.out.println();
            while(rs.next())
            {
                System.out.format("%s %21s %19s %13s %17s %17s %13s %25s %15s",rs.getInt(1),rs.getString(2),rs.getString(3),rs.getFloat(4),rs.getDate(5),rs.getFloat(6),rs.getFloat(7),rs.getString(8),rs.getString(9));
                System.out.println();
                i++;
            }
        }
        catch (SQLException e)
        {
            System.out.println("No matching records found,try again");
            System.out.println(e.getMessage());
        }
        return i;
    }
    public static void depositMoney(String accountNumber,float amountToDeposit,String Type_of_transaction)
    {
        float value = 0;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123"))
        {
            PreparedStatement ps = con.prepareStatement("update accounts set balance=balance+? where account_number=?");
            ps.setFloat(1, amountToDeposit);
            ps.setString(2, accountNumber);
            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Updated the account balance,Amount deposited successfully");
                //updating the transaction log
                ps = con.prepareStatement("insert into transaction_log(from_account,amount,credit,transaction_type) values(?,?,?,?)");
                ps.setString(1, accountNumber);
                ps.setFloat(2, amountToDeposit);
                ps.setFloat(3, amountToDeposit);
                ps.setString(4, Type_of_transaction);
                ps.executeUpdate();
            }
            }
        catch(SQLException e)
        {
            showPreviousTransactions("There is some problem,unable to deposit");
            System.out.println(e.getMessage());

        }
    }
//    public static void updateSaveAccountBalanceEveryYear()
//    {
//        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")) {
//            PreparedStatement ps = con.prepareStatement("select balance from accounts ");
//            ps.setInt(1,i);
//            ResultSet rs = ps.executeQuery();
//            while(rs.next())
//            {
//                countOfAccountsSatisfying=rs.getInt("c");
//            }
//        }
//        catch (SQLException e)
//        {
//            System.out.println(e.getMessage());
//        }
//    }
    public static boolean updateSaveAccountsSatisfyingUpdateCondition()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String getCurrentYear=formatter.format(date);
        if(getCurrentYear.equalsIgnoreCase(nextYear))
        {
            i++;
        }
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDBCBANK", "root", "password@123")) {
            PreparedStatement ps = con.prepareStatement("update accounts set balance=balance+(balance*?) where balance in(select balance from(select datediff(curdate(),date_of_opening)as c,balance,account_type from accounts where account_type='save' having c=365*? or c=366*?)result2)");
            ps.setFloat(1,interest);
            ps.setInt(2,i);
            ps.setInt(3,i);
            int result= ps.executeUpdate();
            if(result>0)
            {
                System.out.println("Updates successful");
                return true;
            }
            else
            {
                System.out.println("Nothing to update,no account fulfills the criteria");
                return false;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
