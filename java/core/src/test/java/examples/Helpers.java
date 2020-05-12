package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Helpers {
  /** Helper that stores a {@link Properties} object into a {@link File} */
  public static void storePropertyInfile(String key, String value, File outputFile) {
    Properties properties = new Properties();
    properties.setProperty(key, value);
    try {
      properties.store(new FileOutputStream(outputFile), "test prop file");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
