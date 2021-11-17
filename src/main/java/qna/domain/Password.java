package qna.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Password {

	public static final int MAX_LENGTH = 20;

	private String password;

	protected Password() {
	}

	private Password(String password) {
		this.password = password;
	}

	public static Password of(String passwordText) {
		validate(passwordText);
		return new Password(passwordText);
	}

	public static void validate(String passwordText) {
		if (Objects.isNull(passwordText)) {
			throw new IllegalArgumentException("null 이면 안됩니다");
		}

		if (passwordText.length() > 20) {
			throw new IllegalArgumentException("길이 20 이하여야 합니다");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Password password = (Password)o;

		return Objects.equals(this.password, password.password);
	}

	@Override
	public int hashCode() {
		return password != null ? password.hashCode() : 0;
	}
}
