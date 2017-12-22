package cs304_SuperBuy.backend;
import java.sql.*;
import java.util.List;
import cs304_SuperBuy.conn;

public class returnBoolean {

    public returnBoolean() {
    }


    public static boolean doSomething(List<String> input) {

        boolean val = false;
        conn c = new conn();
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connect = c.createConnection();
            statement = connect.createStatement();

            String identifier = input.get(0);

            if (identifier.equals("customerLogInSubmit")) {
                try {
                    //todo current customer table has no password field
                    int id =Integer.valueOf((String) input.get(1));
                    String password = input.get(2);
                    String query = "select cpassword from customer" + " WHERE cid = " + id;

                    resultSet = statement.executeQuery(query);

                    try {
                        if (! (resultSet.next())) {
                            System.out.println("Wrong ID");
                            }

                        else if (resultSet.getString("cpassword").equals(password)) {
                            System.out.println("Login Successfully");
                            val = true;}

                        else {
                            System.out.println("Wrong password");
                        }
                    }
                    catch(Exception e) {}
                }
                catch (Exception e) {
                    System.out.println("Fail to create statement");
                }
            }

            if (identifier.equals("merchantLogInSubmit")) {
                try {
                    int id = Integer.valueOf((String) input.get(1));
                    String password = input.get(2);
                    String query = "select mpassword from merchant" + " where mid = " +id;
                    resultSet = statement.executeQuery(query);

                    try {
                        if (!(resultSet.next())) {System.out.println("Wrong ID");}
                        else if (resultSet.getString("mpassword").equals(password)) {
                            System.out.println("Login Successfully");
                            val = true;
                        }
                        else {
                            System.out.println("Wrong password");
                        }
                    }
                    catch (Exception e) {}
                }
                catch (Exception e) {
                    System.out.println("Fail to create statement");
                }
            }

            if (identifier.equals("managerLogInSubmit")) {
                try {
                    int id = Integer.valueOf((String) input.get(1));
                    String password = input.get(2);
                    String query = "select mapassword from manager" + " where id = " +id;
                    resultSet = statement.executeQuery(query);

                    try {
                        if (!(resultSet.next())) {System.out.println("Wrong ID");}
                        else if (resultSet.getString("mapassword").equals(password)) {
                            System.out.println("Login Successfully");
                            val = true;
                        }
                        else {
                            System.out.println("Wrong password");
                            }
                        }
                    catch (Exception e) {}
                }
                catch (Exception e) {
                    System.out.println("Fail to create statement");
                }
            }


            //insert cid and password only;
            if (identifier.equals("addNewCustomer")) {
                try {
                    int id = Integer.valueOf((String) input.get(1));
                    String password = input.get(2);
                    String query = "insert into customer(cid,cpassword)"
                               + " values("+ id +",'" + password + "')";

                               System.out.println(query);
                    statement.executeUpdate(query);
                    val = true;
                }
                catch (Exception e) {
                    System.out.println("Fail to insert new customer");
                }
            }

            //TODO : check merchant type name!
            //input does not include rating and sales,set them to default value
            if (identifier.equals("addNewMerchant")) {
                try {
                    int id = Integer.valueOf((String) input.get(1));
                    String password = input.get(2);

                    String query = "insert into merchant(mid,mpassword)"
                            + " values("+ id +",'" + password + "')";

                    System.out.println(query);

                    statement.executeUpdate(query);
                    val = true;
                }
                catch (Exception e) {
                    System.out.println("Fail to insert new merchant");
                }
            }

            if (identifier.equals("updateOrderStatus")) {
                try {
                    int id = Integer.valueOf((String) input.get(1));
                    String new_status = input.get(2);

                    String query = "UPDATE orders SET status = '"+new_status+"'"
                                + " WHERE ooid=" +id;

                    statement.executeUpdate(query);
                    val = true;
                }
                catch (Exception e) {
                    System.out.println("Fail to update");
                }

            }

            if (identifier.equals("deleteAuction")) {
                System.out.println("159");
                try {
                    int id = Integer.valueOf((String) input.get(1));

                    String query = "DELETE FROM auction "
                                + " WHERE aid = " +id;

                    System.out.println("166");

                    statement.executeUpdate(query);

                    System.out.println(query);
                    System.out.println("168");
                    val = true;
                }
                catch (Exception e) {
                    System.out.println("Fail to update");
                }

            }

              if (identifier.equals("stockmyitem")) {
               try {
                   System.out.println(",.......");
                   int id = Integer.valueOf((String) input.get(1));  //itemid id
                   System.out.println(id);
                   String newqty= input.get(2); //string new qty value

                   int newqtyvalue = Integer.valueOf((String) newqty);
                   System.out.println(id);
                   System.out.println(newqtyvalue);


                   String query = "UPDATE item SET numinstock = numinstock +" +newqtyvalue + " where iid = " +id;


                   System.out.println(query);
                   statement.executeUpdate(query);
                   val = true;


               }
               catch (Exception e) {
                   System.out.println("Fail to create statement");
               }
           }

        }
        catch (Exception e) {
            System.out.println("Fail to create statement");
        }
        finally {
            try { if (resultSet != null) resultSet.close(); }
            catch (Exception e) {};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};
            System.out.println(connect != null);
        }
    return val;
    }

    public static boolean bid(String cid, String aid, String bidprice){
        ResultSet rs = null;
        boolean val = false;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            Integer aaid = Integer.valueOf(aid);
            Float bbidprice = Float.valueOf(bidprice);
            Integer ccid = Integer.valueOf(cid);

            String query = "SELECT * FROM auction WHERE aid =" + aaid
                    + " AND status ='InProcess' AND bidprice < " + bbidprice;

            rs = statement.executeQuery(query);
            System.out.println(query);
            if (rs.next()) {
                val = true;
                query = "INSERT into bid "
                        + "VALUES (null , "+ccid+","+aaid+","+bbidprice+")";
                statement.executeUpdate(query);
                System.out.println("Succeed to bid");

                query = "UPDATE auction SET bidprice =" + bbidprice + "WHERE aid =" +aaid;
                statement.executeUpdate(query);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to bid query");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        return val;
    }
}