package util;

import model.Customer;
import model.Order;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by JammersBlah on 17/07/2017.
 */
public class DbUtil
{
    private Connection connection;

    private final String[] tableNames = {"USERS", "ORDERS", "PRODUCTS", "CUSTOMERS", "PAYMENTS", "ORDR_PROD_LINK", "PAYM_ORDR_LINK", "CUST_ORDR_LINK"};

    private static final String DB_BUILD_SCHEMA = "database_build_schema";

    public DbUtil() throws Exception
    {
        // Get properties+ from database.properties file
        Properties properties = new Properties();
        InputStream in = ClassLoader.getSystemResourceAsStream("database.properties");

//        InputStream in = getClass().getResourceAsStream("database.properties");
        properties.load(in);
        in.close();

        String driver = properties.getProperty("db.maindb.driver");
        String url = properties.getProperty("db.maindb.url");
        String username = properties.getProperty("db.maindb.username");
        String password = properties.getProperty("db.maindb.password");

        Class.forName(driver).newInstance();

        connection = DriverManager.getConnection(url, username, password);
    }

    /*
     * Reads a SQL file from the resources folder
     */
    private List<String> readSqlFile(String pathToFile)
    {
        Path path = null;
        List<String> lines = null;
        try
        {
            path = Paths.get(getClass().getClassLoader().getResource(pathToFile).toURI());
            lines = Files.readAllLines(path, Charset.defaultCharset());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    /*
     * Builds the database using the DB_BUILD_SCHEMA stored in resources
     */
    public void createTable()
    {
        //TODO Check data types

        //Get the db build sql
        List<String> buildSql = readSqlFile(DB_BUILD_SCHEMA);

        // Run each line to build the DB
        for (String sql : buildSql)
        {
            try
            {
                Statement statement = connection.createStatement();

                statement.execute(sql);
            } catch (SQLException e) {
                System.err.println("Something went wrong in the db build" + e.getMessage());
            }
        }
        System.out.println("Database build complete");
    }

    public void deleteAll(String table)
    {
        if (Arrays.asList(tableNames).contains(table))
        {
            StringBuilder query = new StringBuilder();
            query.append("DROP TABLE IF EXISTS ").append(table);
            try
            {
                PreparedStatement deleteAll = connection.prepareStatement(query.toString());
                deleteAll.executeQuery();
            } catch (SQLException e) {
                System.err.println("Delete statement failed" + e.getMessage());
            }
        }
    }

    public void insertCustomer(Customer customer)
    {
        //StringBuilder statement = new StringBuilder();
        //statement.append("INSERT INTO CUSTOMER()");

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO CUSTOMERS (company, contact, delivery_address, postcode, email, phone, source) VALUES ('"
                    + customer.getCompany() + "', '"
                    + customer.getContact() + "', '"
                    + customer.getDeliveryAddress() + "', '"
                    + customer.getPostcode() + "', '"
                    + customer.getEmail() + "', '"
                    + customer.getPhoneNumber() + "', '"
                    + customer.getSource() + "')");
            statement.executeUpdate();
            //connection.commit();
        } catch (SQLException e) {
            System.err.println("Delete statement failed" + e.getMessage());
        }

    }

    public ArrayList<Customer> getCustomers()
    {
        Customer customer;
        ArrayList<Customer> customers = new ArrayList<>();
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM customers");
            ResultSet rs = statement.executeQuery();

            while (rs.next())
            {
                int customerId = rs.getInt("customer_id");
                String companyName = rs.getString("company");
                String contactName = rs.getString("contact");
                String deliveryAddress = rs.getString("delivery_address");
                String postcode = rs.getString("postcode");
                String email = rs.getString("email");
                String phoneNo = rs.getString("phone");
                String source = rs.getString("source");

                customer = new Customer(customerId, companyName, contactName, deliveryAddress, postcode, email, phoneNo, source);

                customers.add(customer);
            }

        } catch (SQLException e) {
            System.out.println("Get customers statement failed" + e.getMessage());
        }

        return customers;
    }

    public Customer getCustomerById(int id)
    {
        Customer customer = null;
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM customers WHERE customer_id = " + id);
            ResultSet rs = statement.executeQuery();
            int customerId = rs.getInt("customer_id");
            String companyName = rs.getString("company");
            String contactName = rs.getString("contact");
            String deliveryAddress = rs.getString("delivery_address");
            String postcode = rs.getString("postcode");
            String email = rs.getString("email");
            String phoneNo = rs.getString("phone");
            String source = rs.getString("source");
            customer = new Customer(customerId, companyName, contactName, deliveryAddress, postcode, email, phoneNo, source);


        } catch (SQLException e) {
            System.out.println("Get Customer by ID statement failed" + e.getMessage());
        }
        return customer;
    }

    public ArrayList<Order> getOrdersByCustomerId(int customerId)
    {
        Order order;
        ArrayList<Order> orders = new ArrayList<>();
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM orders WHERE order_id IN (SELECT order_id FROM cust_ordr_link WHERE customer_id = " + customerId + ")");
            ResultSet rs = statement.executeQuery();

            while (rs.next())
            {
                int orderId = rs.getInt("order_id");
                String orderDate = rs.getString("order_date").toString();
                String expectedDeliveryDate = rs.getString("expected_delivery_date").toString();

                order = new Order(orderId , orderDate, expectedDeliveryDate);
                orders.add(order);

            }

        } catch (SQLException e) {
            System.out.println("Get Orders by Customer ID statement failed: " + e.getMessage());
        }

        return orders;
    }
}
