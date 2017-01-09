package mpstyle.jcache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Lazy and naive cache using in memory SQLite database
 */
public class SqliteCache implements Cache {
  private final static String INSERT_SQL = "INSERT INTO jcache(`key`, ttl, creation_timestamp, `value`) VALUES(?, ?, ?, ?)";
  private final static String SELECT_SQL = "SELECT `value`, `key` FROM jcache WHERE `key` = ? AND ttl + creation_timestamp > ?";
  private final static String DELETE_SQL = "DELETE FROM jcache";
  private final static String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS jcache(key TEXT PRIMARY KEY, ttl INT, creation_timestamp INT, value TEXT)";

  private final Connection connection;

  public SqliteCache() throws SQLException, ClassNotFoundException {
    connection = DriverManager.getConnection("jdbc:sqlite::memory:");

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
    return get(key) != null;
  }

  /**
   * Deletes a record from cache with key <i>key</i>.
   *
   * @param key The key of the item to delete
   * @return true if the item will be deleted, otherwise false.
   */
  public boolean delete(String key) {
    try {
      PreparedStatement deleteStatement = connection.prepareStatement(
          DELETE_SQL + " WHERE key = ?");
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
}
