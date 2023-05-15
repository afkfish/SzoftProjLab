package com.ez_mode.notJson;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/*
Public Domain.
*/

/**
 * JSONWriter provides a quick and convenient way of producing JSON text. The texts produced
 * strictly conform to JSON syntax rules. No whitespace is added, so the results are ready for
 * transmission or storage. Each instance of JSONWriter can produce one JSON text.
 *
 * <p>A JSONWriter instance provides a <code>value</code> method for appending values to the text,
 * and a <code>key</code> method for adding keys before values in objects. There are <code>array
 * </code> and <code>endArray</code> methods that make and bound array values, and <code>object
 * </code> and <code>endObject</code> methods which make and bound object values. All of these
 * methods return the JSONWriter instance, permitting a cascade style. For example,
 *
 * <pre>
 * new JSONWriter(myWriter)
 *     .object()
 *         .key("JSON")
 *         .value("Hello, World!")
 *     .endObject();</pre>
 *
 * which writes
 *
 * <pre>
 * {"JSON":"Hello, World!"}</pre>
 *
 * <p>The first method called must be <code>array</code> or <code>object</code>. There are no
 * methods for adding commas or colons. JSONWriter adds them for you. Objects and arrays can be
 * nested up to 200 levels deep.
 *
 * <p>This can sometimes be easier than using a NotJSONObject to build a string.
 *
 * @author JSON.org
 * @version 2016-08-08
 */
public class NotJSONWriter {
  private static final int maxdepth = 200;

  /** The comma flag determines if a comma should be output before the next value. */
  private boolean comma;

  /** The current mode. Values: 'a' (array), 'd' (done), 'i' (initial), 'k' (key), 'o' (object). */
  protected char mode;

  /** The object/array stack. */
  private final NotJSONObject stack[];

  /** The stack top index. A value of 0 indicates that the stack is empty. */
  private int top;

  /** The writer that will receive the output. */
  protected Appendable writer;

  /**
   * Make a fresh JSONWriter. It can be used to build one JSON text.
   *
   * @param w an appendable object
   */
  public NotJSONWriter(Appendable w) {
    this.comma = false;
    this.mode = 'i';
    this.stack = new NotJSONObject[maxdepth];
    this.top = 0;
    this.writer = w;
  }

  /**
   * Append a value.
   *
   * @param string A string value.
   * @return this
   * @throws NotJSONException If the value is out of sequence.
   */
  private NotJSONWriter append(String string) throws NotJSONException {
    if (string == null) {
      throw new NotJSONException("Null pointer");
    }
    if (this.mode == 'o' || this.mode == 'a') {
      try {
        if (this.comma && this.mode == 'a') {
          this.writer.append(',');
        }
        this.writer.append(string);
      } catch (IOException e) {
        // Android as of API 25 does not support this exception constructor
        // however we won't worry about it. If an exception is happening here
        // it will just throw a "Method not found" exception instead.
        throw new NotJSONException(e);
      }
      if (this.mode == 'o') {
        this.mode = 'k';
      }
      this.comma = true;
      return this;
    }
    throw new NotJSONException("Value out of sequence.");
  }

  /**
   * Begin appending a new array. All values until the balancing <code>endArray</code> will be
   * appended to this array. The <code>endArray</code> method must be called to mark the array's
   * end.
   *
   * @return this
   * @throws NotJSONException If the nesting is too deep, or if the object is started in the wrong
   *     place (for example as a key or after the end of the outermost array or object).
   */
  public NotJSONWriter array() throws NotJSONException {
    if (this.mode == 'i' || this.mode == 'o' || this.mode == 'a') {
      this.push(null);
      this.append("[");
      this.comma = false;
      return this;
    }
    throw new NotJSONException("Misplaced array.");
  }

  /**
   * End something.
   *
   * @param m Mode
   * @param c Closing character
   * @return this
   * @throws NotJSONException If unbalanced.
   */
  private NotJSONWriter end(char m, char c) throws NotJSONException {
    if (this.mode != m) {
      throw new NotJSONException(m == 'a' ? "Misplaced endArray." : "Misplaced endObject.");
    }
    this.pop(m);
    try {
      this.writer.append(c);
    } catch (IOException e) {
      // Android as of API 25 does not support this exception constructor
      // however we won't worry about it. If an exception is happening here
      // it will just throw a "Method not found" exception instead.
      throw new NotJSONException(e);
    }
    this.comma = true;
    return this;
  }

  /**
   * End an array. This method most be called to balance calls to <code>array</code>.
   *
   * @return this
   * @throws NotJSONException If incorrectly nested.
   */
  public NotJSONWriter endArray() throws NotJSONException {
    return this.end('a', ']');
  }

  /**
   * End an object. This method most be called to balance calls to <code>object</code>.
   *
   * @return this
   * @throws NotJSONException If incorrectly nested.
   */
  public NotJSONWriter endObject() throws NotJSONException {
    return this.end('k', '}');
  }

  /**
   * Append a key. The key will be associated with the next value. In an object, every value must be
   * preceded by a key.
   *
   * @param string A key string.
   * @return this
   * @throws NotJSONException If the key is out of place. For example, keys do not belong in arrays
   *     or if the key is null.
   */
  public NotJSONWriter key(String string) throws NotJSONException {
    if (string == null) {
      throw new NotJSONException("Null key.");
    }
    if (this.mode == 'k') {
      try {
        NotJSONObject topObject = this.stack[this.top - 1];
        // don't use the built in putOnce method to maintain Android support
        if (topObject.has(string)) {
          throw new NotJSONException("Duplicate key \"" + string + "\"");
        }
        topObject.put(string, true);
        if (this.comma) {
          this.writer.append(',');
        }
        this.writer.append(NotJSONObject.quote(string));
        this.writer.append(':');
        this.comma = false;
        this.mode = 'o';
        return this;
      } catch (IOException e) {
        // Android as of API 25 does not support this exception constructor
        // however we won't worry about it. If an exception is happening here
        // it will just throw a "Method not found" exception instead.
        throw new NotJSONException(e);
      }
    }
    throw new NotJSONException("Misplaced key.");
  }

  /**
   * Begin appending a new object. All keys and values until the balancing <code>endObject</code>
   * will be appended to this object. The <code>endObject</code> method must be called to mark the
   * object's end.
   *
   * @return this
   * @throws NotJSONException If the nesting is too deep, or if the object is started in the wrong
   *     place (for example as a key or after the end of the outermost array or object).
   */
  public NotJSONWriter object() throws NotJSONException {
    if (this.mode == 'i') {
      this.mode = 'o';
    }
    if (this.mode == 'o' || this.mode == 'a') {
      this.append("{");
      this.push(new NotJSONObject());
      this.comma = false;
      return this;
    }
    throw new NotJSONException("Misplaced object.");
  }

  /**
   * Pop an array or object scope.
   *
   * @param c The scope to close.
   * @throws NotJSONException If nesting is wrong.
   */
  private void pop(char c) throws NotJSONException {
    if (this.top <= 0) {
      throw new NotJSONException("Nesting error.");
    }
    char m = this.stack[this.top - 1] == null ? 'a' : 'k';
    if (m != c) {
      throw new NotJSONException("Nesting error.");
    }
    this.top -= 1;
    this.mode = this.top == 0 ? 'd' : this.stack[this.top - 1] == null ? 'a' : 'k';
  }

  /**
   * Push an array or object scope.
   *
   * @param jo The scope to open.
   * @throws NotJSONException If nesting is too deep.
   */
  private void push(NotJSONObject jo) throws NotJSONException {
    if (this.top >= maxdepth) {
      throw new NotJSONException("Nesting too deep.");
    }
    this.stack[this.top] = jo;
    this.mode = jo == null ? 'a' : 'k';
    this.top += 1;
  }

  /**
   * Make a JSON text of an Object value. If the object has an value.toNotJSONString() method, then
   * that method will be used to produce the JSON text. The method is required to produce a strictly
   * conforming text. If the object does not contain a toNotJSONString method (which is the most
   * common case), then a text will be produced by other means. If the value is an array or
   * Collection, then a NotJSONArray will be made from it and its toNotJSONString method will be
   * called. If the value is a MAP, then a NotJSONObject will be made from it and its
   * toNotJSONString method will be called. Otherwise, the value's toString method will be called,
   * and the result will be quoted.
   *
   * <p>Warning: This method assumes that the data structure is acyclical.
   *
   * @param value The value to be serialized.
   * @return a printable, displayable, transmittable representation of the object, beginning with
   *     <code>{</code>&nbsp;<small>(left brace)</small> and ending with <code>}</code>
   *     &nbsp;<small>(right brace)</small>.
   * @throws NotJSONException If the value is or contains an invalid number.
   */
  public static String valueToString(Object value) throws NotJSONException {
    if (value == null || value.equals(null)) {
      return "null";
    }
    if (value instanceof NotJSONString) {
      String object;
      try {
        object = ((NotJSONString) value).toJSONString();
      } catch (Exception e) {
        throw new NotJSONException(e);
      }
      if (object != null) {
        return object;
      }
      throw new NotJSONException("Bad value from toNotJSONString: " + object);
    }
    if (value instanceof Number) {
      // not all Numbers may match actual JSON Numbers. i.e. Fractions or Complex
      final String numberAsString = NotJSONObject.numberToString((Number) value);
      if (NotJSONObject.NUMBER_PATTERN.matcher(numberAsString).matches()) {
        // Close enough to a JSON number that we will return it unquoted
        return numberAsString;
      }
      // The Number value is not a valid JSON number.
      // Instead we will quote it as a string
      return NotJSONObject.quote(numberAsString);
    }
    if (value instanceof Boolean
        || value instanceof NotJSONObject
        || value instanceof NotJSONArray) {
      return value.toString();
    }
    if (value instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) value;
      return new NotJSONObject(map).toString();
    }
    if (value instanceof Collection) {
      Collection<?> coll = (Collection<?>) value;
      return new NotJSONArray(coll).toString();
    }
    if (value.getClass().isArray()) {
      return new NotJSONArray(value).toString();
    }
    if (value instanceof Enum<?>) {
      return NotJSONObject.quote(((Enum<?>) value).name());
    }
    return NotJSONObject.quote(value.toString());
  }

  /**
   * Append either the value <code>true</code> or the value <code>false</code>.
   *
   * @param b A boolean.
   * @return this
   * @throws NotJSONException if a called function has an error
   */
  public NotJSONWriter value(boolean b) throws NotJSONException {
    return this.append(b ? "true" : "false");
  }

  /**
   * Append a double value.
   *
   * @param d A double.
   * @return this
   * @throws NotJSONException If the number is not finite.
   */
  public NotJSONWriter value(double d) throws NotJSONException {
    return this.value(Double.valueOf(d));
  }

  /**
   * Append a long value.
   *
   * @param l A long.
   * @return this
   * @throws NotJSONException if a called function has an error
   */
  public NotJSONWriter value(long l) throws NotJSONException {
    return this.append(Long.toString(l));
  }

  /**
   * Append an object value.
   *
   * @param object The object to append. It can be null, or a Boolean, Number, String,
   *     NotJSONObject, or NotJSONArray, or an object that implements NotJSONString.
   * @return this
   * @throws NotJSONException If the value is out of sequence.
   */
  public NotJSONWriter value(Object object) throws NotJSONException {
    return this.append(valueToString(object));
  }
}
