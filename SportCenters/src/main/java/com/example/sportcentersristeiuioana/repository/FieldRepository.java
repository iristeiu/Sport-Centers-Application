package com.example.sportcentersristeiuioana.repository;

import com.example.sportcentersristeiuioana.modelsportcenters.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FieldRepository {
    Connection connection = null;
    PreparedStatement insideFieldStatement = null;
    PreparedStatement outsideFieldStatement = null;
    PreparedStatement sportFieldStatement = null;
    PreparedStatement fieldStatement = null;
    ResultSet resultSet = null;

    public void insertInsideField(Sport sport, InsideField field) {
        try {
            connection = getConnection();

            int sport_id = insertSport(sport, connection);
            int address_id = insertAddress(field.getAddress(), connection);
            int field_id = -1;

            fieldStatement = connection.prepareStatement("INSERT INTO field(address_id, price_hour) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            fieldStatement.setInt(1, address_id);
            fieldStatement.setFloat(2, field.getPricePerHour());

            int rowsAffected = fieldStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = fieldStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    field_id = generatedKeys.getInt(1); // Retrieve the generated primary key
                }
            }
            else{
                throw new SQLException("Inserting field failed, no rows affected.");
            }

            insideFieldStatement = connection.prepareStatement("INSERT INTO inside_field (field_id, ventilation, floor_material) VALUES (?, ?, ?)");
            insideFieldStatement.setInt(1,field_id);
            insideFieldStatement.setBoolean(2, field.isVentilation());
            insideFieldStatement.setString(3, field.getFloorMaterial());
            insideFieldStatement.executeUpdate();
            insertRelSportField(sport_id, field_id, connection);
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Error field insert");
        } finally {
            try {
                if (insideFieldStatement != null) {
                    insideFieldStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertRelSportField(int sportId, int fieldId, Connection connection) {
        try {
            sportFieldStatement = connection.prepareStatement("INSERT INTO sport_field(field_id,sport_id) VALUES(?, ?)");
            sportFieldStatement.setInt(1, fieldId);
            sportFieldStatement.setInt(2, sportId);

            int affectedRows = sportFieldStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting inside_field failed, no rows affected.");
            }


        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                if (sportFieldStatement != null) {
                    sportFieldStatement.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertOutsideField(Sport sport, OutsideField field) {
        try {
            connection = getConnection();

            int sport_id = insertSport(sport, connection);
            int address_id = insertAddress(field.getAddress(), connection);
            int field_id = -1;

            fieldStatement = connection.prepareStatement("INSERT INTO field(address_id, price_hour) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            fieldStatement.setInt(1, address_id);
            fieldStatement.setFloat(2, field.getPricePerHour());

            int rowsAffected = fieldStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = fieldStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    field_id = generatedKeys.getInt(1); // Retrieve the generated primary key
                }
            }
            else{
                throw new SQLException("Inserting field failed, no rows affected.");
            }

            outsideFieldStatement = connection.prepareStatement("INSERT INTO outside_field (field_id, surface_nature, night_lights) VALUES (?, ?, ?)");
            outsideFieldStatement.setInt(1,field_id);
            outsideFieldStatement.setBoolean(3, field.isNightLights());
            outsideFieldStatement.setString(2, field.getSurfaceNature());
            outsideFieldStatement.executeUpdate();
            insertRelSportField(sport_id, field_id, connection);
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Error field insert");
        } finally {
            try {
                if (insideFieldStatement != null) {
                    insideFieldStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int insertAddress(Address address, Connection connection) {
        int primaryKey = -1;
        boolean output;
        PreparedStatement statement = null;
        CallableStatement callableStatement = null;
        try {

            callableStatement = connection.prepareCall("{? = call CheckAddressExists(?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.BOOLEAN);
            callableStatement.setString(2, address.getCountry());
            callableStatement.setString(3, address.getCounty());
            callableStatement.setString(4, address.getCity());
            callableStatement.setString(5, address.getDetails());
            callableStatement.execute();

            output = callableStatement.getBoolean(1);

            if (output) {
                statement = connection.prepareStatement("SELECT * FROM address WHERE country = ? AND county = ? AND city = ? AND address_details = ? ;");
                statement.setString(1, address.getCountry());
                statement.setString(2, address.getCounty());
                statement.setString(3, address.getCity());
                statement.setString(4, address.getDetails());

                ResultSet resultSetSelect = statement.executeQuery();

                // Assuming you want to retrieve a value from the SELECT query result set
                if (resultSetSelect.next()) {
                    primaryKey = resultSetSelect.getInt("address_id"); // Replace "id" with your primary key column name
                    // Retrieve other necessary values from the result set if needed
                }
                // Handle no rows found in the SELECT query if needed
            } else {
                statement = connection.prepareStatement("INSERT INTO address(country, county, city, address_details) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, address.getCountry());
                statement.setString(2, address.getCounty());
                statement.setString(3, address.getCity());
                statement.setString(4, address.getDetails());

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        primaryKey = generatedKeys.getInt(1); // Retrieve the generated primary key
                    }
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (statement != null) {
                    statement.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
        return primaryKey;
    }
    public int insertSport(Sport sport, Connection connection) {
        int count = -1, primaryKey = -1;
        boolean output = false;
        PreparedStatement statement = null;
        CallableStatement callableStatement = null;
        try {
            callableStatement = connection.prepareCall("{? = call CheckSportExists(?, ?)}");
            callableStatement.registerOutParameter(1, Types.BOOLEAN);
            callableStatement.setString(2, sport.getSportName());
            callableStatement.setInt(3, sport.getMaxPlayers());
            callableStatement.execute();

            output = callableStatement.getBoolean(1);

            if (output) {
                statement = connection.prepareStatement("SELECT * FROM sport WHERE sport_name = ? and max_players = ?;");
                statement.setString(1, sport.getSportName());
                statement.setInt(2, sport.getMaxPlayers());

                ResultSet resultSetSelect = statement.executeQuery();

                // Assuming you want to retrieve a value from the SELECT query result set
                if (resultSetSelect.next()) {
                    primaryKey = resultSetSelect.getInt("sport_id"); // Replace "id" with your primary key column name
                    // Retrieve other necessary values from the result set if needed
                }
                // Handle no rows found in the SELECT query if needed
            } else {
                statement = connection.prepareStatement("INSERT INTO sport(sport_name, max_players) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, sport.getSportName());
                statement.setInt(2, sport.getMaxPlayers());

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        primaryKey = generatedKeys.getInt(1); // Retrieve the generated primary key
                    }
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (statement != null) {
                    statement.close();
                }


            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
        return primaryKey;
    }

    public List<FieldDetails> getAllFieldDetails() {
        List<FieldDetails> fields = new ArrayList<>();

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM allfielddetails";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("field_id");
                String country = resultSet.getString("country");
                String county = resultSet.getString("county");
                String city = resultSet.getString("city");
                String details = resultSet.getString("address_details");
                int price = resultSet.getInt("price_hour");
                String sportName = resultSet.getString("sport_name");
                int maxPlayers = resultSet.getInt("max_players");
                String fieldType = resultSet.getString("field_type");
                Boolean ventLight = resultSet.getBoolean("ventilation_lights");
                String floorSurf = resultSet.getString("floor_surface");

                FieldDetails fieldDetails = new FieldDetails(id,country,county,city,details,price,sportName,maxPlayers, fieldType,ventLight,floorSurf);
                fields.add(fieldDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fields;
    }

    public List<FieldDetails> getCity(String citySearch) {
        List<FieldDetails> fields = new ArrayList<>();
        citySearch = citySearch.toLowerCase();
        citySearch = "%"+ citySearch +"%";

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM allfielddetails WHERE LOWER(city) LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, citySearch);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("field_id");
                String country = resultSet.getString("country");
                String county = resultSet.getString("county");
                String city = resultSet.getString("city");
                String details = resultSet.getString("address_details");
                int price = resultSet.getInt("price_hour");
                String sportName = resultSet.getString("sport_name");
                int maxPlayers = resultSet.getInt("max_players");
                String fieldType = resultSet.getString("field_type");
                Boolean ventLight = resultSet.getBoolean("ventilation_lights");
                String floorSurf = resultSet.getString("floor_surface");

                FieldDetails fieldDetails = new FieldDetails(id,country,county,city,details,price,sportName,maxPlayers, fieldType,ventLight,floorSurf);
                fields.add(fieldDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fields;
    }
    public List<FieldDetails> getLoc(String locSearch) {
        List<FieldDetails> fields = new ArrayList<>();
        locSearch = locSearch.toLowerCase();
        locSearch = "%"+ locSearch +"%";

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM allfielddetails WHERE LOWER(address_details) LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, locSearch);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("field_id");
                String country = resultSet.getString("country");
                String county = resultSet.getString("county");
                String city = resultSet.getString("city");
                String details = resultSet.getString("address_details");
                int price = resultSet.getInt("price_hour");
                String sportName = resultSet.getString("sport_name");
                int maxPlayers = resultSet.getInt("max_players");
                String fieldType = resultSet.getString("field_type");
                Boolean ventLight = resultSet.getBoolean("ventilation_lights");
                String floorSurf = resultSet.getString("floor_surface");

                FieldDetails fieldDetails = new FieldDetails(id,country,county,city,details,price,sportName,maxPlayers, fieldType,ventLight,floorSurf);
                fields.add(fieldDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fields;
    }
    public List<FieldDetails> getSportSearch(String sportSearch) {
        List<FieldDetails> fields = new ArrayList<>();
        sportSearch = sportSearch.toLowerCase();
        sportSearch = "%"+ sportSearch +"%";

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM allfielddetails WHERE sport_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, sportSearch);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("field_id");
                String country = resultSet.getString("country");
                String county = resultSet.getString("county");
                String city = resultSet.getString("city");
                String details = resultSet.getString("address_details");
                int price = resultSet.getInt("price_hour");
                String sportName = resultSet.getString("sport_name");
                int maxPlayers = resultSet.getInt("max_players");
                String fieldType = resultSet.getString("field_type");
                Boolean ventLight = resultSet.getBoolean("ventilation_lights");
                String floorSurf = resultSet.getString("floor_surface");

                FieldDetails fieldDetails = new FieldDetails(id,country,county,city,details,price,sportName,maxPlayers, fieldType,ventLight,floorSurf);
                fields.add(fieldDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fields;
    }

    public List<FieldDetails> getCityLoc(String citySearch, String locationSearch) {
        List<FieldDetails> fields = new ArrayList<>();
        citySearch = citySearch.toLowerCase();
        citySearch = "%"+ citySearch +"%";

        locationSearch = locationSearch.toLowerCase();
        locationSearch = "%"+ locationSearch +"%";

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM allfielddetails WHERE LOWER(city) LIKE ? AND LOWER(address_details) LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, citySearch);
            preparedStatement.setString(2, locationSearch);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("field_id");
                String country = resultSet.getString("country");
                String county = resultSet.getString("county");
                String city = resultSet.getString("city");
                String details = resultSet.getString("address_details");
                int price = resultSet.getInt("price_hour");
                String sportName = resultSet.getString("sport_name");
                int maxPlayers = resultSet.getInt("max_players");
                String fieldType = resultSet.getString("field_type");
                Boolean ventLight = resultSet.getBoolean("ventilation_lights");
                String floorSurf = resultSet.getString("floor_surface");

                FieldDetails fieldDetails = new FieldDetails(id,country,county,city,details,price,sportName,maxPlayers, fieldType,ventLight,floorSurf);
                fields.add(fieldDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fields;
    }
    public List<FieldDetails> getCitySport(String citySearch, String sportSearch) {
        List<FieldDetails> fields = new ArrayList<>();
        citySearch = citySearch.toLowerCase();
        citySearch = "%"+ citySearch +"%";

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM allfielddetails WHERE LOWER(city) LIKE ? AND sport_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, citySearch);
            preparedStatement.setString(2, sportSearch);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("field_id");
                String country = resultSet.getString("country");
                String county = resultSet.getString("county");
                String city = resultSet.getString("city");
                String details = resultSet.getString("address_details");
                int price = resultSet.getInt("price_hour");
                String sportName = resultSet.getString("sport_name");
                int maxPlayers = resultSet.getInt("max_players");
                String fieldType = resultSet.getString("field_type");
                Boolean ventLight = resultSet.getBoolean("ventilation_lights");
                String floorSurf = resultSet.getString("floor_surface");

                FieldDetails fieldDetails = new FieldDetails(id,country,county,city,details,price,sportName,maxPlayers, fieldType,ventLight,floorSurf);
                fields.add(fieldDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fields;
    }
    public List<FieldDetails> getLocSport(String locSearch, String sportSearch) {
        List<FieldDetails> fields = new ArrayList<>();
        locSearch = locSearch.toLowerCase();
        locSearch = "%"+ locSearch +"%";

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM allfielddetails WHERE LOWER(address_details) LIKE ? AND sport_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, locSearch);
            preparedStatement.setString(2, sportSearch);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("field_id");
                String country = resultSet.getString("country");
                String county = resultSet.getString("county");
                String city = resultSet.getString("city");
                String details = resultSet.getString("address_details");
                int price = resultSet.getInt("price_hour");
                String sportName = resultSet.getString("sport_name");
                int maxPlayers = resultSet.getInt("max_players");
                String fieldType = resultSet.getString("field_type");
                Boolean ventLight = resultSet.getBoolean("ventilation_lights");
                String floorSurf = resultSet.getString("floor_surface");

                FieldDetails fieldDetails = new FieldDetails(id,country,county,city,details,price,sportName,maxPlayers, fieldType,ventLight,floorSurf);
                fields.add(fieldDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fields;
    }
    public List<FieldDetails> getCityLocSport(String citySearch, String locationSearch, String sportSearch) {
        List<FieldDetails> fields = new ArrayList<>();
        citySearch = citySearch.toLowerCase();
        citySearch = "%"+ citySearch +"%";

        locationSearch = locationSearch.toLowerCase();
        locationSearch = "%"+ locationSearch +"%";

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM allfielddetails WHERE LOWER(city) LIKE ? AND LOWER(address_details) LIKE ? AND sport_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, citySearch);
            preparedStatement.setString(2, locationSearch);
            preparedStatement.setString(3, sportSearch);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("field_id");
                String country = resultSet.getString("country");
                String county = resultSet.getString("county");
                String city = resultSet.getString("city");
                String details = resultSet.getString("address_details");
                int price = resultSet.getInt("price_hour");
                String sportName = resultSet.getString("sport_name");
                int maxPlayers = resultSet.getInt("max_players");
                String fieldType = resultSet.getString("field_type");
                Boolean ventLight = resultSet.getBoolean("ventilation_lights");
                String floorSurf = resultSet.getString("floor_surface");

                FieldDetails fieldDetails = new FieldDetails(id,country,county,city,details,price,sportName,maxPlayers, fieldType,ventLight,floorSurf);
                fields.add(fieldDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fields;
    }
    public String[] getSportsList() {
        List<String> sportsList = new ArrayList<>();

        try (Connection connection = getConnection()) {
            String query = "SELECT sport_name FROM sport";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String sportName = resultSet.getString("sport_name");
                sportsList.add(sportName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Convert the List<String> to a String array
        return sportsList.toArray(new String[0]);
    }

    public boolean deleteField(int fieldId) {
        boolean confDelete = false;
        PreparedStatement deleteStatement = null;
        try {
            connection = getConnection();

            String query = "SELECT deleteFieldAndAssociatedData(?)";
            deleteStatement = connection.prepareStatement(query);
            deleteStatement.setInt(1, fieldId);

            ResultSet resultSet = deleteStatement.executeQuery();

            if (resultSet.next()) {
                confDelete = resultSet.getBoolean(1);
            }

        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Error field deletion");
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
}
