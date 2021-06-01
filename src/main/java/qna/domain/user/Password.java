package qna.domain.user;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {

	@Column(name = "password",length = 20, nullable = false)
	private String value;

	protected Password() { }

	public Password(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Password))
			return false;
		Password password = (Password)o;
		return Objects.equals(value, password.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
