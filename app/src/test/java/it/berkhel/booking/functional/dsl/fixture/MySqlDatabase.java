package it.berkhel.booking.functional.dsl.fixture;

import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;

import static org.hamcrest.core.IsEqual.equalTo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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



    public String query(QueryHolder queryHolder, Function<ResultSet, String> resultSetHandler) throws SQLException {
        String result = "";
        try (Connection con = connection();
                PreparedStatement stmt = con.prepareStatement(queryHolder.query())) {
                List<String> parameters = queryHolder.parameters();
                for(var i = 0; i < parameters.size(); i++){
                    stmt.setString(i+1, parameters.get(i));
                }
            try (ResultSet resultSet = stmt.executeQuery()) {
                result = resultSetHandler.apply(resultSet);
            }
        }
        return result;
    }




    public String queryRecordIdWithTheSameId(String id, String table) throws SQLException {
        return query(SelectQueryBuilder.select("id").from(table).where("id", "LIKE", id).build(),
                rset -> {
                    try {
                        return rset.next() ? rset.getString("id") : "";
                    } catch (SQLException e) {
                        return "";
                    }
                });
    }

    public static ResponseAwareMatcher<Response> isEqualToRecordIdFrom(MySqlDatabase mySqlDatabase, String table){
        return response -> equalTo(mySqlDatabase.queryRecordIdWithTheSameId(response.getBody().jsonPath().getString("id"), table));
    }
    
}

class SelectQueryBuilder {

    public static SelectQueryBuilder select(String fields){
        return new SelectQueryBuilder(fields);
    }

    private String fields;
    private String table;
    private List<WhereClause> whereClauses = new ArrayList<>();

    private SelectQueryBuilder(String fields) {
        this.fields = fields;
    }

    public SelectQueryBuilder from(String table) {
        this.table = table;
        return this;
    }

    public SelectQueryBuilder where(String field, String operator, String value) {
        this.whereClauses.add(new WhereClause(field, operator, value));
        return this;
    }

    public QueryHolder build(){

        String query = "SELECT "+fields+" FROM " + table;
        List<String> values = new ArrayList<>();
        if(whereClauses.size() > 0){
        }
        for(var i = 0; i<whereClauses.size(); i++){
            var wq = whereClauses.get(i);
            query += (i == 0) ? " WHERE " : " AND ";
            values.add(wq.value());
            query += wq.field() + " " + wq.operator() + " ? ";
        }
        return new QueryHolder(query, values);
    }
}

class InsertQueryBuilder {
    private String table;
    private List<String> fields = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    public static InsertQueryBuilder create(String table){
        return new InsertQueryBuilder(table);
    }
    private InsertQueryBuilder(String table){
        this.table = table;
    }
    public InsertQueryBuilder with(String field, String value){
        fields.add(field);
        values.add(value);
        return this;
    }
    public QueryHolder build(){
        String query = "INSERT INTO " +table+
         " ("+ String.join(", ", fields) + ") "+
         "VALUES (" + fields.stream().map(s -> "?").collect(Collectors.joining(", "));
        
        return new QueryHolder(query, values);
    }
}

record QueryHolder(String query, List<String> parameters){};
record WhereClause(String field, String operator, String value){};