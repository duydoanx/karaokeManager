package com.karaoke.manager.exception;

public class BearerTokenException extends RuntimeException {
  public BearerTokenException(String message) {
    super(message);
  }

  public BearerTokenException(String message, Throwable cause) {
    super(message, cause);
  }

  public BearerTokenException(Throwable cause) {
    super(cause);
  }

  public BearerTokenException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
