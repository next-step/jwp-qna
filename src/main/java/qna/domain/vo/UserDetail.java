package qna.domain.vo;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class UserDetail {
	@Embedded
	private Name name;

	@Embedded
	private Email email;

	protected UserDetail() {}

	private UserDetail(String name, String email) {
		this.name = Name.of(name);
		this.email = Email.of(email);
	}

	public static UserDetail of(String name, String email) {
		return new UserDetail(name, email);
	}

	@Override
	public String toString() {
		return "UserDetail{" +
			"name='" + name + '\'' +
			", email='" + email + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetail that = (UserDetail)o;
		return Objects.equals(name, that.name) && Objects.equals(email, that.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, email);
	}
}
