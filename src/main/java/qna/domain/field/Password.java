package qna.domain.field;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {

	@Column(length = 20, nullable = false)
	private String password;

	protected Password() {}

	public Password(String password) {
		this.password = password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Password password1 = (Password) o;
		return Objects.equals(this.password, password1.password);
	}
}
