package dataBase;

import Model.Automobile;
import Model.AutomobileModel;
import Model.TrafficFine;

import java.sql.*;

public class TrafficFineDAO {

    public void saveFine(TrafficFine fine){
        String sql = "INSERT INTO fines (id_fine,issue_date,amount,points_deducted,offence_type,description,id_car )"+
                "VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1,fine.getFineNumber());
            ps.setTimestamp(2,java.sql.Timestamp.valueOf(fine.getEvent().getDateTime()));
            ps.setDouble(3,fine.getFinalAmount().doubleValue());
            ps.setInt(4,fine.getPointScoring());
            ps.setString(5,fine.getTypeInfraction().getType().toString());
            ps.setString(6,fine.getTypeInfraction().getDescription());
            ps.setInt(7,fine.getAutomobile().getId());

            ps.executeUpdate();
            System.out.println("✅ Fine registered successfully!");

        }catch (SQLException e){
            e.printStackTrace();
            System.err.println("❌ Error saving fine: " + e.getMessage());
        }

    }





    public Automobile findAutomobileByPlate(String plate) throws SQLException {
        String sql ="""
        SELECT
             a.id AS automobiles_id,
             a.licenseplate,
             a.color,
             a.owner,
             a.address,
             m.id AS model_id,
             m.name AS model_name,
             mk.id AS vehiclemake_id,
             mk.name AS vehiclemake_name
        FROM automobiles a
        JOIN model m ON a.id_model = m.id
        JOIN vehiclemake mk ON m.id_make = mk.id
        WHERE a.licenseplate = ?;
    """;
        try (Connection conn =  DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Automobile a = new Automobile();
                    a.setId(rs.getInt("automobiles_id"));
                    a.setLicensePlate(rs.getString("licenseplate"));
                    a.setOwner(rs.getString("owner"));
                    a.setModel(new AutomobileModel(rs.getInt("model_id"),rs.getString("model_name")));
                    a.setAddress(rs.getString("address"));
                    return a;
                } else {
                    return null;
                }
            }
        }
    }



}
