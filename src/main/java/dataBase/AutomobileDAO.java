package dataBase;

import Model.Automobile;


import java.sql.*;
import java.util.Random;

public class AutomobileDAO {

    public Automobile getRandomAutomobile() throws SQLException {
        String sql = "SELECT * FROM automobiles ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new Automobile(
                        rs.getInt("id"),
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

    public Automobile findAutomobileByPlate(String plate) throws SQLException {
        String sql = """
        SELECT
             a.id AS automobiles_id,
             a.licenseplate,
             a.color,
             a.owner,
             a.address,
             m.id_model AS model_id,
             m.name AS model_name,
             mk.id_make AS vehiclemake_id,
             mk.name AS vehiclemake_name
        FROM automobiles a
        JOIN model m ON a.id_model = m.id_model
        JOIN vehiclemake mk ON m.id_make = mk.id_make
        WHERE a.licenseplate = ?;
    """;

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, plate);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Automobile a = new Automobile();
                    a.setId(rs.getInt("automobiles_id"));
                    a.setLicensePlate(rs.getString("licenseplate"));
                    a.setOwner(rs.getString("owner"));
                    a.setId_model(rs.getInt("model_id"));
                    a.setAddress(rs.getString("address"));
                    a.setColor(rs.getString("color"));

                    return a;
                } else {
                    return null;
                }
            }
        }
    }
}
