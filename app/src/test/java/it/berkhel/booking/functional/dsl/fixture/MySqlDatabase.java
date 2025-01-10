package it.berkhel.booking.functional.dsl.fixture;

import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;

import static org.hamcrest.core.IsEqual.equalTo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlDatabase {

    private String url;
    private String user = "root";
    private String password = "test";


    public MySqlDatabase(Integer port) throws SQLException{
        this.url ="jdbc:mysql://localhost:" + port + "/booking";
        this.initDatabase();
    }

    private void initDatabase() throws SQLException{
        try(Connection con = connection();
        // PreparedStatement createDatabase = con.prepareStatement("CREATE DATABASE IF NOT EXISTS booking");
        //PreparedStatement useDatabase = con.prepareStatement("USE booking");
       // PreparedStatement createTable = con.prepareStatement("CREATE TABLE purchase ( id VARCHAR(255) )");
        ){
            // createDatabase.execute();
            //useDatabase.execute();
        //    createTable.execute();
        }
    }

    private Connection connection() throws SQLException{
        return DriverManager.getConnection(url, user, password);
    }



    public String query(String preparedQuery, List<String> queryParameters, String column) throws SQLException {
        String result = "";
        try (Connection con = connection();
                PreparedStatement stmt = con.prepareStatement(preparedQuery)) {
                for(var i = 0; i < queryParameters.size(); i++){
                    stmt.setString(i+1, queryParameters.get(i));
                }
            try (ResultSet resultSet = stmt.executeQuery()) {
                if(resultSet.next()){
                    result = resultSet.getString(column);
                }
            }
        }
        return result;
    }

    public String queryRecordIdWithTheSameId(String id, String table) throws SQLException {
        return query("SELECT id FROM "+ table + " WHERE id LIKE ?", List.of(id), "id");
    }

    public static ResponseAwareMatcher<Response> isEqualToRecordIdFrom(MySqlDatabase mySqlDatabase, String table){
        return response -> equalTo(mySqlDatabase.queryRecordIdWithTheSameId(response.getBody().jsonPath().getString("id"), table));
    }
    
}
