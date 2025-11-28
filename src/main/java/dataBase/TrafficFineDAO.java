package dataBase;

import Fine.TrafficFine;

import java.io.IOException;
import java.sql.*;

public class TrafficFineDAO {

    public void saveFine(TrafficFine fine){
        String sql = """
        INSERT INTO fine (infringement_type, description, infringement_date, amount, points, vehicle_plate)
        VALUES (?, ?, ?, ?, ?, ?)
        RETURNING fine_number
    """;

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fine.getTypeInfraction().toString());
            ps.setString(2, fine.getTypeInfraction().toString());
            ps.setTimestamp(3, Timestamp.valueOf(fine.getEvent().getDateTime()));
            ps.setBigDecimal(4, fine.getFinalAmount());
            ps.setInt(5, fine.getPointScoring());
            ps.setString(6, fine.getAutomobile().getLicensePlate());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int generatedId = rs.getInt("fine_number");
                fine.setFineNumber(generatedId);

                try {
                    fine.generatePDF();
                } catch (IOException e) {
                    System.err.println("Error generating PDF for fine " + generatedId + ": " + e.getMessage());
                }
            }

            System.out.println("Fine registered and PDF generated successfully! Fine number: " + fine.getFineNumber());

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error saving fine: " + e.getMessage());
        }
    }

    public long getNextFineNumber() throws SQLException {
        String sql = "SELECT nextval('fine_number_seq')";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
            throw new SQLException("Could not get next fine number");
        }
    }
}
