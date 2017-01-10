package mpstyle.jcache;

import java.sql.SQLException;
import mpstyle.jcache.client.H2Cache;
import mpstyle.jcache.client.MysqlCache;
import mpstyle.jcache.client.SqliteCache;

/**
 * Utility class to create, using static methods, un instance of {@link Cache} for the supported database.
 */
public class CacheBuilder {
  /**
   * Returns a new instance of {@link H2Cache}
   *
   * @return A new instance of {@link H2Cache}
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public static Cache getH2Cache() throws SQLException, ClassNotFoundException {
    return new H2Cache();
  }

  /**
   * Returns a new instance of {@link SqliteCache}
   *
   * @return A new instance of {@link SqliteCache}
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public static Cache getSqliteCache() throws SQLException, ClassNotFoundException {
    return new SqliteCache();
  }

  /**
   * Returns a new instance of {@link MysqlCache}
   *
   * @param username The username to access to the database
   * @param password The password to access to the database
   * @param host The host of the database
   * @param port The port of the database
   * @param databaseName The database name
   * @return A new instance of {@link MysqlCache}
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public static Cache getMysqlCache(String username, String password, String host, String port, String databaseName) throws SQLException, ClassNotFoundException {
    return new MysqlCache(username, password, host, port, databaseName);
  }

  /**
   * Returns a new instance of {@link MysqlCache}
   *
   * @param username The username to access to the database
   * @param password The password to access to the database
   * @param host The host of the database
   * @param databaseName The database name
   * @return A new instance of {@link MysqlCache}
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public static Cache getMysqlCache(String username, String password, String host, String databaseName) throws SQLException, ClassNotFoundException {
    return new MysqlCache(username, password, host, databaseName);
  }
}
