package mpstyle.jcache;

import com.sun.istack.internal.NotNull;

public class CacheItem {
  @NotNull
  private String key;
  private String value;
  private int ttl;

  @NotNull
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public int getTtl() {
    return ttl;
  }

  public void setTtl(int ttl) {
    this.ttl = ttl;
  }
}
