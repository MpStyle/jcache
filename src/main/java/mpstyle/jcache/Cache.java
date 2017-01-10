package mpstyle.jcache;

import mpstyle.jcache.entity.CacheItem;

/**
 * Lazy and naive cache.
 */
public interface Cache {

  /**
   * Adds or updates the item in the cache.
   *
   * @param key The key of the item to add or to update.
   * @param value The value of the item to add or to update.
   * @return Returns true if the cache will be cleaned without problems, otherwise false.
   */
  boolean add(String key, String value);

  /**
   * Adds or updates the item in the cache.
   *
   * @param item The {@link} to insert or to update.
   * @return Returns true if the cache will be cleaned without problems, otherwise false.
   */
  boolean add(CacheItem item);

  /**
   * Checks if a <i>key</i> is in cache.
   *
   * @param key The key of the item to search in the cache
   * @return true if item linked to the key exists, otherwise false.
   */
  boolean exists(String key);

  /**
   * Deletes a record from cache with key <i>key</i>.
   *
   * @param key The key of the item to delete
   * @return true if the item will be deleted, otherwise false.
   */
  boolean delete(String key);

  /**
   * Returns the value of the item with <i>key</i> if it exists, otherwise null.
   *
   * @param key The key of the item to retrieve.
   * @return Returns the value of the item with <i>key</i> if it exists, otherwise null.
   */
  String get(String key);

  /**
   * Returns null if the key doesn't exist, otherwise returns the value linked to the <i>key</i> and remove it the item
   * from cache.
   *
   * @param key The key of the item to retrieve.
   * @return Returns the value of the item with <i>key</i> if it exists, otherwise null.
   */
  String pop(String key);

  /**
   * Clears all item in the cache.
   *
   * @return Returns true if the cache will be cleaned without problems, otherwise false.
   */
  boolean clear();

  /**
   * Drop the connection to the cache.<br>
   * Can remove the database for in-memory database or can remove only the cache table for the other.
   * 
   * @return
   */
  boolean close();
}
