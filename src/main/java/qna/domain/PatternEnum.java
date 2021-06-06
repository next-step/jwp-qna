package qna.domain;

import java.util.regex.Pattern;

/**
 * 이메일:
 *   기본적인 이메일 형식을 가져야 하며 최대 50자리가 넘지 않아야 한다.
 *
 * 비밀번호:
 *   영문(대소문자 구분) + 숫자 + 특수문자 조합으로 9~20 자리여야한다.
 */
public enum PatternEnum {
    EMAIL("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"),
    PASSWORD("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{9,12}$");

    private final Pattern pattern;

    PatternEnum(final String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public Pattern toPattern() {
        return this.pattern;
    }
}
