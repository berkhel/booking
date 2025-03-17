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
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class MySqlDatabase {

    private String url;
    private String user = "root";
    private String password = "test";


    public MySqlDatabase(Integer port) throws SQLException{
        this.url ="jdbc:mysql://localhost:" + port + "/booking";
    }


    private Connection connection() throws SQLException{
        return DriverManager.getConnection(url, user, password);
    }



    public String query(QueryHolder queryHolder, Function<ResultSet, String> resultSetHandler) throws SQLException {
        // System.out.println("QUERY:"+queryHolder.query());
        // System.out.println("PARAM:"+queryHolder.parameters());
        String result = "";
        try (Connection con = connection();
                PreparedStatement stmt = con.prepareStatement(queryHolder.query())) {
                List<String> parameters = queryHolder.parameters();
                for(var i = 0; i < parameters.size(); i++){
                    stmt.setString(i+1, parameters.get(i));
                }
            try (ResultSet resultSet = stmt.executeQuery()) {
                result = resultSetHandler.apply(resultSet);
            } catch(SQLException e ){
                throw e;
            }
        }
        return result;
    }

    public String query(QueryHolder queryHolder) throws SQLException {
        return query(queryHolder, rs -> "");
    }

    public Integer update(QueryHolder queryHolder) throws SQLException {
        Integer result;
        try (Connection con = connection();
                PreparedStatement stmt = con.prepareStatement(queryHolder.query())) {
                List<String> parameters = queryHolder.parameters();
                for(var i = 0; i < parameters.size(); i++){
                    stmt.setString(i+1, parameters.get(i));
                }
                result = stmt.executeUpdate();
        }
        return result;
    }

    public Integer update(String command) throws SQLException {
        return update(new QueryHolder(command));
    }


    public void deleteAllRecords() {
        List<String> tables = List.of("ticket_entry","ticket","attendee","purchase","event","account");

        tables.forEach(table -> {
            try {
                update("DELETE FROM "+table);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });


        // check
        Integer count = tables.stream().mapToInt( table -> {
            try {
              return Integer.parseInt(
                    query(new QueryHolder("SELECT COUNT(*) AS total_rows FROM "+ table), rs -> {
                try {
                    return rs.next() 
                        ? rs.getString("total_rows")
                        : "0";
                } catch (SQLException ex) {
                    return "0";
                }
            } ));
            } catch (SQLException e) {
                return 0;
            }
        }).sum();

        assert count == 0 : "Deleting all records failed!";
        

    }



    public void createEvent(String eventId, Integer maxSeats) throws SQLException {
        Integer insertedAccount = update(InsertQueryBuilder.create("account")
                .with("id", eventId)
                .build());

        assert insertedAccount == 1: "Account not created!";

        Integer insertedRows = update(InsertQueryBuilder.create("event")
                .with("id", eventId)
                .with("account_id", eventId)
                .with("max_seats", maxSeats + "")
                .build());
        
        

        assert insertedRows == 1 : "Event not created!";


        Integer insertedTickets = 0;
        for(var i = 0; i < maxSeats; i++){
           insertedTickets += update(InsertQueryBuilder.create("ticket")
            .with("id",UUID.randomUUID().toString())
            .with("event",eventId)
            .with("account",eventId)
            .build());
        }

        assert insertedTickets == maxSeats : "Tickets not created!";

    }


    public String lookupId(String id, String table) throws SQLException {
        return lookup(table, "id", id);
    }

    public SelectQuery select(String columnName){
        return new SelectQuery(columnName);
    }

    public class SelectQuery {
        
        private SelectQueryBuilder builder; 
        private String columnName;

        public SelectQuery(String columnName){
            this.columnName = columnName;
            builder = SelectQueryBuilder.select(columnName);
        }

        public SelectQuery from(String table){
            builder.from(table);
            return this;
        }

        public SelectQuery where(String column, String operator, String value){
            builder.where(column, operator, value);
            return this;
        }
        
        public String query() throws SQLException{
            return MySqlDatabase.this.query(builder.build(), rs -> {
                try {
                    return rs.next() ? rs.getString(columnName) : "";
                } catch (SQLException e) {
                    return "";
                }
            });
        }

    }


    public String lookup(String table, String columnName, String columnValue) throws SQLException {
        return query(SelectQueryBuilder.select(columnName).from(table).where(columnName, "=", columnValue).build(),
                rset -> {
                    try {
                        return rset.next() ? rset.getString(columnName) : "";
                    } catch (SQLException e) {
                        return "";
                    }
                });
    }

    public static ResponseAwareMatcher<Response> isEqualToRecordIdFrom(MySqlDatabase mySqlDatabase, String table){
        return response -> equalTo(mySqlDatabase.lookupId(response.path("id"), table));
    }

    public static ResponseAwareMatcher<Response> existsAsValueIn(MySqlDatabase mySqlDatabase, String table, String column){
        return response -> new BaseMatcher<String>() {

            private String expected;

            @Override
            public void describeTo(Description description) {
                description.appendText(expected);
            }

            @Override
            public boolean matches(Object actual) {
                try {
                this.expected = mySqlDatabase.lookup(table, column, (String) actual);
                    return actual.equals(expected);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
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
         "VALUES (" + fields.stream().map(s -> "?")
            .collect(Collectors.joining(", ")) + ")";
        
        return new QueryHolder(query, values);
    }
}

record QueryHolder(String query, List<String> parameters){
    public QueryHolder(String query){
        this(query, List.of());
    }
};
record WhereClause(String field, String operator, String value){};