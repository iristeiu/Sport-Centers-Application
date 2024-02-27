package com.example.sportcentersristeiuioana.repository;

import com.example.sportcentersristeiuioana.modelperson.Person;

import java.sql.*;
import java.time.LocalDate;

public class PersonRepository {
    Connection connection = null;
    PreparedStatement userStatement = null;
    PreparedStatement personStatement = null;
    public int verifyEmail(String email){
        int count = -1;
        try {
            connection = getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS email_count FROM _user WHERE email = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("email_count");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public int verifyAccount(String email, String password){
        int count = -1;
        try {
            connection = getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS account FROM _user WHERE email = ? AND user_password = ?");
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("account");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public String insertPerson(String email, String password, String firstName, String lastName, LocalDate dob, String gender) {

        Integer rowsAffected1 = 0;
        Integer rowsAffected2 = 0;
        try {
            connection = getConnection();
            String insertUserQuery = "INSERT INTO _user (email, user_password) VALUES (?, ?)";
            userStatement = connection.prepareStatement(insertUserQuery);

            userStatement.setString(1, email);
            userStatement.setString(2, password);
            rowsAffected1 = userStatement.executeUpdate();


            String insertPersonQuery = "INSERT INTO person (last_name, first_name, date_of_birth, gender, status) VALUES (?, ?, ?, CAST(? AS gender_enum), ?)";
            personStatement = connection.prepareStatement(insertPersonQuery);
            personStatement.setString(1, lastName);
            personStatement.setString(2, firstName);
            personStatement.setDate(3, Date.valueOf(dob));
            personStatement.setString(4, gender);
            personStatement.setInt(5, 1);
            rowsAffected2 = personStatement.executeUpdate();
            rowsAffected1 +=rowsAffected2;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return "Error at DB level";
        } finally {
            // Close resources in a finally block to ensure they are closed even if an exception occurs
            try {
                if (personStatement != null) {
                    personStatement.close();
                }
                if(userStatement != null){
                    userStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
                return rowsAffected1 + " Rows Affected. Success! Connection Closed!";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error at DB level";
            }
        }
    }
    public Person getAllPersonDetails(String email, String password) {
        Person person = new Person();

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM persondetails WHERE email = ? AND user_password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                person.setPersonId(resultSet.getInt("person_id"));
                person.setLastName(resultSet.getString("last_name"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setDob(resultSet.getDate("date_of_birth").toLocalDate());
                person.setGender(resultSet.getString("gender"));
                person.setStatus(resultSet.getString("person_status"));
                person.setEmail(resultSet.getString("email"));
                person.setPassword(resultSet.getString("user_password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;
    }

    public boolean deleteAccount(int idPerson) {
        boolean confDelete = false;
        PreparedStatement deleteStatement = null;
        try {
            connection = getConnection();

            String query = "SELECT DeletePerson(?)";
            deleteStatement = connection.prepareStatement(query);
            deleteStatement.setInt(1, idPerson);

            ResultSet resultSet = deleteStatement.executeQuery();

            if (resultSet.next()) {
                confDelete = resultSet.getBoolean(1);
            }

        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Error person deletion");
        } finally {
            try {
                if (deleteStatement != null) {
                    deleteStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return confDelete;
    }
    public Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/SportCenters";
        String dbUsername = "postgres";
        String dbPassword = "Camin214";
        return DriverManager.getConnection(url, dbUsername, dbPassword);
    }

    public boolean updateFirstName(int personId, String firstName) {
        CallableStatement update = null;
        boolean updated = false;
        try {
            connection = getConnection();

            String query = "{? = CALL UpdateFirstName(?, ?)}";
            update = connection.prepareCall(query);
            update.registerOutParameter(1, Types.BOOLEAN);
            update.setInt(2, personId);
            update.setString(3, firstName);

            update.execute();
            updated = update.getBoolean(1);

        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Error person deletion");
        } finally {
            try {
                if (update != null) {
                    update.close();
                }

                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return updated;
    }

    public boolean updateLastName(int personId, String lastName) {
        CallableStatement update = null;
        boolean updated = false;
        try {
            connection = getConnection();

            String query = "{? = CALL UpdateLastName(?, ?)}";
            update = connection.prepareCall(query);
            update.registerOutParameter(1, Types.BOOLEAN);
            update.setInt(2, personId);
            update.setString(3, lastName);

            update.execute();
            updated = update.getBoolean(1);

        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Error person deletion");
        } finally {
            try {
                if (update != null) {
                    update.close();
                }

                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return updated;
    }

}
