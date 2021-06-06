package qna.domain;

import java.util.regex.Pattern;

/**
 * 이메일:
 *   기본적인 이메일 형식을 가져야 하며 최대 50자리가 넘지 않아야 한다.
 *
 * 비밀번호:
 *   영문(대소문자 구분) + 숫자 + 특수문자 조합으로 9~20 자리여야한다.
 *
 * 이름:
 *   영문, 한글, 숫자 조합으로 최대 20자리가 넘지 않아야 한다.
 *
 * 아이디:
 *   영문, 숫자 조합으로 최대 20자리가 넘지 않아야 한다.
 */
public enum PatternEnum {
    EMAIL("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"),
    PASSWORD("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{9,12}$"),
    NAME("^[0-9a-zA-Z가-힣]*$"),
    USER_ID("^[0-9a-zA-Z]*$");

    private final Pattern pattern;

    PatternEnum(final String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public Pattern toPattern() {
        return this.pattern;
    }
}
