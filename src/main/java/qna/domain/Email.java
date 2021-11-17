package qna.domain;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Embeddable;

@Embeddable
public class Email {

	public static final int MAX_LENGTH = 50;
	private static final Pattern EMAIL_REGEX_PATTERN = Pattern.compile("^(.+)@(.+)$");

	private String email;

	protected Email() {
	}

	private Email(String email) {
		this.email = email;
	}

	public static Email of(String email) {
		validateEmail(email);
		return new Email(email);
	}

	private static void validateEmail(String emailText) {
		if (emailText.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("이메일 길이는 50자 이내여야 합니다");
		}
		Matcher matcher = EMAIL_REGEX_PATTERN.matcher(emailText);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("이메일 양식에 맞지 않습니다");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Email email1 = (Email)o;

		return Objects.equals(email, email1.email);
	}

	@Override
	public int hashCode() {
		return email != null ? email.hashCode() : 0;
	}
}
