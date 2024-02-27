package com.example.sportcentersristeiuioana.repository;

import com.example.sportcentersristeiuioana.modelperson.GroupDetails;
import com.example.sportcentersristeiuioana.modelperson.MyGroupDetails;
import com.example.sportcentersristeiuioana.modelsportcenters.FieldDetails;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GroupRepository {
    public List<MyGroupDetails> getMyGroupsDetails(int personId) {
        List<MyGroupDetails> groups = new ArrayList<>();

        try (Connection connection = getConnection()) {
            String query = "SELECT group_name,status FROM mygroupdetails WHERE person_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,personId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String groupName = resultSet.getString("group_name");
                String status = resultSet.getString("status");

                MyGroupDetails myGroupDetails = new MyGroupDetails(groupName,"","",status);
                groups.add(myGroupDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public List<GroupDetails> getAllGroupsDetails(int personId) {
        List<GroupDetails> groups = new ArrayList<>();

        try (Connection connection = getConnection()) {
            String query = "SELECT *\n" +
                    "FROM reservation_details_view rv\n" +
                    "where group_name not in (\n" +
                    "\tselect group_name \n" +
                    "\tfrom \"_groups\" g \n" +
                    "\tjoin members m  on g.group_id = m.group_id \n" +
                    "\twhere m.member_id = ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int reservation_id = resultSet.getInt("reservation_id");
                String groupName = resultSet.getString("group_name");
                Date date = resultSet.getDate("reservation_date");
                String hours = resultSet.getString("hours");
                String sport_name = resultSet.getString("sport_name");
                String country = resultSet.getString("country");
                String city = resultSet.getString("city");
                String address_details = resultSet.getString("address_details");
                int nrOfPlayers = resultSet.getInt("group_nr_of_players");
                boolean available = resultSet.getBoolean("available");

                GroupDetails groupDetails = new GroupDetails(reservation_id, groupName, date, hours, sport_name,country, city, address_details, nrOfPlayers, available);
                groups.add(groupDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }
    public List<GroupDetails> getGroups(String groupNameSearch, int personId) {
        List<GroupDetails> groups = new ArrayList<>();
        groupNameSearch = groupNameSearch.toLowerCase();
        groupNameSearch = "%"+ groupNameSearch +"%";

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM reservation_details_view rv " +
                    "JOIN mygroupDetails  gv ON rv.group_name = gv.group_name " +
                    "WHERE LOWER(rv.group_name) LIKE ? AND gv.person_id <> ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, groupNameSearch);
            preparedStatement.setInt(2, personId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int reservation_id = resultSet.getInt("reservation_id");
                String groupName = resultSet.getString("group_name");
                Date date = resultSet.getDate("reservation_date");
                String hours = resultSet.getString("hours");
                String sport_name = resultSet.getString("sport_name");
                String country = resultSet.getString("country");
                String city = resultSet.getString("city");
                String address_details = resultSet.getString("address_details");
                int nrOfPlayers = resultSet.getInt("group_nr_of_players");
                boolean available = resultSet.getBoolean("available");

                GroupDetails groupDetails = new GroupDetails(reservation_id, groupName, date, hours, sport_name,country, city, address_details, nrOfPlayers, available);
                groups.add(groupDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }
    public void addToGroup(String groupName, int memberId, boolean status){
        PreparedStatement memberStatement = null;
        PreparedStatement groupStatement = null;
        Connection connection = null;
        int rowsAffected = 0;
            try {
                connection = getConnection();
                String groupString = "SELECT group_id FROM _groups WHERE group_name = ?";
                groupStatement = connection.prepareStatement(groupString);
                groupStatement.setString(1, groupName);
                int groupId = 0;
                try (ResultSet resultSet = groupStatement.executeQuery()) {
                    if (resultSet.next()) {
                        groupId = resultSet.getInt("group_id");
                    }
                }
                if(groupId != 0) {
                    String insertMembersQuery = "INSERT INTO members (group_id, member_id, status_admin) VALUES (?,?,?)";
                    memberStatement = connection.prepareStatement(insertMembersQuery);
                    memberStatement.setInt(1, groupId);
                    memberStatement.setInt(2, memberId);
                    memberStatement.setBoolean(3, status);
                    rowsAffected = memberStatement.executeUpdate();
                }

            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                System.out.println("Error at DB level");
            } finally {
                // Close resources in a finally block to ensure they are closed even if an exception occurs
                try {
                    if (memberStatement != null) {
                        memberStatement.close();
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
    public int getGroupId(String groupName) {
        try (Connection connection = getConnection()) {
            String query = "SELECT group_id FROM _groups WHERE group_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, groupName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("group_id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if group is not found or an exception occurs
    }

    public boolean deleteMemberAndCheckGroup(int groupId, int memberId) {
        try (Connection connection = getConnection()) {
            String deleteMemberFunctionCall = "SELECT deleteMemberAndCheckGroup(?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(deleteMemberFunctionCall)) {
                statement.setInt(1, memberId);
                statement.setInt(2, groupId);

                statement.execute();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/SportCenters";
        String dbUsername = "postgres";
        String dbPassword = "Camin214";
        return DriverManager.getConnection(url, dbUsername, dbPassword);
    }
}
