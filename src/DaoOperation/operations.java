package DaoOperation;
import connection.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class operations {

    // the main motive of autocommit(true, false) rollback is data consisty
    // if we transfer amount from a account to another if there will be any issue data cosisty will be maintained
    // eg tranfer 300 in databse there will same 300 after debit and credit
    // asa nhi hoga k ek sa pasa deduct ho gai or dusra m deposit nhi aai con.rollback() krna agr koi issue aata h
    // na pasa deduct honga or na hi deposit
    static Connection con=connection.getConnection();

    public static void transferMoney(String sender_account_number , String receiver_account_number, Double amount)
    {
        try {
            // First i set autocommit as false i will do it manually if code works fine
         //   con.setAutoCommit(false);
            String withdrawQuery="update accounts set Amount=Amount-? where Acoount_number=?";
            String depositQuery="update accounts set Amount=Amount+? where Acoount_number=?";

            PreparedStatement withdrawPstm=con.prepareStatement(withdrawQuery);
            PreparedStatement depositPstm= con.prepareStatement(depositQuery);

            withdrawPstm.setDouble(1,amount);
            withdrawPstm.setString(2, sender_account_number);

            depositPstm.setDouble(1,amount);
            depositPstm.setString(2,receiver_account_number);

            // executing quries
            int rowsAffectedWithdraw=withdrawPstm.executeUpdate();
            int rowsAffectedDeposit = depositPstm.executeUpdate();

            // mtlb pasa deposit b thi wya or withdraw bhi thi wya
            if (rowsAffectedWithdraw>0 && rowsAffectedDeposit>0){

               con.commit();
               con.setAutoCommit(true);
               System.out.println("transaction successful");
            }
            else {
                con.rollback();
                con.setAutoCommit(false);
                System.out.println("transaction failed");
            }

        }catch (Exception e){
            e.printStackTrace();
        }



    }
}
