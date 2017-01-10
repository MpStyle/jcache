package mpstyle.jcache;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MysqlCacheTest {
  private Cache cache;

  @Before
  public void setUp() throws Exception {
    cache = CacheBuilder.getMysqlCache("test", "", "mysql", "jcache");
  }

  @After
  public void tearDown() throws Exception {
    cache.clear();
  }

  @Test
  public void addAndGet1() throws Exception {
    Assert.assertTrue(cache.add("a", "b"));
    Assert.assertTrue(cache.exists("a"));
    Assert.assertEquals("b", cache.get("a"));
  }

  @Test
  public void addAndGet2() throws Exception {
    CacheItem item = new CacheItem();

    item.setKey("a");
    item.setTtl(30);
    item.setValue("b");

    cache.add(item);

    Assert.assertEquals("b", cache.get("a"));
    Assert.assertTrue(cache.exists("a"));

    Thread.sleep(40);

    Assert.assertTrue(cache.get("a") == null);
    Assert.assertFalse(cache.exists("a"));
  }

  @Test
  public void clear() throws Exception {
    cache.add("c", "d");
    Assert.assertEquals("d", cache.get("c"));
    Assert.assertTrue(cache.exists("c"));
    Assert.assertTrue(cache.clear());
    Assert.assertTrue(cache.get("c") == null);
    Assert.assertFalse(cache.exists("c"));
  }

  @Test
  public void pop() throws SQLException, ClassNotFoundException {
    cache.add("a", "b");
    Assert.assertEquals("b", cache.get("a"));
    Assert.assertTrue(cache.exists("a"));
    Assert.assertEquals("b", cache.pop("a"));
    Assert.assertTrue(cache.get("a") == null);
    Assert.assertFalse(cache.exists("a"));
  }
}