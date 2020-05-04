package com.mihaibojin.logger;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoggerBridge {
  private static final LoggerBridge instance = new LoggerBridge();

  private LoggerInterface logger;

  static {
    instance.setLogger(new DummyLogger());
  }

  public static LoggerInterface getLogger() {
    return instance.logger;
  }

  public void setLogger(LoggerInterface logger) {
    LoggerBridge.instance.logger = logger;
  }

  private static String replaceSlf4jTokens(String format) {
    return format.replaceAll("\\{}", "%s");
  }

  public static class DummyLogger implements LoggerInterface {
    @Override
    public void trace(String format, Object... arguments) {
      System.out.println(String.format("[TRACE] " + replaceSlf4jTokens(format), arguments));
    }

    @Override
    public void debug(String format, Object... arguments) {
      System.out.println(String.format("[DEBUG] " + replaceSlf4jTokens(format), arguments));
    }

    @Override
    public void info(String format, Object... arguments) {
      System.out.println(String.format("[INFO] " + replaceSlf4jTokens(format), arguments));
    }

    @Override
    public void warn(String format, Object... arguments) {
      System.out.println(String.format("[WARN] " + replaceSlf4jTokens(format), arguments));
    }

    @Override
    public void error(String format, Object... arguments) {
      System.out.println(String.format("[ERROR] " + replaceSlf4jTokens(format), arguments));
    }

    @Override
    public void error(String msg, Throwable t) {
      String stacktrace =
          Stream.of(t.getStackTrace())
              .map(StackTraceElement::toString)
              .collect(Collectors.joining("\n"));
      System.out.println(
          String.format(
              "[ERROR] " + msg, t.getMessage() + "\n" + t.getCause() + "\n" + stacktrace));
    }
  }
}
