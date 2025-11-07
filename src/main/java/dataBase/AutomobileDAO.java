package dataBase;

import Model.Automobile;


import java.sql.*;
import java.util.Random;

public class AutomobileDAO {

    public Automobile getRandomAutomobile() throws SQLException {
        String sql = "SELECT * FROM automobile ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new Automobile(
                        rs.getInt("id_car"),
                        rs.getString("licenseplate"),
                        rs.getString("owner"),
                        rs.getString("address"),
                        rs.getString("color"),
                        rs.getInt("id_model")
                );
            }
        }
        throw new SQLException("No automobiles found in database");
    }
}
