package project.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.models.Apartment;
import project.models.Material;
import project.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends Configurations {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void setUser(User user) {
        String insert = "INSERT INTO " + Constants.USER_TABLE + "(" +
                Constants.USERS_FIRSTNAME + "," + Constants.USERS_LASTNAME + "," +
                Constants.USERS_USERNAME + "," + Constants.USERS_PASSWORD + "," +
                Constants.USERS_ACCOUNT_TYPE + ")" +
                "VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, user.getFirstName());
            prSt.setString(2, user.getLastName());
            prSt.setString(3, user.getUsername());
            prSt.setString(4, user.getPassword());
            prSt.setString(5, user.getAccountType());
            prSt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getUser(User user) {
        ResultSet resultSet = null;

        String select = "SELECT * FROM " + Constants.USER_TABLE + " WHERE " +
                Constants.USERS_FIRSTNAME + "=? AND " + Constants.USERS_LASTNAME + "=? AND " +
                Constants.USERS_USERNAME + "=? AND " + Constants.USERS_PASSWORD + "=? AND " +
                Constants.USERS_ACCOUNT_TYPE + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getFirstName());
            prSt.setString(2, user.getLastName());
            prSt.setString(3, user.getUsername());
            prSt.setString(4, user.getPassword());
            prSt.setString(5, user.getAccountType());
            resultSet = prSt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getUserForLogIn(User user) {
        ResultSet resultSet = null;

        String select = "SELECT * FROM " + Constants.USER_TABLE + " WHERE " +
                Constants.USERS_USERNAME + "=? AND " + Constants.USERS_PASSWORD + "=? AND " +
                Constants.USERS_ACCOUNT_TYPE + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getUsername());
            prSt.setString(2, user.getPassword());
            prSt.setString(3, user.getAccountType());
            resultSet = prSt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public int getUsersId(User user) {
        int id = 0;
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.USERS_ID + " FROM " + Constants.USER_TABLE
                    + " WHERE " + Constants.USERS_USERNAME + " = '" + user.getUsername() +
                    "' AND " + Constants.USERS_PASSWORD + " = '" + user.getPassword() + "' AND " +
                    Constants.USERS_ACCOUNT_TYPE + " = '" + user.getAccountType() + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                id = rs.getInt(Constants.USERS_ID);
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public String getUsersName(User user) {
        String usersName = "";
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.USERS_FIRSTNAME + ", " + Constants.USERS_LASTNAME + " FROM "
                    + Constants.USER_TABLE + " WHERE " + Constants.USERS_USERNAME + " = '" + user.getUsername() +
                    "' AND " + Constants.USERS_PASSWORD + " = '" + user.getPassword() + "' AND " +
                    Constants.USERS_ACCOUNT_TYPE + " = '" + user.getAccountType() + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String firstName = rs.getString(Constants.USERS_FIRSTNAME);
                String lastName = rs.getString(Constants.USERS_LASTNAME);
                usersName = firstName + " " + lastName;
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersName;
    }

    public List<Apartment> getApartments() {
        List<Apartment> apartments = new ArrayList<Apartment>();
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT * FROM " + Constants.APARTMENT_TABLE;

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            int number = 1;

            while (rs.next()) {
                int id = rs.getInt(Constants.APARTMENTS_ID);
                int idOwners = rs.getInt(Constants.APARTMENTS_ID_OWNERS);
                double area = rs.getDouble(Constants.APARTMENTS_AREA);
                String address = rs.getString(Constants.APARTMENTS_ADDRESS);
                String repairType = rs.getString(Constants.APARTMENTS_REPAIR_TYPE);

                String owner = getOwnersName(idOwners);
                Apartment apartment = new Apartment(number++, area, owner, address, repairType);
                apartments.add(apartment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apartments;
    }

    public String getApartmentsAddress(int ownersId) {
        String address = "";
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.APARTMENTS_ADDRESS + " FROM " + Constants.APARTMENT_TABLE
                    + " WHERE " + Constants.APARTMENTS_ID_OWNERS + " = '" + ownersId + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                address = rs.getString(Constants.APARTMENTS_ADDRESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    public String getOwnersName(int ownersId) {
        String ownersName = "";
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.USERS_FIRSTNAME + ", " + Constants.USERS_LASTNAME + " FROM "
                    + Constants.USER_TABLE + " WHERE " + Constants.USERS_ID + " = '" + ownersId + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String firstName = rs.getString(Constants.USERS_FIRSTNAME);
                String lastName = rs.getString(Constants.USERS_LASTNAME);
                ownersName = firstName + " " + lastName;
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ownersName;
    }

    public int getOwnersId(String name) {
        int id = 0;
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.USERS_ID + " FROM " + Constants.USER_TABLE +
                    " WHERE " + Constants.USERS_FIRSTNAME + " = '" + name.split(" ")[0] + "' AND " +
                    Constants.USERS_LASTNAME + " = '" + name.split(" ")[1] + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                id = rs.getInt(Constants.USERS_ID);
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public String getOwnersPinCode(int ownersId) {
        String pinCode = "";
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.USERS_PIN_CODE + " FROM " + Constants.USER_TABLE +
                    " WHERE " + Constants.USERS_ID + " = '" + ownersId + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                pinCode = rs.getString(Constants.USERS_PIN_CODE);
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pinCode;
    }

    public ObservableList<Material> getMaterials(String table) {
        ObservableList<Material> materials = FXCollections.observableArrayList();
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.MATERIALS_NAME + ", " + Constants.MATERIALS_MANUFACTURER
                    + ", " + Constants.MATERIALS_PRICE + ", " + Constants.MATERIALS_UNIT + ", "
                    + Constants.MATERIALS_AMOUNT + " FROM " + table;

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            int id = 1;

            while (rs.next()) {
                String name = rs.getString(Constants.MATERIALS_NAME);
                String manufacturer = rs.getString(Constants.MATERIALS_MANUFACTURER);
                double price = rs.getDouble(Constants.MATERIALS_PRICE);
                String unit = rs.getString(Constants.MATERIALS_UNIT);
                int amount = rs.getInt(Constants.MATERIALS_AMOUNT);

                Material material = new Material(id++, name, manufacturer, price, unit, amount);
                materials.add(material);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return materials;
    }

    public int getMaterialsId(String table, Material material) {
        int id = 0;
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.MATERIALS_ID + " FROM " + table
                    + " WHERE " + Constants.MATERIALS_NAME + " = '" + material.getName() +
                    "' AND " + Constants.MATERIALS_MANUFACTURER + " = '" + material.getManufacturer() + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                id = rs.getInt(Constants.MATERIALS_ID);
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public void addMaterial(String table, Material material) {
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = null;
            PreparedStatement prSt;

            if (table.contains("owner")) {
                query = "INSERT INTO " + table + "(" + Constants.MATERIALS_ID + "," +
                        Constants.MATERIALS_NAME + "," + Constants.MATERIALS_MANUFACTURER + "," +
                        Constants.MATERIALS_PRICE + "," + Constants.MATERIALS_UNIT + "," +
                        Constants.MATERIALS_AMOUNT + ")" + "VALUES(?, ?, ?, ?, ?, ?)";

                prSt = getDbConnection().prepareStatement(query);

                prSt.setInt(1, material.getId());
                prSt.setString(2, material.getName());
                prSt.setString(3, material.getManufacturer());
                prSt.setDouble(4, material.getPrice());
                prSt.setString(5, material.getUnit());
                prSt.setInt(6, material.getAmount());

            } else {
                query = "INSERT INTO " + table + "(" + Constants.MATERIALS_NAME + "," +
                        Constants.MATERIALS_MANUFACTURER + "," + Constants.MATERIALS_PRICE + "," +
                        Constants.MATERIALS_UNIT + "," + Constants.MATERIALS_AMOUNT +
                        ")" + "VALUES(?, ?, ?, ?, ?)";

                prSt = getDbConnection().prepareStatement(query);

                prSt.setString(1, material.getName());
                prSt.setString(2, material.getManufacturer());
                prSt.setDouble(3, material.getPrice());
                prSt.setString(4, material.getUnit());
                prSt.setInt(5, material.getAmount());
            }
            prSt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeMaterial(String table, Material material) {
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "DELETE FROM " + table + " WHERE id = ?";

            PreparedStatement prSt = conn.prepareStatement(query);
            prSt.setInt(1, material.getId());

            prSt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMaterial(String table, int id, Material material) {
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "UPDATE " + table + " SET " + Constants.MATERIALS_NAME + " = ?, "
                    + Constants.MATERIALS_MANUFACTURER + " = ?," + Constants.MATERIALS_PRICE + " = ?,"
                    + Constants.MATERIALS_UNIT + " = ?," + Constants.MATERIALS_AMOUNT + " = ? WHERE "
                    + Constants.MATERIALS_ID + " = '" + id + "'";

            PreparedStatement prSt = conn.prepareStatement(query);
            prSt.setString(1, material.getName());
            prSt.setString(2, material.getManufacturer());
            prSt.setDouble(3, material.getPrice());
            prSt.setString(4, material.getUnit());
            prSt.setInt(5, material.getAmount());

            prSt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMaterialsAmount(String table, int amount, Material material) {
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "UPDATE " + table + " SET " + Constants.MATERIALS_AMOUNT + " = ? WHERE "
                    + Constants.MATERIALS_ID + " = '" + material.getId() + "'";

            PreparedStatement prSt = conn.prepareStatement(query);
            prSt.setInt(1, amount);

            prSt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaterialsAmount(String table, Material material) {
        int amount = 0;
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.MATERIALS_AMOUNT + " FROM "+ table + " WHERE "
                    + Constants.MATERIALS_ID + " = '" + material.getId() + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                amount = rs.getInt(Constants.MATERIALS_AMOUNT);
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }

    public double getMaterialsPrice(String table, Material material) {
        double price = 0;
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.MATERIALS_PRICE + " FROM "+ table + " WHERE "
                    + Constants.MATERIALS_ID + " = '" + material.getId() + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                price = rs.getDouble(Constants.MATERIALS_PRICE);
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    public boolean isMaterialDuplicated(String table, Material material) {
        int counter = 0;
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.MATERIALS_NAME + ", " + Constants.MATERIALS_MANUFACTURER + " FROM "
                    + table + " WHERE " + Constants.MATERIALS_NAME + " = '" + material.getName() +
                    "' AND " + Constants.MATERIALS_MANUFACTURER + " = '" + material.getManufacturer() + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                counter++;
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
         if (counter >= 1) { return true; }
         else { return false; }
    }

    public void setMaterialsState(String table, String state, Material material) {
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "UPDATE " + table + " SET " + Constants.MATERIALS_STATE + " = ? WHERE "
                    + Constants.MATERIALS_ID + " = '" + material.getId() + "'";

            PreparedStatement prSt = conn.prepareStatement(query);
            prSt.setString(1, state);

            prSt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Material> getMaterialsByState(String table, String state) {
        ObservableList<Material> materials = FXCollections.observableArrayList();
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.MATERIALS_ID +  ", " + Constants.MATERIALS_NAME + ", " +
                    Constants.MATERIALS_MANUFACTURER + ", " + Constants.MATERIALS_PRICE + ", " +
                    Constants.MATERIALS_UNIT + ", " + Constants.MATERIALS_AMOUNT + " FROM " +
                    table + " WHERE " + Constants.MATERIALS_STATE + " = '" + state + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt(Constants.MATERIALS_ID);
                String name = rs.getString(Constants.MATERIALS_NAME);
                String manufacturer = rs.getString(Constants.MATERIALS_MANUFACTURER);
                double price = rs.getDouble(Constants.MATERIALS_PRICE);
                String unit = rs.getString(Constants.MATERIALS_UNIT);
                int amount = rs.getInt(Constants.MATERIALS_AMOUNT);

                materials.add(new Material(id, name, manufacturer, price, unit, amount));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return materials;
    }

    public double getOwnersCash(int id) {
        double cash = 0.0;
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "SELECT " + Constants.USERS_CASH + " FROM " + Constants.USER_TABLE
                    + " WHERE " + Constants.USERS_ID + " = '" + id + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                cash = rs.getDouble(Constants.USERS_CASH);
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cash;
    }

    public void setOwnersCash(int id, double cash) {
        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, dbUser, dbPass);

            String query = "UPDATE " + Constants.USER_TABLE + " SET " + Constants.USERS_CASH + " = ? WHERE "
                    + Constants.USERS_ID + " = '" + id + "'";

            PreparedStatement prSt = conn.prepareStatement(query);
            prSt.setDouble(1, cash);

            prSt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
