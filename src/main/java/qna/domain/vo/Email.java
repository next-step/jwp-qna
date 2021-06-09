package qna.domain.vo;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {

	private static final int MAX_LENGTH = 50;

	@Column(length = MAX_LENGTH)
	private String email;

	protected Email() {
	}

	private Email(String email) {
		this.email = email;
	}

	public static Email generate(String email) {
		validateEmailNotOverFiftyCharacter(email);
		return new Email(email);
	}

	private static void validateEmailNotOverFiftyCharacter(String email) {
		if (MAX_LENGTH < email.getBytes().length) {
			throw new IllegalArgumentException("이메일 길이는 " + MAX_LENGTH + "byte 이하여야 합니다.");
		}
	}

	public void changeEmail(String email) {
		validateEmailNotOverFiftyCharacter(email);
		this.email = email;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Email)) {
			return false;
		}
		Email email1 = (Email) object;
		return Objects.equals(email, email1.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}
}
