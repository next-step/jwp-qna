package qna.domain.field;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Email {

	@Column(length = 50)
	private String email;

	protected Email() {}

	public Email(String email) {
		this.email = email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Email email1 = (Email) o;
		return Objects.equals(this.email, email1.email);
	}
}
