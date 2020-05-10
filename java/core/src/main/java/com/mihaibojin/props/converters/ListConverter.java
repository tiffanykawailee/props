package com.mihaibojin.props.converters;

public interface ListConverter {
  /**
   * Convenience method allowing the use of a custom separator string in subclasses; defaults to ","
   */
  default String separator() {
    return ",";
  }
}
