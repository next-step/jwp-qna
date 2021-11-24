package qna.constants;

public enum EntityField {
  EMAIL("이메일"),
  NAME("이름"),
  PASSWORD("비밀번호"),
  TITLE("제목"),
  USER_ID("사용자 ID");

  private final String value;

  EntityField(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}
