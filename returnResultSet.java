package cs304_SuperBuy.backend;
import cs304_SuperBuy.conn;

import java.sql.*;
import java.util.*;


public class returnResultSet {
    public returnResultSet() {
    }

    public static void  main(String[] args) {
        List<String> input1 = new ArrayList<String>();
        input1.add("updateMyAccount");
        input1.add("40");
        input1.add("a");
        input1.add("b");
        input1.add("c");
        //ArrayList<String> input2 = {"updateMyAccount","40","a","b","c"};
        doSomething(input1);
        //doSomething(input2);

    }

    // view my order by Customer
    public static List<Map<String, Object>> checkOrder(Integer cid, Integer id, Integer type) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = null;
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT orders.ooid, orders.status, item.iname,item.price,placeorder.qty "
                    + "FROM orders, item,placeorder "
                    + "WHERE placeorder.ooid = orders.ooid AND placeorder.iid = item.iid "
                    + " AND placeorder.cid = " + cid;

            if (!id.equals(null))
                query += " OR orders.ooid = "+ id +"";


            switch(type) {
                case 0: break;
                case 1: query += " AND orders.status ='in process'";
                    break;
                case 2: query += " AND orders.status = 'shipped'";
                    break;
                case 3: query += " AND orders.status = 'delivered'";
                default: System.out.println("Invalid type");
                    break;
            }
            System.out.println("Succeed to create checkOrder");
            System.out.println(query);
            rs = statement.executeQuery(query);

            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();
            while(rs.next()) {
                row = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create checkOrder");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};
        }
        return resultList;
    }


    public static ResultSet doSomething(List<String> input) {
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;
        PreparedStatement prep = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String identifier = input.get(0);


            //view auction by Customer

            //TODO: autocommit later;

            ///////////////////#########################################FOR Merchant
            ///////////////////#########################################




            if (identifier.equals("vewAuctionsForC")) {

                try {
                    String query = "SELECT * FROM auction WHERE";
                    String aid = input.get(1);
                    String bprice = input.get(2);
                    String status = input.get(3);
                    //have aid (string to int)
                    if ((aid.length()) != 0)
                        query += " aid = "+ Integer.valueOf((String) aid) + " AND";

                    else {
                        if (bprice.length() != 0)
                            query += " bidprice <= " +Float.valueOf((String) bprice) +" AND";


                        if (! status.equals("null"))
                            query += " status like '%"+ status +"%'";
                    }

                    if (query.substring(query.length() - 5) .equals("WHERE"))
                        query=query.substring(0,query.length() -5);

                    if (query.substring(query.length() - 3) .equals("AND"))
                        query=query.substring(0,query.length() -3);

                    System.out.println(query);
                    rs = statement.executeQuery(query);
                }
                catch (Exception e) {
                    System.out.println("Fail to create statement1");
                }
            }

            if (identifier.equals("viewMyAccountforC")) {
                try {
                    int id =Integer.valueOf((String) input.get(1));
                    String query = "SELECT cname, phone, address "
                            + "FROM customer "
                            + "WHERE cid = " + id;
                    rs = statement.executeQuery(query);
                }
                catch (Exception e) {
                    System.out.println("Fail to create statement1");
                }
            }

            if (identifier.equals("viewMyAccountforM")) {
                try {
                    int id =Integer.valueOf((String) input.get(1));
                    String query = "SELECT mname, address "
                            + "FROM merchant "
                            + "WHERE mid = " + id;
                    rs = statement.executeQuery(query);
                }
                catch (Exception e) {
                    System.out.println("Fail to create statement1");
                }
            }


            if (identifier.equals("updateMyAccountforM")) {
                try {
                    int id = Integer.valueOf((String) input.get(1));
                    String name = input.get(2);
                    String addr = input.get(3);

                    String query = "";

                    if (name.length() != 0) {
                        if (addr.length() != 0) {
                            query = "UPDATE merchant SET mname='"+ name +"', address = '"+ addr +"'"
                                    + " WHERE mid = " + id;
                        }
                        else query = "UPDATE merchant SET mname='"+ name +"'"
                                + " WHERE mid = " + id;
                    }

                    else if (addr.length() !=0) {
                        query = "UPDATE merchant SET address='"+ addr +"'"
                                + " WHERE mid = " + id;
                    }


                    statement.executeUpdate(query);


                    query = "SELECT mname,address FROM merchant "
                            + " WHERE mid = "+ id;

                    rs = statement.executeQuery(query);
                }
                catch (Exception e) {
                    System.out.println("Fail to create  statement");
                }
            }








            if (identifier.equals("updateMyAccountforC")) {
                try {
                    int id = Integer.valueOf((String) input.get(1));
                    String name = input.get(2);
                    String phone = input.get(3);
                    String address = input.get(4);
                    String query = "";

                    if (!name.equals("")) {
                        query = "UPDATE customer SET cname='"+ name +"' WHERE cid=" + id;
                        statement.executeUpdate(query);
                    }


                    if (!phone.equals("")) {
                        query = "UPDATE customer SET phone='"+ phone +"' WHERE cid=" + id;
                        statement.executeUpdate(query);
                    }


                    if (!address.equals("")) {
                        query = "UPDATE customer SET address='"+ address +"' WHERE cid=" + id;
                        statement.executeUpdate(query);
                    }


                    query = "SELECT cname,phone,address FROM customer "
                            + " WHERE cid = "+ id;

                    rs = statement.executeQuery(query);
                }
                catch (Exception e) {
                    System.out.println("Fail to create  statement");
                }
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create statement");
        }

        return rs;
    }

    public static ResultSet searchMerchantforM(String id, List<String> type,
                                           String name, List<String> rat, String itemname,String price, Integer sort) {

        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT item.iid, item.price, item.iname, item.numinstock, merchant.mid FROM merchant,item WHERE "
                    +" merchant.mid = item.mid AND";

            if (id.length() != 0) {
                query += " merchant.mid = "+ id +" AND";
            }


            String temp="";
            while (type.size() != 0) {
                String t = type.get(0);
                //TODO: = or .equal;

                temp += " merchant.mtype = '"+ t +"' OR";
                type.remove(0);
            }

            if (temp.length() != 0) {
                temp = temp.substring(0, temp.length() - 2);
                //TODO: potential erro
                query += " ("+temp+") AND";
            }

            if (name.length() != 0) {
                query = query+ " merchant.mname like '%"+ name +"%' AND" ;
            }

            temp = "";
            while (rat.size() != 0) {
                int t = Integer.valueOf((String)rat.get(0));
                temp = temp + " merchant.rating = "+ t +" OR";
                rat.remove(0);
            }

            if (temp.length() != 0) {
                temp = temp.substring(0, temp.length() - 2);
                //TODO: potential erro
                query = query+ " (" +temp +") " +" AND ";
            }

            if (itemname.length() !=0) {
                query += " item.iname like '%"+itemname+"%' AND";
            }

            if (price.length() !=0) {
                query += " item.price <= "+price+"";
            }

            if ((query.substring(query.length() - 3 )).equals("AND"))
                query = query.substring(0,query.length()-3);

            if ((query.substring(query.length() - 5 )).equals("WHERE"))
                query = query.substring(0,query.length()-5);


            if (sort == 2)
                query += " ORDER BY item.price desc";
            if (sort == 3)
                query += " ORDER BY item.price ";
            if (sort == 4)
                query += " ORDER BY merchant.rating desc";
            if (sort == 5)
                query += " ORDER BY merchant.rating";
            if (sort == 6)
                query += " ORDER BY merchant.sales desc";
            if (sort == 7)
                query += " ORDER BY merchant.sales ";



            System.out.println(query);
            rs = statement.executeQuery(query);

            while (rs.next()) {

            }
        }
        catch(Exception e){
            System.out.println("Fail to create searchMerchant");
        }
        System.out.println("Success to create searchMearchant");
        return rs;
    }

    public static ResultSet searchMerchant(String id, List<String> type,
                                           String name, List<String> rat, Integer sort) {

        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT * FROM merchant WHERE";

            if (id.length() != 0) {
                query += " mid = "+ id +" AND";
            }


            String temp="";
            while (type.size() != 0) {
                String t = type.get(0);
                //TODO: = or .equal;

                temp += " mtype = '"+ t +"' OR";
                type.remove(0);
            }

            if (temp.length() != 0) {
                temp = temp.substring(0, temp.length() - 2);
                //TODO: potential erro
                query += " ("+temp+") AND";
            }

            if (name.length() != 0) {
                query = query+ " mname like '%"+ name +"%' AND" ;
            }

            temp = "";
            while (rat.size() != 0) {
                int t = Integer.valueOf((String) rat.get(0));
                temp = temp + " rating = "+ t +" OR";
                rat.remove(0);
            }

            if (temp.length() != 0) {
                temp = temp.substring(0, temp.length() - 2);
                //TODO: potential erro
                query = query+ " (" +temp +") ";
            }

            if ((query.substring(query.length() - 3 )).equals("AND"))
                query = query.substring(0,query.length()-3);

            if ((query.substring(query.length() - 5 )).equals("WHERE"))
                query = query.substring(0,query.length()-5);


            if (sort == 2)
                query += " ORDER BY mname desc";
            if (sort == 3)
                query += " ORDER BY mname";
            if (sort == 4)
                query += " ORDER BY rating desc";
            if (sort == 5)
                query += " ORDER BY rating";
            if (sort == 6)
                query += " ORDER BY sales desc";
            if (sort == 7)
                query += " ORDER BY sales ";

            System.out.println(query);
            rs = statement.executeQuery(query);

        }
        catch(Exception e){
            System.out.println("Fail to create searchMerchant");
        }
        System.out.println("Success to create searchMearchant");
        return rs;
    }



    public static  List<List<Object>> viewItem(String mid, String id, Integer sign, String qty) {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        String query ="";
        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            Integer t = Integer.valueOf((String) mid);
            query = "SELECT iid, price,numinstock,iname "
                    + "FROM item "
                    + "WHERE mid = " + t;


            if (id.length() !=0) {
                t = Integer.valueOf((String) id);
                System.out.println("in");
                query += " AND iid = " + t;
                System.out.println("dfsdfsfds");
            }

            if (qty.length() !=0) {
                t = Integer.valueOf((String) qty);
                System.out.println("wwww!");
                if (sign == 1)
                    query += " AND item.numinstock =" +t;
                if (sign == 2)
                    query += " AND item.numinstock >=" + t;
                else
                    query += " AND item.numinstock <=" + t;
            }

            System.out.println(query);
            rs = statement.executeQuery(query);
            System.out.println(1);

            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();
            System.out.println("1");

            while(rs.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create orderOverview");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        return resultList;
    }


    ///FOR MANAGER
    public static List<List<Object>>  merchantStats() {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();    ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT mtype, count(*) as quantity FROM merchant GROUP BY mtype";

            System.out.println(query);
            rs = statement.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create merchantStats");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        return resultList;
    }



    public static List<List<Object>> orderStats() {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT sum(item.price) as total, orders.ooid, customer.cname "
                    + "FROM customer, orders,placeorder,item "
                    + "WHERE placeorder.ooid = orders.ooid AND placeorder.cid = customer.cid "
                    + "AND placeorder.iid = item.iid "
                    + "GROUP BY orders.ooid,customer.cname "
                    + "ORDER BY total" ;

            System.out.println(query);
            rs = statement.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create orderOverview");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        return resultList;
    }

    public static List<List<Object>>  customerOverview() {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT * FROM customer";

            System.out.println(query);
            rs = statement.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create customerOverview");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        return resultList;
    }



    public static List<List<Object>> orderOverview(String name) {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT merchant.mid, merchant.mname, item.iid, item.price, item.numinstock "
                    + "FROM merchant, item "
                    + "WHERE merchant.mid = item.mid "
                    + "AND merchant.mname like '%"+ name +"%'";

            System.out.println(query);
            rs = statement.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create orderOverview");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        return resultList;
    }

    public static List<List<Object>>  customerStats() {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            //TODO:query;
            String query = "SELECT customer.cid,customer.cname,sum(item.price) "
                    + "FROM customer, item, placeorder "
                    + "WHERE customer.cid = placeorder.cid AND placeorder.iid = item.iid "
                    + "GROUP BY customer.cid";

            System.out.println(query);
            rs = statement.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create customerStats");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        return resultList;
    }

    public static List<List<Object>> auctionStats() {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT auction.aid,merchant.mname,auction.bidprice,customer.cname,item.iname "
                    + "FROM customer, auction,bid,item,merchant "
                    + "WHERE auction.aid = bid.aid AND auction.mid = merchant.mid "
                    + "AND auction.iid = item.iid AND bid.cid = customer.cid ";

            System.out.println(query);
            rs = statement.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create orderOverview");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        return resultList;
    }

    public static List<List<Object>> searchCustomer(String name) {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT * "
                    + "FROM customer "
                    + "WHERE cname like '%"+ name +"%'";

            System.out.println(query);
            rs = statement.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create orderOverview");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        return resultList;
    }

    public static List<List<Object>> searchOrder(Integer id) {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT orders.ooid, customer.cname,orders.status "
                    + "FROM customer, orders,placeorder "
                    + "WHERE placeorder.ooid = orders.ooid AND placeorder.cid = customer.cid "
                    + "AND orders.ooid =" + id;

            System.out.println(query);
            rs = statement.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create orderOverview");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        return resultList;
    }


    public static List<List<Object>> searchByOrderStatus(String status) {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT orders.ooid, customer.cname,customer.address "
                    + "FROM customer, orders,placeorder "
                    + "WHERE placeorder.ooid = orders.ooid AND placeorder.cid = customer.cid "
                    + "AND orders.status like '%"+ status +"%'";

            rs = statement.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            System.out.println(query);
            while(rs.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create orderOverview");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        System.out.println(resultList);
        return resultList;
    }

    public static List<List<Object>> showAllOrders() {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT orders.ooid, customer.cname,orders.status "
                    + "FROM customer, orders,placeorder "
                    + "WHERE placeorder.ooid = orders.ooid AND placeorder.cid = customer.cid";

            System.out.println(query);
            rs = statement.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create orderOverview");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};
        }
        return resultList;
    }

    public static List<List<Object>> checkOrderforM(Integer cid, Integer mid, String id, Integer type) {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            //assume one of cid and mid must be null

            String query = "";
            if (cid !=0) {
                query = "SELECT orders.ooid, orders.status, item.iname,item.price "
                        + "FROM orders, item,placeorder "
                        + "WHERE placeorder.ooid = orders.ooid AND placeorder.iid = item.iid "
                        + " AND placeorder.cid = " + cid;
            }

            if (mid !=0) {
                query ="SELECT orders.ooid ,orders.status, item.iid,item. iname "
                        + "FROM item,orders,placeorder "
                        + "WHERE item.mid = "+ mid +" AND item.iid=placeorder.iid "
                        + "AND placeorder.ooid=orders.ooid ";
            }

            if (id.length() !=0)
                query += " AND orders.ooid = "+ id +"";

            switch(type) {
                case 1: query += " AND orders.status ='Delivered'";
                    break;
                case 2: query += " AND orders.status ='InProcess'";
                    break;
                case 3: query += " AND orders.status = 'Shipped'";
                    break;
                default:
                    break;
            }
            System.out.println("Succeed to create checkOrder");
            rs = statement.executeQuery(query);

            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                //row = new HashMap<String, Object>();

                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create checkOrder");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};
        }
        System.out.println(resultList);
        return resultList;
    }

    public static List<List<Object>> checkOrder(Integer cid, Integer mid, String id, Integer type) {
    List <List<Object>>  resultList = new ArrayList<List<Object>> ();
    ResultSet rs = null;
    Connection connect = null;
    Statement statement = null;

    try {
      conn c = new conn();
      connect = c.createConnection();
      statement = connect.createStatement();

      //assume one of cid and mid must be null

      String query = "";
      if (cid !=0) {
         query = "SELECT orders.ooid, orders.status, item.iname, item.price "
              + "FROM orders, item, placeorder "
              + "WHERE placeorder.ooid = orders.ooid AND placeorder.iid = item.iid "
              + " AND placeorder.cid = " + cid;
      }

      if (mid !=0) {
          query ="SELECT orders.ooid,orders.status,item.iid,item.iname "
              + "FROM item,orders,placeorder "
              + "WHERE item.iid = placeorder.iid "
              + "AND orders.ooid = placeorder.ooid "
              + "AND item.mid = " +mid;
      }

      if (id.length() !=0)
        query += " AND orders.ooid = "+ id +"";

      switch(type) {
        case 1: query += " AND orders.status = 'Delivered'";
                break;
        case 2: query += " AND orders.status = 'Shipped'";
                break;
        case 3: query += " AND orders.status = 'InProcess'";
                break;
        default:
                break;
      }
      System.out.println(query);
      rs = statement.executeQuery(query);

      System.out.println("Succeed to create checkOrder");

      ResultSetMetaData metaData = rs.getMetaData();
      Integer columnCount = metaData.getColumnCount();

      while(rs.next()) {
        //row = new HashMap<String, Object>();

        List<Object> row = new ArrayList<Object>();
        for (int i = columnCount; i >= 1; i--) {
            row.add(rs.getObject(i));
        }
        Collections.reverse(row);
        resultList.add(row);
      }
    }
    catch (Exception e) {
      System.out.println("Fail to create checkOrder");
    }
    finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};
        }
    System.out.println(resultList);
    return resultList;
  }

    public static  List<List<Object>> viewAllAuction (String merchantid,String auctionid, String auction_status) {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT * "
                    + "FROM auction WHERE";

            List<String> temp = new ArrayList<String> ();

            if (merchantid.length() != 0) {
                temp.add(" mid = " + Integer.valueOf(merchantid));
            }
            if (auctionid.length() != 0) {
                temp.add(" aid = " + Integer.valueOf(auctionid));
            }
            if (auction_status.length() !=0) {
                temp.add(" status = '"+auction_status+"'");
            }

            for (int i =0; i<temp.size(); i++) {
                query += temp.get(i) + " AND";
            }

            if (query.substring(query.length() - 5) .equals("WHERE"))
                query=query.substring(0,query.length() -5);

            if (query.substring(query.length() - 3) .equals("AND"))
                query=query.substring(0,query.length() -3);

            System.out.println(query);
            rs = statement.executeQuery(query);
            System.out.println(query);

            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create orderOverview");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        return resultList;
    }

    public static  List<List<Object>> updatePrice(String id,String newPrice) {
    List <List<Object>>  resultList = new ArrayList<List<Object>> ();
    ResultSet rs = null;
    Connection connect = null;
    Statement statement = null;

    try {
      conn c = new conn();
      connect = c.createConnection();
      statement = connect.createStatement();
        String query = "UPDATE item SET price = "+ Integer.valueOf(newPrice) +""
                      +" WHERE iid=" + Integer.valueOf(id);

                      System.out.println(query);

        statement.executeUpdate(query);

        query = "SELECT iid, price,numinstock,iname "
              + " FROM item"
              + " WHERE iid = "+ id;

        System.out.println(query);

        rs = statement.executeQuery(query);

        ResultSetMetaData metaData = rs.getMetaData();
        Integer columnCount = metaData.getColumnCount();

        while(rs.next()) {
          List<Object> row = new ArrayList<Object>();
          for (int i = columnCount; i >= 1; i--) {
              row.add(rs.getObject(i));
          }
          Collections.reverse(row);
          resultList.add(row);
        }
    }
    catch (Exception e) {
      System.out.println("Fail to create orderOverview");
    }
    finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        System.out.println(resultList);
    return resultList;
  }

    public static Integer addNewAuction(String mid, String iid, String askprice) {

        System.out.println("1255");
        System.out.println(mid);
        System.out.println(iid);
        System.out.println(askprice);
    List <List<Object>>  resultList = new ArrayList<List<Object>> ();
    ResultSet rs = null;
    int num =0;
    Connection connect = null;
    Statement statement = null;

    try {
      conn c = new conn();
      connect = c.createConnection();
      statement = connect.createStatement();

      Float floataskprice = Float.valueOf(askprice);
      Float bidprice = floataskprice;
      String status = "InProcess";

      Integer merchantid = Integer.valueOf(mid);
      Integer itemid = Integer.valueOf(iid);

      Calendar calendar = Calendar.getInstance();
      java.sql.Date sqlDate = new java.sql.Date(calendar.getTime().getTime());
      String query =  "insert into auction values (null , "+ merchantid +","+ itemid +""
                + " ,'"+sqlDate+"',"+floataskprice+","+bidprice+",'"+status+"')";

        System.out.println(query);
      statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);

      System.out.println(".........");
      System.out.println(query);

      rs = statement.getGeneratedKeys();

      while(rs.next()) {
         num = rs.getInt(1);
      }
    }
    catch (Exception e) {
      System.out.println("Fail to create orderOverview");
    }
    finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
    return num;
  }

    public static boolean bid(String aid, String bidprice){
    ResultSet rs = null;
    boolean val =false;
    Connection connect = null;
    Statement statement = null;

    try {
      conn c = new conn();
      connect = c.createConnection();
      statement = connect.createStatement();

      Integer aaid = Integer.valueOf(aid);
      Float bbidprice = Float.valueOf(bidprice);



        String query = "UPDATE auction SET bidprice =" +bbidprice +"where aid = " + aaid + " AND bidprice <=" + bbidprice;
        statement.executeUpdate(query);
        val= true;
    }
    catch (Exception e) {
      System.out.println("Fail to create orderOverview");
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


    public static List<List<Object>> checkOrderforC(Integer cid, Integer mid, String id, Integer type) {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            //assume one of cid and mid must be null

            String query = "";
            if (cid !=0) {
                query = "SELECT orders.ooid, orders.status, item.iname,item.price "
                        + "FROM orders, item,placeorder "
                        + "WHERE placeorder.ooid = orders.ooid AND placeorder.iid = item.iid "
                        + " AND placeorder.cid = " + cid;
            }

            if (mid !=0) {
                query ="SELECT orders,ooid ,orders.status, item.iid, "
                        + "FROM item,customer,orders,placeorder "
                        + "WHERE item.mid = "+ mid +" AND item.iid=placeorder.iid "
                        + "AND placeorder.cid=customer.cid ";
            }

            if (id.length() !=0)
                query += " AND orders.ooid = "+ id +"";

            switch(type) {
                case 1: break;
                case 2: query += " AND orders.status ='process'";
                    break;
                case 3: query += " AND orders.status = 'shipped'";
                    break;
                default: System.out.println("Invalid type");
                    break;
            }
            System.out.println("Succeed to create checkOrder");
            rs = statement.executeQuery(query);

            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                //row = new HashMap<String, Object>();

                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create checkOrder");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};
        }
        System.out.println(resultList);
        return resultList;
    }


    public static String EndAnAuction(String aid){
        Connection connect = null;
        Statement statement = null;
        ResultSet rs = null;

        String val = "";

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();


            Integer aaid = Integer.valueOf(aid);


            String query = "SELECT max(bid.price), customer.cname"
                    + " FROM bid,customer WHERE bid.aid =" +aaid +" AND bid.cid = customer.cid"
                    + " GROUP BY customer.cname";

            rs = statement.executeQuery(query);
            System.out.println(query);

            if (rs.next()) {
                System.out.println(1);
                val = (rs.getObject(2)).toString();
                System.out.println(1);
            }

            query = "UPDATE auction SET auction.status = 'End' WHERE auction.aid = " + aaid;

            System.out.println(query);

            statement.executeUpdate(query);

            System.out.println(query);

            System.out.println("Succeed to end an acution");
        }
        catch (Exception e) {
            System.out.println("Fail to end an auction");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};
        }
        System.out.println(val);
        return val;
    }

    public static Integer placeOrder(String cid, List<String> inames) {
        ResultSet rs = null;
        int num =0;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            Integer ccid = Integer.valueOf(cid);

            // First generate a new oid,defalut status = InProcess.
            // I will set every newly inserted order status as InProcess here.
            // But we can also achieve this by setting default value in mysql.
            String query = "INSERT INTO orders VALUES(null, 'InProcess')";

            statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);

            rs = statement.getGeneratedKeys();
            System.out.println("Succeed to insert orders");

            // num is the ooid to be returned
            if(rs.next()) {
                num = rs.getInt(1);
                System.out.println(1);

            }

            for (int i = 0; i < inames.size() ; i++) {
                //insert into placeorder
                String itemn = inames.get(i);
                query = "INSERT INTO placeorder "
                        + " SELECT "+num+",iid,"+ccid+",1"
                        + " FROM item WHERE iname ='"+itemn+"'";
                System.out.println(query);
                statement.executeUpdate(query);
                System.out.println("Succeed to insert into placeorder");

                //update item,assume quantity =1 [^..^]
                query = "UPDATE item SET numinstock = numinstock - 1  WHERE iname ='"+itemn+"'";
                System.out.println(query);
                statement.executeUpdate(query);
                System.out.println("Succeed to update item");


                //update merchant.sales,assume quantity =1 [^..^]
                query = "UPDATE merchant JOIN item ON item.mid = merchant.mid"
                        + " SET merchant.sales =merchant.sales + 1"
                        + " WHERE item.iname ='"+itemn+"'";
                System.out.println(query);
                statement.executeUpdate(query);
                System.out.println("Succeed to update merchant");
            }
        }
        catch (Exception e) {
            System.out.println("Fail to create orderOverview");
        }
        finally {
            try { if (rs != null) rs.close(); }
            catch (Exception e) {System.out.println(1);};
            try { if (statement != null) statement.close(); }
            catch (Exception e) {};
            try { if (connect != null) connect.close(); }
            catch (Exception e) {};

        }
        System.out.println(num);

        return num;

    }

    public static List<List<Object>> searchMerchant2(String id, List<String> type,
                                                     String name, List<String> rat, String itemname,String price, Integer sort) {
        List <List<Object>>  resultList = new ArrayList<List<Object>> ();
        ResultSet rs = null;
        Connection connect = null;
        Statement statement = null;

        try {
            conn c = new conn();
            connect = c.createConnection();
            statement = connect.createStatement();

            String query = "SELECT merchant.mname,merchant.rating,merchant.mtype, "
                    +" item.iname,item.price,item.numinstock FROM merchant,item WHERE "
                    +" merchant.mid = item.mid AND";

            if (id.length() != 0) {
                query += " merchantmid = "+ id +" AND";
            }


            String temp="";
            while (type.size() != 0) {
                String t = type.get(0);
                //TODO: = or .equal;

                temp += " merchant.mtype = '"+ t +"' OR";
                type.remove(0);
            }

            if (temp.length() != 0) {
                temp = temp.substring(0, temp.length() - 2);
                //TODO: potential erro
                query += " ("+temp+") AND";
            }

            if (name.length() != 0) {
                query = query+ " merchant.mname like '%"+ name +"%' AND" ;
            }

            temp = "";
            while (rat.size() != 0) {
                int t = Integer.valueOf((String)rat.get(0));
                temp = temp + " merchant.rating = "+ t +" OR";
                rat.remove(0);
            }

            if (temp.length() != 0) {
                temp = temp.substring(0, temp.length() - 2);
                //TODO: potential erro
                query = query+ " (" +temp +") " +" AND";
            }

            if (itemname.length() !=0) {
                query += " item.iname like '%"+itemname+"%' AND";
            }

            System.out.println(price);

            if (price.length() !=0) {
                query += " item.price <= "+price+"";
            }

            if ((query.substring(query.length() - 3 )).equals("AND"))
                query = query.substring(0,query.length()-3);

            if ((query.substring(query.length() - 5 )).equals("WHERE"))
                query = query.substring(0,query.length()-5);


            if (sort == 2)
                query += " ORDER BY item.price desc";
            if (sort == 3)
                query += " ORDER BY item.price ";
            if (sort == 4)
                query += " ORDER BY merchant.rating desc";
            if (sort == 5)
                query += " ORDER BY merchant.rating";
            if (sort == 6)
                query += " ORDER BY merchant.sales desc";
            if (sort == 7)
                query += " ORDER BY merchant.sales ";



            System.out.println(query);
            rs = statement.executeQuery(query);

            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while(rs.next()) {
                //row = new HashMap<String, Object>();

                List<Object> row = new ArrayList<Object>();
                for (int i = columnCount; i >= 1; i--) {
                    row.add(rs.getObject(i));
                }
                Collections.reverse(row);
                resultList.add(row);
            }
        }
        catch(Exception e){
            System.out.println("Fail to create searchMerchant");
        }
        System.out.println("Success to create searchMearchant");
        return resultList;
    }



}
