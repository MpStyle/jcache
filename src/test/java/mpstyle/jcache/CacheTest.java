package mpstyle.jcache;

import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;

public class CacheTest {
  @Test
  public void addAndGet1() throws Exception {
    Cache cache = new Cache();
    Assert.assertTrue(cache.add("a", "b"));
    Assert.assertTrue(cache.exists("a"));
    Assert.assertEquals("b", cache.get("a"));
  }

  @Test
  public void addAndGet2() throws Exception {
    Cache cache = new Cache();
    CacheItem item = new CacheItem();

    item.setKey("a");
    item.setTtl(3);
    item.setValue("b");

    cache.add(item);

    Assert.assertEquals("b", cache.get("a"));
    Assert.assertTrue(cache.exists("a"));

    Thread.sleep(4);

    Assert.assertTrue(cache.get("a") == null);
    Assert.assertFalse(cache.exists("a"));
  }

  @Test
  public void clear() throws Exception {
    Cache cache = new Cache();
    cache.add("c", "d");
    Assert.assertEquals("d", cache.get("c"));
    Assert.assertTrue(cache.exists("c"));
    Assert.assertTrue(cache.clear());
    Assert.assertTrue(cache.get("c") == null);
    Assert.assertFalse(cache.exists("c"));
  }

  @Test
  public void pop() throws SQLException, ClassNotFoundException {
    Cache cache = new Cache();
    cache.add("a", "b");
    Assert.assertEquals("b", cache.get("a"));
    Assert.assertTrue(cache.exists("a"));
    Assert.assertEquals("b", cache.pop("a"));
    Assert.assertTrue(cache.get("a") == null);
    Assert.assertFalse(cache.exists("a"));
  }
}