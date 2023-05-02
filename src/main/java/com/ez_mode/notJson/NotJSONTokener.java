package com.ez_mode.notJson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/*
Public Domain.
 */

/**
 * A JSONTokener takes a source string and extracts characters and tokens from it. It is used by the
 * JSONObject and JSONArray constructors to parse JSON source strings.
 *
 * @author JSON.org
 * @version 2014-05-03
 */
public class NotJSONTokener {
  /** current read character position on the current line. */
  private long character;

  /** flag to indicate if the end of the input has been found. */
  private boolean eof;

  /** current read index of the input. */
  private long index;

  /** current line of the input. */
  private long line;

  /** previous character read from the input. */
  private char previous;

  /** Reader for the input. */
  private final Reader reader;

  /** flag to indicate that a previous character was requested. */
  private boolean usePrevious;

  /** the number of characters read in the previous line. */
  private long characterPreviousLine;

  /**
   * Construct a JSONTokener from a Reader. The caller must close the Reader.
   *
   * @param reader A reader.
   */
  public NotJSONTokener(Reader reader) {
    this.reader = reader.markSupported() ? reader : new BufferedReader(reader);
    this.eof = false;
    this.usePrevious = false;
    this.previous = 0;
    this.index = 0;
    this.character = 1;
    this.characterPreviousLine = 0;
    this.line = 1;
  }

  /**
   * Construct a JSONTokener from an InputStream. The caller must close the input stream.
   *
   * @param inputStream The source.
   */
  public NotJSONTokener(InputStream inputStream) {
    this(new InputStreamReader(inputStream));
  }

  /**
   * Construct a JSONTokener from a string.
   *
   * @param s A source string.
   */
  public NotJSONTokener(String s) {
    this(new StringReader(s));
  }

  /**
   * Back up one character. This provides a sort of lookahead capability, so that you can test for a
   * digit or letter before attempting to parse the next number or identifier.
   *
   * @throws NotJSONException Thrown if trying to step back more than 1 step or if already at the
   *     start of the string
   */
  public void back() throws NotJSONException {
    if (this.usePrevious || this.index <= 0) {
      throw new NotJSONException("Stepping back two steps is not supported");
    }
    this.decrementIndexes();
    this.usePrevious = true;
    this.eof = false;
  }

  /** Decrements the indexes for the {@link #back()} method based on the previous character read. */
  private void decrementIndexes() {
    this.index--;
    if (this.previous == '\r' || this.previous == '\n') {
      this.line--;
      this.character = this.characterPreviousLine;
    } else if (this.character > 0) {
      this.character--;
    }
  }

  /**
   * Get the hex value of a character (base16).
   *
   * @param c A character between '0' and '9' or between 'A' and 'F' or between 'a' and 'f'.
   * @return An int between 0 and 15, or -1 if c was not a hex digit.
   */
  public static int dehexchar(char c) {
    if (c >= '0' && c <= '9') {
      return c - '0';
    }
    if (c >= 'A' && c <= 'F') {
      return c - ('A' - 10);
    }
    if (c >= 'a' && c <= 'f') {
      return c - ('a' - 10);
    }
    return -1;
  }

  /**
   * Checks if the end of the input has been reached.
   *
   * @return true if at the end of the file and we didn't step back
   */
  public boolean end() {
    return this.eof && !this.usePrevious;
  }

  /**
   * Determine if the source string still contains characters that next() can consume.
   *
   * @return true if not yet at the end of the source.
   * @throws NotJSONException thrown if there is an error stepping forward or backward while
   *     checking for more data.
   */
  public boolean more() throws NotJSONException {
    if (this.usePrevious) {
      return true;
    }
    try {
      this.reader.mark(1);
    } catch (IOException e) {
      throw new NotJSONException("Unable to preserve stream position", e);
    }
    try {
      // -1 is EOF, but next() can not consume the null character '\0'
      if (this.reader.read() <= 0) {
        this.eof = true;
        return false;
      }
      this.reader.reset();
    } catch (IOException e) {
      throw new NotJSONException("Unable to read the next character from the stream", e);
    }
    return true;
  }

  /**
   * Get the next character in the source string.
   *
   * @return The next character, or 0 if past the end of the source string.
   * @throws NotJSONException Thrown if there is an error reading the source string.
   */
  public char next() throws NotJSONException {
    int c;
    if (this.usePrevious) {
      this.usePrevious = false;
      c = this.previous;
    } else {
      try {
        c = this.reader.read();
      } catch (IOException exception) {
        throw new NotJSONException(exception);
      }
    }
    if (c <= 0) { // End of stream
      this.eof = true;
      return 0;
    }
    this.incrementIndexes(c);
    this.previous = (char) c;
    return this.previous;
  }

  /**
   * Get the last character read from the input or '\0' if nothing has been read yet.
   *
   * @return the last character read from the input.
   */
  protected char getPrevious() {
    return this.previous;
  }

  /**
   * Increments the internal indexes according to the previous character read and the character
   * passed as the current character.
   *
   * @param c the current character read.
   */
  private void incrementIndexes(int c) {
    if (c > 0) {
      this.index++;
      if (c == '\r') {
        this.line++;
        this.characterPreviousLine = this.character;
        this.character = 0;
      } else if (c == '\n') {
        if (this.previous != '\r') {
          this.line++;
          this.characterPreviousLine = this.character;
        }
        this.character = 0;
      } else {
        this.character++;
      }
    }
  }

  /**
   * Consume the next character, and check that it matches a specified character.
   *
   * @param c The character to match.
   * @return The character.
   * @throws NotJSONException if the character does not match.
   */
  public char next(char c) throws NotJSONException {
    char n = this.next();
    if (n != c) {
      if (n > 0) {
        throw this.syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
      }
      throw this.syntaxError("Expected '" + c + "' and instead saw ''");
    }
    return n;
  }

  /**
   * Get the next n characters.
   *
   * @param n The number of characters to take.
   * @return A string of n characters.
   * @throws NotJSONException Substring bounds error if there are not n characters remaining in the
   *     source string.
   */
  public String next(int n) throws NotJSONException {
    if (n == 0) {
      return "";
    }

    char[] chars = new char[n];
    int pos = 0;

    while (pos < n) {
      chars[pos] = this.next();
      if (this.end()) {
        throw this.syntaxError("Substring bounds error");
      }
      pos += 1;
    }
    return new String(chars);
  }

  /**
   * Get the next char in the string, skipping whitespace.
   *
   * @throws NotJSONException Thrown if there is an error reading the source string.
   * @return A character, or 0 if there are no more characters.
   */
  public char nextClean() throws NotJSONException {
    for (; ; ) {
      char c = this.next();
      if (c == 0 || c > ' ') {
        return c;
      }
    }
  }

  /**
   * Return the characters up to the next close quote character. Backslash processing is done. The
   * formal JSON format does not allow strings in single quotes, but an implementation is allowed to
   * accept them.
   *
   * @param quote The quoting character, either <code>"</code>&nbsp;<small>(double quote)</small> or
   *     <code>'</code>&nbsp;<small>(single quote)</small>.
   * @return A String.
   * @throws NotJSONException Unterminated string.
   */
  public String nextString(char quote) throws NotJSONException {
    char c;
    StringBuilder sb = new StringBuilder();
    for (; ; ) {
      c = this.next();
      switch (c) {
        case 0:
        case '\n':
        case '\r':
          throw this.syntaxError("Unterminated string");
        case '\\':
          c = this.next();
          switch (c) {
            case 'b':
              sb.append('\b');
              break;
            case 't':
              sb.append('\t');
              break;
            case 'n':
              sb.append('\n');
              break;
            case 'f':
              sb.append('\f');
              break;
            case 'r':
              sb.append('\r');
              break;
            case 'u':
              try {
                sb.append((char) Integer.parseInt(this.next(4), 16));
              } catch (NumberFormatException e) {
                throw this.syntaxError("Illegal escape.", e);
              }
              break;
            case '"':
            case '\'':
            case '\\':
            case '/':
              sb.append(c);
              break;
            default:
              throw this.syntaxError("Illegal escape.");
          }
          break;
        default:
          if (c == quote) {
            return sb.toString();
          }
          sb.append(c);
      }
    }
  }

  /**
   * Get the text up but not including the specified character or the end of line, whichever comes
   * first.
   *
   * @param delimiter A delimiter character.
   * @return A string.
   * @throws NotJSONException Thrown if there is an error while searching for the delimiter
   */
  public String nextTo(char delimiter) throws NotJSONException {
    StringBuilder sb = new StringBuilder();
    for (; ; ) {
      char c = this.next();
      if (c == delimiter || c == 0 || c == '\n' || c == '\r') {
        if (c != 0) {
          this.back();
        }
        return sb.toString().trim();
      }
      sb.append(c);
    }
  }

  /**
   * Get the text up but not including one of the specified delimiter characters or the end of line,
   * whichever comes first.
   *
   * @param delimiters A set of delimiter characters.
   * @return A string, trimmed.
   * @throws NotJSONException Thrown if there is an error while searching for the delimiter
   */
  public String nextTo(String delimiters) throws NotJSONException {
    char c;
    StringBuilder sb = new StringBuilder();
    for (; ; ) {
      c = this.next();
      if (delimiters.indexOf(c) >= 0 || c == 0 || c == '\n' || c == '\r') {
        if (c != 0) {
          this.back();
        }
        return sb.toString().trim();
      }
      sb.append(c);
    }
  }

  /**
   * Get the next value. The value can be a Boolean, Double, Integer, JSONArray, JSONObject, Long,
   * or String, or the JSONObject.NULL object.
   *
   * @throws NotJSONException If syntax error.
   * @return An object.
   */
  public Object nextValue() throws NotJSONException {
    char c = this.nextClean();
    String string;

    switch (c) {
      case '"':
      case '\'':
        return this.nextString(c);
      case '{':
        this.back();
        try {
          return new NotJSONObject(this);
        } catch (StackOverflowError e) {
          throw new NotJSONException("JSON Array or Object depth too large to process.", e);
        }
      case '[':
        this.back();
        try {
          return new NotJSONArray(this);
        } catch (StackOverflowError e) {
          throw new NotJSONException("JSON Array or Object depth too large to process.", e);
        }
    }

    /*
     * Handle unquoted text. This could be the values true, false, or
     * null, or it can be a number. An implementation (such as this one)
     * is allowed to also accept non-standard forms.
     *
     * Accumulate characters until we reach the end of the text or a
     * formatting character.
     */

    StringBuilder sb = new StringBuilder();
    while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
      sb.append(c);
      c = this.next();
    }
    if (!this.eof) {
      this.back();
    }

    string = sb.toString().trim();
    if ("".equals(string)) {
      throw this.syntaxError("Missing value");
    }
    return NotJSONObject.stringToValue(string);
  }

  /**
   * Skip characters until the next character is the requested character. If the requested character
   * is not found, no characters are skipped.
   *
   * @param to A character to skip to.
   * @return The requested character, or zero if the requested character is not found.
   * @throws NotJSONException Thrown if there is an error while searching for the to character
   */
  public char skipTo(char to) throws NotJSONException {
    char c;
    try {
      long startIndex = this.index;
      long startCharacter = this.character;
      long startLine = this.line;
      this.reader.mark(1000000);
      do {
        c = this.next();
        if (c == 0) {
          // in some readers, reset() may throw an exception if
          // the remaining portion of the input is greater than
          // the mark size (1,000,000 above).
          this.reader.reset();
          this.index = startIndex;
          this.character = startCharacter;
          this.line = startLine;
          return 0;
        }
      } while (c != to);
      this.reader.mark(1);
    } catch (IOException exception) {
      throw new NotJSONException(exception);
    }
    this.back();
    return c;
  }

  /**
   * Make a NotJSONException to signal a syntax error.
   *
   * @param message The error message.
   * @return A NotJSONException object, suitable for throwing
   */
  public NotJSONException syntaxError(String message) {
    return new NotJSONException(message + this.toString());
  }

  /**
   * Make a NotJSONException to signal a syntax error.
   *
   * @param message The error message.
   * @param causedBy The throwable that caused the error.
   * @return A NotJSONException object, suitable for throwing
   */
  public NotJSONException syntaxError(String message, Throwable causedBy) {
    return new NotJSONException(message + this.toString(), causedBy);
  }

  /**
   * Make a printable string of this JSONTokener.
   *
   * @return " at {index} [character {character} line {line}]"
   */
  @Override
  public String toString() {
    return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
  }
}
