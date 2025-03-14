import java.sql.*;

public class BloodBankDatabase {
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/new_schema";
    static final String USERNAME = "root";
    static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int insertDonor(String name, String bloodGroup, String contactNumber, int age) {
        String sql = "INSERT INTO Donors (name, blood_group, contact_number, age) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, bloodGroup);
            preparedStatement.setString(3, contactNumber);
            preparedStatement.setInt(4, age);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Return the generated user ID
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Return -1 if user ID retrieval fails
    }

    public static ResultSet getAllDonors() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        Statement statement = connection.createStatement();
        return statement.executeQuery("SELECT count(name),blood_group FROM Donors group by blood_group");
    }

    public static boolean removeDonor(int userId) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Donors WHERE user_id = ?")) {

            preparedStatement.setInt(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
