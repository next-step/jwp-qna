package qna.domain.vo;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {
	private static final int MAX_LENGTH = 50;
	private static final Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");

	@Column(length = MAX_LENGTH)
	private String email;

	protected Email() { }

	Email(String email) {
		validateLength(email);
		validatePattern(email);
		this.email = email;
	}

	private void validateLength(String email) {
		if (email.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("이메일의 최대 길이값을 초과했습니다.");
		}
	}

	private void validatePattern(String email) {
		Matcher matcher = emailPattern.matcher(email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("이메일 형식이 잘못됐습니다.");
		}
	}

	static Email of(String email) {
		return new Email(email);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Email other = (Email)o;
		return Objects.equals(email, other.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}
}
