package com.example.sportcentersristeiuioana.repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationRepository {
    Connection connection = null;
    PreparedStatement groupStatement = null;
    PreparedStatement memberStatment = null;
    PreparedStatement reservationStatemnet = null;
    int rowsAffected = 0;
    public void createReservation(int memberId, String groupName, Boolean status, int fielfId, LocalTime start, LocalTime end) {
        int groupId = 0 ;
        try {
            connection = getConnection();

            String insertGroupQuery = "INSERT INTO _groups (group_name) VALUES (?)";
            groupStatement = connection.prepareStatement(insertGroupQuery, Statement.RETURN_GENERATED_KEYS);
            groupStatement.setString(1, groupName);

             rowsAffected = groupStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = groupStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    groupId = generatedKeys.getInt(1); // Retrieve the generated primary key
                }
            }
            else{
                throw new SQLException("Inserting group failed, no rows affected.");
            }

            String insertMembersQuery = "INSERT INTO members (group_id, member_id, status_admin) VALUES (?,?,?)";
            memberStatment = connection.prepareStatement(insertMembersQuery);
            memberStatment.setInt(1, groupId);
            memberStatment.setInt(2, memberId);
            memberStatment.setBoolean(3,status);
            int rowsAffected1 = memberStatment.executeUpdate();

            String insertReservation = "INSERT INTO reservation (field_id, reservation_date, start_time, end_time,reserved_by) VALUES (?,?,?,?,?)";
            reservationStatemnet = connection.prepareStatement(insertReservation);
            reservationStatemnet.setInt(1, fielfId);
            reservationStatemnet.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            reservationStatemnet.setTime(3, java.sql.Time.valueOf(start));
            reservationStatemnet.setTime(4, java.sql.Time.valueOf(end));
            reservationStatemnet.setInt(5, groupId);
            int rowsAffected2 = reservationStatemnet.executeUpdate();

            rowsAffected += rowsAffected2+rowsAffected1;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Error at DB level");
        } finally {
            // Close resources in a finally block to ensure they are closed even if an exception occurs
            try {
                if (memberStatment != null) {
                    memberStatment.close();
                }
                if(groupStatement != null){
                    groupStatement.close();
                }
                if(reservationStatemnet != null){
                    reservationStatemnet.close();
                }
                if (connection != null) {
                    connection.close();
                }
                System.out.println( rowsAffected + " Rows Affected. Success! Connection Closed!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println( "Error at DB level");
            }
        }
    }
    public static boolean checkReservation(int fieldId, LocalTime startTime, LocalTime endTime)
            throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "SELECT checkReservationOverlap(?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, fieldId);
                preparedStatement.setTime(2, Time.valueOf(startTime));
                preparedStatement.setTime(3, Time.valueOf(endTime));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBoolean(1);
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/SportCenters";
        String dbUsername = "postgres";
        String dbPassword = "Camin214";
        return DriverManager.getConnection(url, dbUsername, dbPassword);
    }
}
