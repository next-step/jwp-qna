package qna.domain;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Embeddable;

@Embeddable
public class Email {

	private static final int MAX_LENGTH = 50;
	private static final Pattern EMAIL_REGEX_PATTERN = Pattern.compile("^(.+)@(.+)$");

	private String emailText;

	protected Email() {
	}

	private Email(String emailText) {
		this.emailText = emailText;
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

		return Objects.equals(emailText, email1.emailText);
	}

	@Override
	public int hashCode() {
		return emailText != null ? emailText.hashCode() : 0;
	}
}
