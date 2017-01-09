package mpstyle.jcache;

import java.sql.SQLException;

public class CacheBuilder {
  /**
   * Returns a new instance of {@link H2Cache}
   *
   * @return
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public static Cache getH2Cache() throws SQLException, ClassNotFoundException {
    return new H2Cache();
  }

  /**
   * Returns a new instance of {@link SqliteCache}
   *
   * @return
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public static Cache getSqliteCache() throws SQLException, ClassNotFoundException {
    return new SqliteCache();
  }
}
