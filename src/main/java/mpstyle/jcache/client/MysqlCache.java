package mpstyle.jcache.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import mpstyle.jcache.Cache;
import mpstyle.jcache.entity.CacheItem;

/**
 * Lazy and naive cache using in memory MySQL database.<br>
 * The max key size is 255 characters.
 */
public class MysqlCache implements Cache {
  private final static int KEY_MAX_LENGTH = 255;
  private final static String INSERT_SQL = "INSERT INTO jcache(`key`, ttl, creation_timestamp, `value`) VALUES(?, ?, ?, ?)";
  private final static String SELECT_SQL = "SELECT `value`, `key` FROM jcache WHERE `key` = ? AND ttl + creation_timestamp > ?";
  private final static String DELETE_SQL = "DELETE FROM jcache";
  private final static String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS jcache(`key` varchar("
      + String.valueOf(KEY_MAX_LENGTH) + ") PRIMARY KEY NOT NULL, ttl BIGINT, creation_timestamp BIGINT, `value` TEXT)";
  private final static String DROP_SQL = "DROP TABLE jcache";

  private Connection connection;

  /**
   * Create a mysql cache object using the parameters for the connection.
   *
   * @param username The username to access to the database
   * @param password The password to access to the database
   * @param host The host of the database
   * @param port The port of the database
   * @param databaseName The database name
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public MysqlCache(String username, String password, String host, String port, String databaseName)
      throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.jdbc.Driver");
    String DB_URL = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
    connection = DriverManager.getConnection(DB_URL, username, password);

    createCacheTable();
  }

  /**
   * Create a mysql cache object using the parameters for the connection.
   *
   * @param username The username to access to the database
   * @param password The password to access to the database
   * @param host The host of the database
   * @param databaseName The database name
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public MysqlCache(String username, String password, String host, String databaseName) throws ClassNotFoundException,
      SQLException {
    Class.forName("com.mysql.jdbc.Driver");
    String DB_URL = "jdbc:mysql://" + host + "/" + databaseName;
    connection = DriverManager.getConnection(DB_URL, username, password);

    createCacheTable();
  }

  private void createCacheTable() throws SQLException {
    Statement statement = connection.createStatement();
    statement.executeUpdate(CREATE_TABLE_SQL);
  }

  /**
   * Adds or updates the item in the cache.
   *
   * @param key The key of the item to add or to update.
   * @param value The value of the item to add or to update.
   * @return Returns true if the cache will be cleaned without problems, otherwise false.
   */
  public boolean add(String key, String value) {
    if (!validateKey(key)) {
      return false;
    }

    CacheItem cacheItem = new CacheItem();

    cacheItem.setKey(key);
    cacheItem.setTtl(Integer.MAX_VALUE);
    cacheItem.setValue(value);

    return add(cacheItem);
  }

  /**
   * Adds or updates the item in the cache.
   *
   * @param item The {@link} to insert or to update.
   * @return Returns true if the cache will be cleaned without problems, otherwise false.
   */
  public boolean add(CacheItem item) {
    if (!validateKey(item.getKey())) {
      return false;
    }

    try {
      if (!delete(item.getKey())) {
        return false;
      }

      PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
      statement.setString(1, item.getKey());
      statement.setInt(2, item.getTtl());
      statement.setLong(3, System.currentTimeMillis());
      statement.setString(4, item.getValue());

      statement.execute();
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return false;
  }

  /**
   * Checks if a <i>key</i> is in cache.
   *
   * @param key The key of the item to search in the cache
   * @return true if item linked to the key exists, otherwise false.
   */
  public boolean exists(String key) {
    if (!validateKey(key)) {
      return false;
    }

    return get(key) != null;
  }

  /**
   * Deletes a record from cache with key <i>key</i>.
   *
   * @param key The key of the item to delete
   * @return true if the item will be deleted, otherwise false.
   */
  public boolean delete(String key) {
    if (!validateKey(key)) {
      return false;
    }

    try {
      PreparedStatement deleteStatement = connection.prepareStatement(DELETE_SQL + " WHERE `key` = ?");
      deleteStatement.setString(1, key);
      deleteStatement.executeUpdate();

      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return false;
  }

  /**
   * Returns the value of the item with <i>key</i> if it exists, otherwise null.
   *
   * @param key The key of the item to retrieve.
   * @return Returns the value of the item with <i>key</i> if it exists, otherwise null.
   */
  public String get(String key) {
    if (!validateKey(key)) {
      return null;
    }

    try {
      String value = null;
      PreparedStatement statement = connection.prepareStatement(SELECT_SQL);
      statement.setString(1, key);
      statement.setLong(2, System.currentTimeMillis());

      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        value = rs.getString(1);
      }

      return value;

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  }

  /**
   * Returns null if the key doesn't exist, otherwise returns the value linked to the <i>key</i> and remove it the item
   * from cache.
   *
   * @param key The key of the item to retrieve.
   * @return Returns the value of the item with <i>key</i> if it exists, otherwise null.
   */
  public String pop(String key) {
    if (!validateKey(key)) {
      return null;
    }

    if (!exists(key)) {
      return null;
    }

    String value = get(key);

    if (!delete(key)) {
      return null;
    }

    return value;
  }

  /**
   * Clears all item in the cache.
   *
   * @return Returns true if the cache will be cleaned without problems, otherwise false.
   */
  public boolean clear() {
    try {
      connection.createStatement().executeUpdate(DELETE_SQL);
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return false;
  }

  public boolean close() {
    try {
      PreparedStatement deleteStatement = connection.prepareStatement(DROP_SQL);
      deleteStatement.executeUpdate();

      connection.close();
      connection=null;

      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  private boolean validateKey(String key) {
    return key.length() <= KEY_MAX_LENGTH;
  }
}
