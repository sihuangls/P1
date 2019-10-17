
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLiteDataAdapter implements IDataAdapter  {


    Connection conn = null;

    public int connect(String dbfile) {
        try {
            // db parameters
//        	System.lineSeparator();
//        	System.out.println(a + File.separator + b);
//        	E:\final\3700\sql
            String url = "jdbc:sqlite:" + dbfile;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_OPEN_FAILED;
        }
        return CONNECTION_OPEN_OK;
    }

   
    @Override
    public int disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_CLOSE_FAILED;
        }
        return CONNECTION_CLOSE_OK;
    }
    
    
    public ProductModel loadProduct(int productID) {
        ProductModel product = null;

        try {
            String sql = "SELECT ProductID, Barcode, Name, Expiration_Date, Price, Tax_Rate, Quantity, Supplier, "
            		+ "Manufactured_Date FROM Products WHERE ProductID = " + productID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                product = new ProductModel();
            	product.mProductID = rs.getInt("ProductID");
            	product.mBarcode = rs.getString("Barcode");          
            	product.mName = rs.getString("Name");
            	product.mExpiration_Date = rs.getString("Expiration_Date");
            	product.mPrice = rs.getDouble("Price");
            	product.mTax_Rate = rs.getDouble("Tax_Rate");
            	product.mQuantity = rs.getDouble("Quantity");            
            	product.mSupplier = rs.getString("Supplier");
            	product.mManufactured_Date = rs.getString("Manufactured_Date");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return product;
    }
   
    public int saveProduct(ProductModel product) {
        try {
            String sql = "INSERT INTO Products(ProductID, Barcode, Name, Expiration_Date, Price, Tax_Rate, "
            		+ "Quantity, Supplier, Manufactured_Date) VALUES " + product;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return PRODUCT_DUPLICATE_ERROR;
        }

        return PRODUCT_SAVED_OK;
    }
    
    
    public CustomerModel loadCustomer(int customerID) {
    	CustomerModel customer = null;

        try {
            String sql = "SELECT CustomerID, Name, Address, Phone, payment_info FROM Customers WHERE CustomerID = " + customerID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                customer = new CustomerModel();
            	customer.mCustomerID = rs.getInt("CustomerID");     
            	customer.mName = rs.getString("Name");
            	customer.mAddress = rs.getString("Address");
            	customer.mPhone = rs.getDouble("Phone");
            	customer.mpayment_info = rs.getString("payment_info");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return customer;
    }
   
    public int saveCustomer(CustomerModel customer) {
        try {
            String sql = "INSERT INTO Customers(CustomerID, Name, Address, Phone, payment_info) VALUES " + customer;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return PRODUCT_DUPLICATE_ERROR;
        }

        return PRODUCT_SAVED_OK;
    }
    
    @Override
    public int savePurchase(PurchaseModel purchase) {
        try {
            String sql = "INSERT INTO Purchase(PurchaseID, CustomerID, ProductID, Price, Quantity, Cost, Tax_Rate, Total, Date) VALUES " + purchase;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return PURCHASE_DUPLICATE_ERROR;
        }

        return PURCHASE_SAVED_OK;

    }
}

