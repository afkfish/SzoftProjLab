package com.ez_mode.notJson;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NotJSONArray implements Iterable<Object> {

  /** The arrayList where the JSONArray's properties are kept. */
  private final ArrayList<Object> myArrayList;

  /** Construct an empty JSONArray. */
  public NotJSONArray() {
    this.myArrayList = new ArrayList<Object>();
  }

  public NotJSONArray(NotJSONTokener x) throws NotJSONException {
    this();
    if (x.nextClean() != '[') {
      throw x.syntaxError("A JSONArray text must start with '['");
    }

    char nextChar = x.nextClean();
    if (nextChar == 0) {
      // array is unclosed. No ']' found, instead EOF
      throw x.syntaxError("Expected a ',' or ']'");
    }
    if (nextChar != ']') {
      x.back();
      for (; ; ) {
        if (x.nextClean() == ',') {
          x.back();
          this.myArrayList.add(NotJSONObject.NULL);
        } else {
          x.back();
          this.myArrayList.add(x.nextValue());
        }
        switch (x.nextClean()) {
          case 0:
            // array is unclosed. No ']' found, instead EOF
            throw x.syntaxError("Expected a ',' or ']'");
          case ',':
            nextChar = x.nextClean();
            if (nextChar == 0) {
              // array is unclosed. No ']' found, instead EOF
              throw x.syntaxError("Expected a ',' or ']'");
            }
            if (nextChar == ']') {
              return;
            }
            x.back();
            break;
          case ']':
            return;
          default:
            throw x.syntaxError("Expected a ',' or ']'");
        }
      }
    }
  }

  /**
   * Construct a JSONArray from a source JSON text.
   *
   * @param source A string that begins with <code>[</code>&nbsp;<small>(left bracket)</small> and
   *     ends with <code>]</code> &nbsp;<small>(right bracket)</small>.
   * @throws NotJSONException If there is a syntax error.
   */
  public NotJSONArray(String source) throws NotJSONException {
    this(new NotJSONTokener(source));
  }

  /**
   * Construct a JSONArray from a Collection.
   *
   * @param collection A Collection.
   */
  public NotJSONArray(Collection<?> collection) {
    if (collection == null) {
      this.myArrayList = new ArrayList<Object>();
    } else {
      this.myArrayList = new ArrayList<Object>(collection.size());
      this.addAll(collection, true);
    }
  }

  /**
   * Construct a JSONArray from an Iterable. This is a shallow copy.
   *
   * @param iter A Iterable collection.
   */
  public NotJSONArray(Iterable<?> iter) {
    this();
    if (iter == null) {
      return;
    }
    this.addAll(iter, true);
  }

  /**
   * Construct a JSONArray from another JSONArray. This is a shallow copy.
   *
   * @param array A array.
   */
  public NotJSONArray(NotJSONArray array) {
    if (array == null) {
      this.myArrayList = new ArrayList<Object>();
    } else {
      // shallow copy directly the internal array lists as any wrapping
      // should have been done already in the original JSONArray
      this.myArrayList = new ArrayList<Object>(array.myArrayList);
    }
  }

  /**
   * Construct a JSONArray from an array.
   *
   * @param array Array. If the parameter passed is null, or not an array, an exception will be
   *     thrown.
   * @throws NotJSONException If not an array or if an array value is non-finite number.
   * @throws NullPointerException Thrown if the array parameter is null.
   */
  public NotJSONArray(Object array) throws NotJSONException {
    this();
    if (!array.getClass().isArray()) {
      throw new NotJSONException(
          "JSONArray initial value should be a string or collection or array.");
    }
    this.addAll(array, true);
  }

  /**
   * Construct a JSONArray with the specified initial capacity.
   *
   * @param initialCapacity the initial capacity of the JSONArray.
   * @throws NotJSONException If the initial capacity is negative.
   */
  public NotJSONArray(int initialCapacity) throws NotJSONException {
    if (initialCapacity < 0) {
      throw new NotJSONException("JSONArray initial capacity cannot be negative.");
    }
    this.myArrayList = new ArrayList<Object>(initialCapacity);
  }

  @Override
  public Iterator<Object> iterator() {
    return this.myArrayList.iterator();
  }

  /**
   * Get the object value associated with an index.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return An object value.
   * @throws NotJSONException If there is no value for the index.
   */
  public Object get(int index) throws NotJSONException {
    Object object = this.opt(index);
    if (object == null) {
      throw new NotJSONException("JSONArray[" + index + "] not found.");
    }
    return object;
  }

  /**
   * Get the boolean value associated with an index. The string values "true" and "false" are
   * converted to boolean.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The truth.
   * @throws NotJSONException If there is no value for the index or if the value is not convertible
   *     to boolean.
   */
  public boolean getBoolean(int index) throws NotJSONException {
    Object object = this.get(index);
    if (object.equals(Boolean.FALSE)
        || (object instanceof String && ((String) object).equalsIgnoreCase("false"))) {
      return false;
    } else if (object.equals(Boolean.TRUE)
        || (object instanceof String && ((String) object).equalsIgnoreCase("true"))) {
      return true;
    }
    throw wrongValueFormatException(index, "boolean", object, null);
  }

  /**
   * Get the double value associated with an index.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The value.
   * @throws NotJSONException If the key is not found or if the value cannot be converted to a
   *     number.
   */
  public double getDouble(int index) throws NotJSONException {
    final Object object = this.get(index);
    if (object instanceof Number) {
      return ((Number) object).doubleValue();
    }
    try {
      return Double.parseDouble(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(index, "double", object, e);
    }
  }

  /**
   * Get the float value associated with a key.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The numeric value.
   * @throws NotJSONException if the key is not found or if the value is not a Number object and
   *     cannot be converted to a number.
   */
  public float getFloat(int index) throws NotJSONException {
    final Object object = this.get(index);
    if (object instanceof Number) {
      return ((Number) object).floatValue();
    }
    try {
      return Float.parseFloat(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(index, "float", object, e);
    }
  }

  /**
   * Get the Number value associated with a key.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The numeric value.
   * @throws NotJSONException if the key is not found or if the value is not a Number object and
   *     cannot be converted to a number.
   */
  public Number getNumber(int index) throws NotJSONException {
    Object object = this.get(index);
    try {
      if (object instanceof Number) {
        return (Number) object;
      }
      return NotJSONObject.stringToNumber(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(index, "number", object, e);
    }
  }

  /**
   * Get the enum value associated with an index.
   *
   * @param <E> Enum Type
   * @param clazz The type of enum to retrieve.
   * @param index The index must be between 0 and length() - 1.
   * @return The enum value at the index location
   * @throws NotJSONException if the key is not found or if the value cannot be converted to an
   *     enum.
   */
  public <E extends Enum<E>> E getEnum(Class<E> clazz, int index) throws NotJSONException {
    E val = optEnum(clazz, index);
    if (val == null) {
      // NotJSONException should really take a throwable argument.
      // If it did, I would re-implement this with the Enum.valueOf
      // method and place any thrown exception in the NotJSONException
      throw wrongValueFormatException(
          index, "enum of type " + NotJSONObject.quote(clazz.getSimpleName()), opt(index), null);
    }
    return val;
  }

  /**
   * Get the BigDecimal value associated with an index. If the value is float or double, the {@link
   * BigDecimal#BigDecimal(double)} constructor will be used. See notes on the constructor for
   * conversion issues that may arise.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The value.
   * @throws NotJSONException If the key is not found or if the value cannot be converted to a
   *     BigDecimal.
   */
  public BigDecimal getBigDecimal(int index) throws NotJSONException {
    Object object = this.get(index);
    BigDecimal val = NotJSONObject.objectToBigDecimal(object, null);
    if (val == null) {
      throw wrongValueFormatException(index, "BigDecimal", object, null);
    }
    return val;
  }

  /**
   * Get the BigInteger value associated with an index.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The value.
   * @throws NotJSONException If the key is not found or if the value cannot be converted to a
   *     BigInteger.
   */
  public BigInteger getBigInteger(int index) throws NotJSONException {
    Object object = this.get(index);
    BigInteger val = NotJSONObject.objectToBigInteger(object, null);
    if (val == null) {
      throw wrongValueFormatException(index, "BigInteger", object, null);
    }
    return val;
  }

  /**
   * Get the int value associated with an index.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The value.
   * @throws NotJSONException If the key is not found or if the value is not a number.
   */
  public int getInt(int index) throws NotJSONException {
    final Object object = this.get(index);
    if (object instanceof Number) {
      return ((Number) object).intValue();
    }
    try {
      return Integer.parseInt(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(index, "int", object, e);
    }
  }

  /**
   * Get the JSONArray associated with an index.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return A JSONArray value.
   * @throws NotJSONException If there is no value for the index. or if the value is not a JSONArray
   */
  public NotJSONArray getJSONArray(int index) throws NotJSONException {
    Object object = this.get(index);
    if (object instanceof NotJSONArray) {
      return (NotJSONArray) object;
    }
    throw wrongValueFormatException(index, "JSONArray", object, null);
  }

  /**
   * Get the JSONObject associated with an index.
   *
   * @param index subscript
   * @return A JSONObject value.
   * @throws NotJSONException If there is no value for the index or if the value is not a JSONObject
   */
  public NotJSONObject getJSONObject(int index) throws NotJSONException {
    Object object = this.get(index);
    if (object instanceof NotJSONObject) {
      return (NotJSONObject) object;
    }
    throw wrongValueFormatException(index, "JSONObject", object, null);
  }

  /**
   * Get the long value associated with an index.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The value.
   * @throws NotJSONException If the key is not found or if the value cannot be converted to a
   *     number.
   */
  public long getLong(int index) throws NotJSONException {
    final Object object = this.get(index);
    if (object instanceof Number) {
      return ((Number) object).longValue();
    }
    try {
      return Long.parseLong(object.toString());
    } catch (Exception e) {
      throw wrongValueFormatException(index, "long", object, e);
    }
  }

  /**
   * Get the string associated with an index.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return A string value.
   * @throws NotJSONException If there is no string value for the index.
   */
  public String getString(int index) throws NotJSONException {
    Object object = this.get(index);
    if (object instanceof String) {
      return (String) object;
    }
    throw wrongValueFormatException(index, "String", object, null);
  }

  /**
   * Determine if the value is <code>null</code>.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return true if the value at the index is <code>null</code>, or if there is no value.
   */
  public boolean isNull(int index) {
    return NotJSONObject.NULL.equals(this.opt(index));
  }

  /**
   * Make a string from the contents of this JSONArray. The <code>separator</code> string is
   * inserted between each element. Warning: This method assumes that the data structure is
   * acyclical.
   *
   * @param separator A string that will be inserted between the elements.
   * @return a string.
   * @throws NotJSONException If the array contains an invalid number.
   */
  public String join(String separator) throws NotJSONException {
    int len = this.length();
    if (len == 0) {
      return "";
    }

    StringBuilder sb = new StringBuilder(NotJSONObject.valueToString(this.myArrayList.get(0)));

    for (int i = 1; i < len; i++) {
      sb.append(separator).append(NotJSONObject.valueToString(this.myArrayList.get(i)));
    }
    return sb.toString();
  }

  /**
   * Get the number of elements in the JSONArray, included nulls.
   *
   * @return The length (or size).
   */
  public int length() {
    return this.myArrayList.size();
  }

  /**
   * Removes all of the elements from this JSONArray. The JSONArray will be empty after this call
   * returns.
   */
  public void clear() {
    this.myArrayList.clear();
  }

  /**
   * Get the optional object value associated with an index.
   *
   * @param index The index must be between 0 and length() - 1. If not, null is returned.
   * @return An object value, or null if there is no object at that index.
   */
  public Object opt(int index) {
    return (index < 0 || index >= this.length()) ? null : this.myArrayList.get(index);
  }

  /**
   * Get the optional boolean value associated with an index. It returns false if there is no value
   * at that index, or if the value is not Boolean.TRUE or the String "true".
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The truth.
   */
  public boolean optBoolean(int index) {
    return this.optBoolean(index, false);
  }

  /**
   * Get the optional boolean value associated with an index. It returns the defaultValue if there
   * is no value at that index or if it is not a Boolean or the String "true" or "false" (case
   * insensitive).
   *
   * @param index The index must be between 0 and length() - 1.
   * @param defaultValue A boolean default.
   * @return The truth.
   */
  public boolean optBoolean(int index, boolean defaultValue) {
    try {
      return this.getBoolean(index);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * Get the optional double value associated with an index. NaN is returned if there is no value
   * for the index, or if the value is not a number and cannot be converted to a number.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The value.
   */
  public double optDouble(int index) {
    return this.optDouble(index, Double.NaN);
  }

  /**
   * Get the optional double value associated with an index. The defaultValue is returned if there
   * is no value for the index, or if the value is not a number and cannot be converted to a number.
   *
   * @param index subscript
   * @param defaultValue The default value.
   * @return The value.
   */
  public double optDouble(int index, double defaultValue) {
    final Number val = this.optNumber(index, null);
    if (val == null) {
      return defaultValue;
    }
    final double doubleValue = val.doubleValue();
    // if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
    // return defaultValue;
    // }
    return doubleValue;
  }

  /**
   * Get the optional float value associated with an index. NaN is returned if there is no value for
   * the index, or if the value is not a number and cannot be converted to a number.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The value.
   */
  public float optFloat(int index) {
    return this.optFloat(index, Float.NaN);
  }

  /**
   * Get the optional float value associated with an index. The defaultValue is returned if there is
   * no value for the index, or if the value is not a number and cannot be converted to a number.
   *
   * @param index subscript
   * @param defaultValue The default value.
   * @return The value.
   */
  public float optFloat(int index, float defaultValue) {
    final Number val = this.optNumber(index, null);
    if (val == null) {
      return defaultValue;
    }
    final float floatValue = val.floatValue();
    // if (Float.isNaN(floatValue) || Float.isInfinite(floatValue)) {
    // return floatValue;
    // }
    return floatValue;
  }

  /**
   * Get the optional int value associated with an index. Zero is returned if there is no value for
   * the index, or if the value is not a number and cannot be converted to a number.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The value.
   */
  public int optInt(int index) {
    return this.optInt(index, 0);
  }

  /**
   * Get the optional int value associated with an index. The defaultValue is returned if there is
   * no value for the index, or if the value is not a number and cannot be converted to a number.
   *
   * @param index The index must be between 0 and length() - 1.
   * @param defaultValue The default value.
   * @return The value.
   */
  public int optInt(int index, int defaultValue) {
    final Number val = this.optNumber(index, null);
    if (val == null) {
      return defaultValue;
    }
    return val.intValue();
  }

  /**
   * Get the enum value associated with a key.
   *
   * @param <E> Enum Type
   * @param clazz The type of enum to retrieve.
   * @param index The index must be between 0 and length() - 1.
   * @return The enum value at the index location or null if not found
   */
  public <E extends Enum<E>> E optEnum(Class<E> clazz, int index) {
    return this.optEnum(clazz, index, null);
  }

  /**
   * Get the enum value associated with a key.
   *
   * @param <E> Enum Type
   * @param clazz The type of enum to retrieve.
   * @param index The index must be between 0 and length() - 1.
   * @param defaultValue The default in case the value is not found
   * @return The enum value at the index location or defaultValue if the value is not found or
   *     cannot be assigned to clazz
   */
  public <E extends Enum<E>> E optEnum(Class<E> clazz, int index, E defaultValue) {
    try {
      Object val = this.opt(index);
      if (NotJSONObject.NULL.equals(val)) {
        return defaultValue;
      }
      if (clazz.isAssignableFrom(val.getClass())) {
        // we just checked it!
        @SuppressWarnings("unchecked")
        E myE = (E) val;
        return myE;
      }
      return Enum.valueOf(clazz, val.toString());
    } catch (IllegalArgumentException e) {
      return defaultValue;
    } catch (NullPointerException e) {
      return defaultValue;
    }
  }

  /**
   * Get the optional BigInteger value associated with an index. The defaultValue is returned if
   * there is no value for the index, or if the value is not a number and cannot be converted to a
   * number.
   *
   * @param index The index must be between 0 and length() - 1.
   * @param defaultValue The default value.
   * @return The value.
   */
  public BigInteger optBigInteger(int index, BigInteger defaultValue) {
    Object val = this.opt(index);
    return NotJSONObject.objectToBigInteger(val, defaultValue);
  }

  /**
   * Get the optional BigDecimal value associated with an index. The defaultValue is returned if
   * there is no value for the index, or if the value is not a number and cannot be converted to a
   * number. If the value is float or double, the {@link BigDecimal#BigDecimal(double)} constructor
   * will be used. See notes on the constructor for conversion issues that may arise.
   *
   * @param index The index must be between 0 and length() - 1.
   * @param defaultValue The default value.
   * @return The value.
   */
  public BigDecimal optBigDecimal(int index, BigDecimal defaultValue) {
    Object val = this.opt(index);
    return NotJSONObject.objectToBigDecimal(val, defaultValue);
  }

  /**
   * Get the optional JSONArray associated with an index.
   *
   * @param index subscript
   * @return A JSONArray value, or null if the index has no value, or if the value is not a
   *     JSONArray.
   */
  public NotJSONArray optJSONArray(int index) {
    Object o = this.opt(index);
    return o instanceof NotJSONArray ? (NotJSONArray) o : null;
  }

  /**
   * Get the optional JSONObject associated with an index. Null is returned if the key is not found,
   * or null if the index has no value, or if the value is not a JSONObject.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return A JSONObject value.
   */
  public NotJSONObject optJSONObject(int index) {
    Object o = this.opt(index);
    return o instanceof NotJSONObject ? (NotJSONObject) o : null;
  }

  /**
   * Get the optional long value associated with an index. Zero is returned if there is no value for
   * the index, or if the value is not a number and cannot be converted to a number.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return The value.
   */
  public long optLong(int index) {
    return this.optLong(index, 0);
  }

  /**
   * Get the optional long value associated with an index. The defaultValue is returned if there is
   * no value for the index, or if the value is not a number and cannot be converted to a number.
   *
   * @param index The index must be between 0 and length() - 1.
   * @param defaultValue The default value.
   * @return The value.
   */
  public long optLong(int index, long defaultValue) {
    final Number val = this.optNumber(index, null);
    if (val == null) {
      return defaultValue;
    }
    return val.longValue();
  }

  /**
   * Get an optional {@link Number} value associated with a key, or <code>null</code> if there is no
   * such key or if the value is not a number. If the value is a string, an attempt will be made to
   * evaluate it as a number ({@link BigDecimal}). This method would be used in cases where type
   * coercion of the number value is unwanted.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return An object which is the value.
   */
  public Number optNumber(int index) {
    return this.optNumber(index, null);
  }

  /**
   * Get an optional {@link Number} value associated with a key, or the default if there is no such
   * key or if the value is not a number. If the value is a string, an attempt will be made to
   * evaluate it as a number ({@link BigDecimal}). This method would be used in cases where type
   * coercion of the number value is unwanted.
   *
   * @param index The index must be between 0 and length() - 1.
   * @param defaultValue The default.
   * @return An object which is the value.
   */
  public Number optNumber(int index, Number defaultValue) {
    Object val = this.opt(index);
    if (NotJSONObject.NULL.equals(val)) {
      return defaultValue;
    }
    if (val instanceof Number) {
      return (Number) val;
    }

    if (val instanceof String) {
      try {
        return NotJSONObject.stringToNumber((String) val);
      } catch (Exception e) {
        return defaultValue;
      }
    }
    return defaultValue;
  }

  /**
   * Get the optional string value associated with an index. It returns an empty string if there is
   * no value at that index. If the value is not a string and is not null, then it is converted to a
   * string.
   *
   * @param index The index must be between 0 and length() - 1.
   * @return A String value.
   */
  public String optString(int index) {
    return this.optString(index, "");
  }

  /**
   * Get the optional string associated with an index. The defaultValue is returned if the key is
   * not found.
   *
   * @param index The index must be between 0 and length() - 1.
   * @param defaultValue The default value.
   * @return A String value.
   */
  public String optString(int index, String defaultValue) {
    Object object = this.opt(index);
    return NotJSONObject.NULL.equals(object) ? defaultValue : object.toString();
  }

  /**
   * Append a boolean value. This increases the array's length by one.
   *
   * @param value A boolean value.
   * @return this.
   */
  public NotJSONArray put(boolean value) {
    return this.put(value ? Boolean.TRUE : Boolean.FALSE);
  }

  /**
   * Put a value in the JSONArray, where the value will be a JSONArray which is produced from a
   * Collection.
   *
   * @param value A Collection value.
   * @return this.
   * @throws NotJSONException If the value is non-finite number.
   */
  public NotJSONArray put(Collection<?> value) {
    return this.put(new NotJSONArray(value));
  }

  /**
   * Append a double value. This increases the array's length by one.
   *
   * @param value A double value.
   * @return this.
   * @throws NotJSONException if the value is not finite.
   */
  public NotJSONArray put(double value) throws NotJSONException {
    return this.put(Double.valueOf(value));
  }

  /**
   * Append a float value. This increases the array's length by one.
   *
   * @param value A float value.
   * @return this.
   * @throws NotJSONException if the value is not finite.
   */
  public NotJSONArray put(float value) throws NotJSONException {
    return this.put(Float.valueOf(value));
  }

  /**
   * Append an int value. This increases the array's length by one.
   *
   * @param value An int value.
   * @return this.
   */
  public NotJSONArray put(int value) {
    return this.put(Integer.valueOf(value));
  }

  /**
   * Append an long value. This increases the array's length by one.
   *
   * @param value A long value.
   * @return this.
   */
  public NotJSONArray put(long value) {
    return this.put(Long.valueOf(value));
  }

  /**
   * Put a value in the JSONArray, where the value will be a JSONObject which is produced from a
   * Map.
   *
   * @param value A Map value.
   * @return this.
   * @throws NotJSONException If a value in the map is non-finite number.
   * @throws NullPointerException If a key in the map is <code>null</code>
   */
  public NotJSONArray put(Map<?, ?> value) {
    return this.put(new NotJSONObject(value));
  }

  /**
   * Append an object value. This increases the array's length by one.
   *
   * @param value An object value. The value should be a Boolean, Double, Integer, JSONArray,
   *     JSONObject, Long, or String, or the JSONObject.NULL object.
   * @return this.
   * @throws NotJSONException If the value is non-finite number.
   */
  public NotJSONArray put(Object value) {
    NotJSONObject.testValidity(value);
    this.myArrayList.add(value);
    return this;
  }

  /**
   * Put or replace a boolean value in the JSONArray. If the index is greater than the length of the
   * JSONArray, then null elements will be added as necessary to pad it out.
   *
   * @param index The subscript.
   * @param value A boolean value.
   * @return this.
   * @throws NotJSONException If the index is negative.
   */
  public NotJSONArray put(int index, boolean value) throws NotJSONException {
    return this.put(index, value ? Boolean.TRUE : Boolean.FALSE);
  }

  /**
   * Put a value in the JSONArray, where the value will be a JSONArray which is produced from a
   * Collection.
   *
   * @param index The subscript.
   * @param value A Collection value.
   * @return this.
   * @throws NotJSONException If the index is negative or if the value is non-finite.
   */
  public NotJSONArray put(int index, Collection<?> value) throws NotJSONException {
    return this.put(index, new NotJSONArray(value));
  }

  /**
   * Put or replace a double value. If the index is greater than the length of the JSONArray, then
   * null elements will be added as necessary to pad it out.
   *
   * @param index The subscript.
   * @param value A double value.
   * @return this.
   * @throws NotJSONException If the index is negative or if the value is non-finite.
   */
  public NotJSONArray put(int index, double value) throws NotJSONException {
    return this.put(index, Double.valueOf(value));
  }

  /**
   * Put or replace a float value. If the index is greater than the length of the JSONArray, then
   * null elements will be added as necessary to pad it out.
   *
   * @param index The subscript.
   * @param value A float value.
   * @return this.
   * @throws NotJSONException If the index is negative or if the value is non-finite.
   */
  public NotJSONArray put(int index, float value) throws NotJSONException {
    return this.put(index, Float.valueOf(value));
  }

  /**
   * Put or replace an int value. If the index is greater than the length of the JSONArray, then
   * null elements will be added as necessary to pad it out.
   *
   * @param index The subscript.
   * @param value An int value.
   * @return this.
   * @throws NotJSONException If the index is negative.
   */
  public NotJSONArray put(int index, int value) throws NotJSONException {
    return this.put(index, Integer.valueOf(value));
  }

  /**
   * Put or replace a long value. If the index is greater than the length of the JSONArray, then
   * null elements will be added as necessary to pad it out.
   *
   * @param index The subscript.
   * @param value A long value.
   * @return this.
   * @throws NotJSONException If the index is negative.
   */
  public NotJSONArray put(int index, long value) throws NotJSONException {
    return this.put(index, Long.valueOf(value));
  }

  /**
   * Put a value in the JSONArray, where the value will be a JSONObject that is produced from a Map.
   *
   * @param index The subscript.
   * @param value The Map value.
   * @return this.
   * @throws NotJSONException If the index is negative or if the value is an invalid number.
   * @throws NullPointerException If a key in the map is <code>null</code>
   */
  public NotJSONArray put(int index, Map<?, ?> value) throws NotJSONException {
    this.put(index, new NotJSONObject(value));
    return this;
  }

  /**
   * Put or replace an object value in the JSONArray. If the index is greater than the length of the
   * JSONArray, then null elements will be added as necessary to pad it out.
   *
   * @param index The subscript.
   * @param value The value to put into the array. The value should be a Boolean, Double, Integer,
   *     JSONArray, JSONObject, Long, or String, or the JSONObject.NULL object.
   * @return this.
   * @throws NotJSONException If the index is negative or if the value is an invalid number.
   */
  public NotJSONArray put(int index, Object value) throws NotJSONException {
    if (index < 0) {
      throw new NotJSONException("JSONArray[" + index + "] not found.");
    }
    if (index < this.length()) {
      NotJSONObject.testValidity(value);
      this.myArrayList.set(index, value);
      return this;
    }
    if (index == this.length()) {
      // simple append
      return this.put(value);
    }
    // if we are inserting past the length, we want to grow the array all at once
    // instead of incrementally.
    this.myArrayList.ensureCapacity(index + 1);
    while (index != this.length()) {
      // we don't need to test validity of NULL objects
      this.myArrayList.add(NotJSONObject.NULL);
    }
    return this.put(value);
  }

  /**
   * Put a collection's elements in to the JSONArray.
   *
   * @param collection A Collection.
   * @return this.
   */
  public NotJSONArray putAll(Collection<?> collection) {
    this.addAll(collection, false);
    return this;
  }

  /**
   * Put an Iterable's elements in to the JSONArray.
   *
   * @param iter An Iterable.
   * @return this.
   */
  public NotJSONArray putAll(Iterable<?> iter) {
    this.addAll(iter, false);
    return this;
  }

  /**
   * Put a JSONArray's elements in to the JSONArray.
   *
   * @param array A JSONArray.
   * @return this.
   */
  public NotJSONArray putAll(NotJSONArray array) {
    // directly copy the elements from the source array to this one
    // as all wrapping should have been done already in the source.
    this.myArrayList.addAll(array.myArrayList);
    return this;
  }

  /**
   * Put an array's elements in to the JSONArray.
   *
   * @param array Array. If the parameter passed is null, or not an array or Iterable, an exception
   *     will be thrown.
   * @return this.
   * @throws NotJSONException If not an array, JSONArray, Iterable or if an value is non-finite
   *     number.
   * @throws NullPointerException Thrown if the array parameter is null.
   */
  public NotJSONArray putAll(Object array) throws NotJSONException {
    this.addAll(array, false);
    return this;
  }

  /**
   * Creates a NotJSONPointer using an initialization string and tries to match it to an item within
   * this JSONArray. For example, given a JSONArray initialized with this document:
   *
   * <pre>
   * [
   *     {"b":"c"}
   * ]
   * </pre>
   *
   * and this NotJSONPointer string:
   *
   * <pre>
   * "/0/b"
   * </pre>
   *
   * Then this method will return the String "c" A NotNotJSONPointerException may be thrown from
   * code called by this method.
   *
   * @param NotJSONPointer string that can be used to create a NotJSONPointer
   * @return the item matched by the NotJSONPointer, otherwise null
   */
  public Object query(String NotJSONPointer) {
    return query(new NotJSONPointer(NotJSONPointer));
  }

  /**
   * Uses a user initialized NotJSONPointer and tries to match it to an item within this JSONArray.
   * For example, given a JSONArray initialized with this document:
   *
   * <pre>
   * [
   *     {"b":"c"}
   * ]
   * </pre>
   *
   * and this NotJSONPointer:
   *
   * <pre>
   * "/0/b"
   * </pre>
   *
   * Then this method will return the String "c" A NotNotJSONPointerException may be thrown from
   * code called by this method.
   *
   * @param NotJSONPointer string that can be used to create a NotJSONPointer
   * @return the item matched by the NotJSONPointer, otherwise null
   */
  public Object query(NotJSONPointer NotJSONPointer) {
    return NotJSONPointer.queryFrom(this);
  }

  /**
   * Queries and returns a value from this object using {@code NotJSONPointer}, or returns null if
   * the query fails due to a missing key.
   *
   * @param NotJSONPointer the string representation of the JSON pointer
   * @return the queried value or {@code null}
   * @throws IllegalArgumentException if {@code NotJSONPointer} has invalid syntax
   */
  public Object optQuery(String NotJSONPointer) {
    return optQuery(new NotJSONPointer(NotJSONPointer));
  }

  /**
   * Queries and returns a value from this object using {@code NotJSONPointer}, or returns null if
   * the query fails due to a missing key.
   *
   * @param NotJSONPointer The JSON pointer
   * @return the queried value or {@code null}
   * @throws IllegalArgumentException if {@code NotJSONPointer} has invalid syntax
   */
  public Object optQuery(NotJSONPointer NotJSONPointer) {
    try {
      return NotJSONPointer.queryFrom(this);
    } catch (NotJSONPointerException e) {
      return null;
    }
  }

  /**
   * Remove an index and close the hole.
   *
   * @param index The index of the element to be removed.
   * @return The value that was associated with the index, or null if there was no value.
   */
  public Object remove(int index) {
    return index >= 0 && index < this.length() ? this.myArrayList.remove(index) : null;
  }

  /**
   * Determine if two JSONArrays are similar. They must contain similar sequences.
   *
   * @param other The other JSONArray
   * @return true if they are equal
   */
  public boolean similar(Object other) {
    if (!(other instanceof NotJSONArray)) {
      return false;
    }
    int len = this.length();
    if (len != ((NotJSONArray) other).length()) {
      return false;
    }
    for (int i = 0; i < len; i += 1) {
      Object valueThis = this.myArrayList.get(i);
      Object valueOther = ((NotJSONArray) other).myArrayList.get(i);
      if (valueThis == valueOther) {
        continue;
      }
      if (valueThis == null) {
        return false;
      }
      if (valueThis instanceof NotJSONObject) {
        if (!((NotJSONObject) valueThis).similar(valueOther)) {
          return false;
        }
      } else if (valueThis instanceof NotJSONArray) {
        if (!((NotJSONArray) valueThis).similar(valueOther)) {
          return false;
        }
      } else if (valueThis instanceof Number && valueOther instanceof Number) {
        if (!NotJSONObject.isNumberSimilar((Number) valueThis, (Number) valueOther)) {
          return false;
        }
      } else if (valueThis instanceof NotJSONString && valueOther instanceof NotJSONString) {
        if (!((NotJSONString) valueThis)
            .toJSONString()
            .equals(((NotJSONString) valueOther).toJSONString())) {
          return false;
        }
      } else if (!valueThis.equals(valueOther)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Produce a JSONObject by combining a JSONArray of names with the values of this JSONArray.
   *
   * @param names A JSONArray containing a list of key strings. These will be paired with the
   *     values.
   * @return A JSONObject, or null if there are no names or if this JSONArray has no values.
   * @throws NotJSONException If any of the names are null.
   */
  public NotJSONObject toJSONObject(NotJSONArray names) throws NotJSONException {
    if (names == null || names.isEmpty() || this.isEmpty()) {
      return null;
    }
    NotJSONObject jo = new NotJSONObject(names.length());
    for (int i = 0; i < names.length(); i += 1) {
      jo.put(names.getString(i), this.opt(i));
    }
    return jo;
  }

  /**
   * Make a JSON text of this JSONArray. For compactness, no unnecessary whitespace is added. If it
   * is not possible to produce a syntactically correct JSON text then null will be returned
   * instead. This could occur if the array contains an invalid number.
   *
   * <p><b> Warning: This method assumes that the data structure is acyclical. </b>
   *
   * @return a printable, displayable, transmittable representation of the array.
   */
  @Override
  public String toString() {
    try {
      return this.toString(0);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Make a pretty-printed JSON text of this JSONArray.
   *
   * <p>If
   *
   * <pre> {@code indentFactor > 0}</pre>
   *
   * and the {@link NotJSONArray} has only one element, then the array will be output on a single
   * line:
   *
   * <pre>{@code [1]}</pre>
   *
   * <p>If an array has 2 or more elements, then it will be output across multiple lines:
   *
   * <pre>{@code
   * [
   * 1,
   * "value 2",
   * 3
   * ]
   * }</pre>
   *
   * <p><b> Warning: This method assumes that the data structure is acyclical. </b>
   *
   * @param indentFactor The number of spaces to add to each level of indentation.
   * @return a printable, displayable, transmittable representation of the object, beginning with
   *     <code>[</code>&nbsp;<small>(left bracket)</small> and ending with <code>]</code>
   *     &nbsp;<small>(right bracket)</small>.
   * @throws NotJSONException if a called function fails
   */
  @SuppressWarnings("resource")
  public String toString(int indentFactor) throws NotJSONException {
    StringWriter sw = new StringWriter();
    synchronized (sw.getBuffer()) {
      return this.write(sw, indentFactor, 0).toString();
    }
  }

  /**
   * Write the contents of the JSONArray as JSON text to a writer. For compactness, no whitespace is
   * added.
   *
   * <p><b> Warning: This method assumes that the data structure is acyclical. </b>
   *
   * @param writer the writer object
   * @return The writer.
   * @throws NotJSONException if a called function fails
   */
  public Writer write(Writer writer) throws NotJSONException {
    return this.write(writer, 0, 0);
  }

  /**
   * Write the contents of the JSONArray as JSON text to a writer.
   *
   * <p>If
   *
   * <pre>{@code indentFactor > 0}</pre>
   *
   * and the {@link NotJSONArray} has only one element, then the array will be output on a single
   * line:
   *
   * <pre>{@code [1]}</pre>
   *
   * <p>If an array has 2 or more elements, then it will be output across multiple lines:
   *
   * <pre>{@code
   * [
   * 1,
   * "value 2",
   * 3
   * ]
   * }</pre>
   *
   * <p><b> Warning: This method assumes that the data structure is acyclical. </b>
   *
   * @param writer Writes the serialized JSON
   * @param indentFactor The number of spaces to add to each level of indentation.
   * @param indent The indentation of the top level.
   * @return The writer.
   * @throws NotJSONException if a called function fails or unable to write
   */
  @SuppressWarnings("resource")
  public Writer write(Writer writer, int indentFactor, int indent) throws NotJSONException {
    try {
      boolean needsComma = false;
      int length = this.length();
      writer.write('[');

      if (length == 1) {
        try {
          NotJSONObject.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
        } catch (Exception e) {
          throw new NotJSONException("Unable to write JSONArray value at index: 0", e);
        }
      } else if (length != 0) {
        final int newIndent = indent + indentFactor;

        for (int i = 0; i < length; i += 1) {
          if (needsComma) {
            writer.write(',');
          }
          if (indentFactor > 0) {
            writer.write('\n');
          }
          NotJSONObject.indent(writer, newIndent);
          try {
            NotJSONObject.writeValue(writer, this.myArrayList.get(i), indentFactor, newIndent);
          } catch (Exception e) {
            throw new NotJSONException("Unable to write JSONArray value at index: " + i, e);
          }
          needsComma = true;
        }
        if (indentFactor > 0) {
          writer.write('\n');
        }
        NotJSONObject.indent(writer, indent);
      }
      writer.write(']');
      return writer;
    } catch (IOException e) {
      throw new NotJSONException(e);
    }
  }

  /**
   * Returns a java.util.List containing all of the elements in this array. If an element in the
   * array is a JSONArray or JSONObject it will also be converted to a List and a Map respectively.
   *
   * <p>Warning: This method assumes that the data structure is acyclical.
   *
   * @return a java.util.List containing the elements of this array
   */
  public List<Object> toList() {
    List<Object> results = new ArrayList<Object>(this.myArrayList.size());
    for (Object element : this.myArrayList) {
      if (element == null || NotJSONObject.NULL.equals(element)) {
        results.add(null);
      } else if (element instanceof NotJSONArray) {
        results.add(((NotJSONArray) element).toList());
      } else if (element instanceof NotJSONObject) {
        results.add(((NotJSONObject) element).toMap());
      } else {
        results.add(element);
      }
    }
    return results;
  }

  /**
   * Check if JSONArray is empty.
   *
   * @return true if JSONArray is empty, otherwise false.
   */
  public boolean isEmpty() {
    return this.myArrayList.isEmpty();
  }

  /**
   * Add a collection's elements to the JSONArray.
   *
   * @param collection A Collection.
   * @param wrap {@code true} to call {@link NotJSONObject#wrap(Object)} for each item, {@code
   *     false} to add the items directly
   */
  private void addAll(Collection<?> collection, boolean wrap) {
    this.myArrayList.ensureCapacity(this.myArrayList.size() + collection.size());
    if (wrap) {
      for (Object o : collection) {
        this.put(NotJSONObject.wrap(o));
      }
    } else {
      for (Object o : collection) {
        this.put(o);
      }
    }
  }

  /**
   * Add an Iterable's elements to the JSONArray.
   *
   * @param iter An Iterable.
   * @param wrap {@code true} to call {@link NotJSONObject#wrap(Object)} for each item, {@code
   *     false} to add the items directly
   */
  private void addAll(Iterable<?> iter, boolean wrap) {
    if (wrap) {
      for (Object o : iter) {
        this.put(NotJSONObject.wrap(o));
      }
    } else {
      for (Object o : iter) {
        this.put(o);
      }
    }
  }

  /**
   * Add an array's elements to the JSONArray.
   *
   * @param array Array. If the parameter passed is null, or not an array, JSONArray, Collection, or
   *     Iterable, an exception will be thrown.
   * @param wrap {@code true} to call {@link NotJSONObject#wrap(Object)} for each item, {@code
   *     false} to add the items directly
   * @throws NotJSONException If not an array or if an array value is non-finite number.
   * @throws NullPointerException Thrown if the array parameter is null.
   */
  private void addAll(Object array, boolean wrap) throws NotJSONException {
    if (array.getClass().isArray()) {
      int length = Array.getLength(array);
      this.myArrayList.ensureCapacity(this.myArrayList.size() + length);
      if (wrap) {
        for (int i = 0; i < length; i += 1) {
          this.put(NotJSONObject.wrap(Array.get(array, i)));
        }
      } else {
        for (int i = 0; i < length; i += 1) {
          this.put(Array.get(array, i));
        }
      }
    } else if (array instanceof NotJSONArray) {
      // use the built in array list `addAll` as all object
      // wrapping should have been completed in the original
      // JSONArray
      this.myArrayList.addAll(((NotJSONArray) array).myArrayList);
    } else if (array instanceof Collection) {
      this.addAll((Collection<?>) array, wrap);
    } else if (array instanceof Iterable) {
      this.addAll((Iterable<?>) array, wrap);
    } else {
      throw new NotJSONException(
          "JSONArray initial value should be a string or collection or array.");
    }
  }

  /**
   * Create a new NotJSONException in a common format for incorrect conversions.
   *
   * @param idx index of the item
   * @param valueType the type of value being coerced to
   * @param cause optional cause of the coercion failure
   * @return NotJSONException that can be thrown.
   */
  private static NotJSONException wrongValueFormatException(
      int idx, String valueType, Object value, Throwable cause) {
    if (value == null) {
      return new NotJSONException(
          "JSONArray[" + idx + "] is not a " + valueType + " (null).", cause);
    }
    // don't try to toString collections or known object types that could be large.
    if (value instanceof Map || value instanceof Iterable || value instanceof NotJSONObject) {
      return new NotJSONException(
          "JSONArray[" + idx + "] is not a " + valueType + " (" + value.getClass() + ").", cause);
    }
    return new NotJSONException(
        "JSONArray["
            + idx
            + "] is not a "
            + valueType
            + " ("
            + value.getClass()
            + " : "
            + value
            + ").",
        cause);
  }
}
