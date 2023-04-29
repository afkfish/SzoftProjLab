package com.ez_mode.notJson;

/*
Public Domain.
*/

/**
 * The JSONPointerException is thrown by {@link NotJSONPointer} if an error occurs during evaluating
 * a pointer.
 *
 * @author JSON.org
 * @version 2016-05-13
 */
public class NotJSONPointerException extends NotJSONException {
  private static final long serialVersionUID = 8872944667561856751L;

  public NotJSONPointerException(String message) {
    super(message);
  }

  public NotJSONPointerException(String message, Throwable cause) {
    super(message, cause);
  }
}
