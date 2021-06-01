package qna.domain.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {
	private static final int MAX_LENGTH = 20;

	@Column(length = MAX_LENGTH, nullable = false)
	private String password;

	protected Password() {}

	private Password(String password) {
		validateMaxLength(password);
		this.password = password;
	}

	private void validateMaxLength(String password) {
		if (password.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("유저 비밀번호의 최대 길이를 초과했습니다.");
		}
	}

	public static Password of(String password) {
		return new Password(password);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Password other = (Password)o;
		return Objects.equals(password, other.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(password);
	}
}
