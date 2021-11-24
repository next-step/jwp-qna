package qna.utils;

import qna.constants.EntityField;

import java.security.InvalidParameterException;
import java.util.regex.Pattern;

public class ValidationUtils {
  public static void stringNullOrEmptyCheck(String string) {
    stringNullOrEmptyCheck(string, null);
  }

  public static void stringNullOrEmptyCheck(String string, String message) {
    if (string == null || string.trim().isEmpty()) {
      throw new NullPointerException(message);
    }
  }

  public static boolean patternMatchCheck(String string, String regex) {
    return Pattern.compile(regex)
      .matcher(string)
      .matches();
  }

  public static void fieldLengthCheck(String string, int limitLength, EntityField field) {
    if (string.length() > limitLength) {
      throw new InvalidParameterException(field.value() + "은(는) " + limitLength + "자를 초과하여 입력할 수 없습니다.");
    }
  }
}
