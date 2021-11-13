package qna.domain;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {
	private static final String EMPTY = "";
	private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
	private static final int MAX_LENGTH = 50;

	@Column(name = "email", length = MAX_LENGTH)
	private String value;

	protected Email() {

	}

	private Email(String value) {
		this.value = value;
	}

	public static Email of(String value) {
		if (value == null || value.isEmpty()) {
			return new Email(EMPTY);
		}

		if (!PATTERN.matcher(value).matches()) {
			throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
		}

		if (value.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("이메일의 길이는 최대 50자까지 가능합니다.");
		}

		return new Email(value);
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Email that = (Email)o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

}
