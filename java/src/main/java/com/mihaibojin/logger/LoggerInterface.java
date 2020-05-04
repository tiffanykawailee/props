package com.mihaibojin.logger;

public interface LoggerInterface {
  void trace(String format, Object... arguments);

  void debug(String format, Object... arguments);

  void info(String format, Object... arguments);

  void warn(String format, Object... arguments);

  void error(String format, Object... arguments);

  void error(String msg, Throwable t);
}
