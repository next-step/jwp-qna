package qna.domain.vo;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.logging.log4j.util.Strings;

@Embeddable
public class Password {

	private static final int MAX_LENGTH = 20;

	@Column(nullable = false, length = MAX_LENGTH)
	private String password;

	protected Password() {
	}

	private Password(String password) {
		this.password = password;
	}

	public static Password generate(String password) {
		validatePassword(password);
		return new Password(password);
	}

	private static void validatePassword(String password) {
		validateIsNotNull(password);
		validateIsNotBlank(password);
		validateMaxLength(password);
	}

	private static void validateIsNotBlank(String password) {
		if (Strings.isBlank(password)) {
			throw new IllegalArgumentException("비밀번호는 빈 문자열이 아닙니다.");
		}
	}

	private static void validateMaxLength(String password) {
		if (MAX_LENGTH < password.getBytes().length) {
			throw new IllegalArgumentException("비밀번호는 반드시 " + MAX_LENGTH + "byte 이하여야 합니다.");
		}
	}

	private static void validateIsNotNull(String password) {
		if (Objects.isNull(password)) {
			throw new IllegalArgumentException("비밀번호는 반드시 입력되어야 합니다.");
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Password)) {
			return false;
		}
		Password password1 = (Password) object;
		return Objects.equals(password, password1.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(password);
	}
}
