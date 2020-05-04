package java.src.main.java.com.mihaibojin.resolvers;

public interface Resolver {
  boolean canAutoUpdate();
  void refresh();
  String get(String key);
}
