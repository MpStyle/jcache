package mpstyle.jcache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionBuilder {
  private final Connection connection;

  public ConnectionBuilder() throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");

    connection = DriverManager.getConnection("jdbc:sqlite::memory:");
    Statement statement = connection.createStatement();
    statement
        .executeUpdate("CREATE TABLE IF NOT EXISTS jcache(key TEXT PRIMARY KEY, ttl INT, creation_timestamp INT, value TEXT)");
  }

  public Connection getConnection() {
    return connection;
  }
}
