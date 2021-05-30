package qna.domain.vo;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class UserAuthentication {

	@Embedded
	private UserId userId;

	@Embedded
	private Password password;

	protected UserAuthentication() { }

	private UserAuthentication(String userId, String password) {
		this.userId = UserId.of(userId);
		this.password = Password.of(password);
	}

	public static UserAuthentication of(String userId, String password) {
		return new UserAuthentication(userId, password);
	}

	public String getUserId() {
		return userId.getUserId();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserAuthentication that = (UserAuthentication)o;
		return Objects.equals(userId, that.userId) && Objects.equals(password, that.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, password);
	}

	@Override
	public String toString() {
		return "UserAuthentication{" +
			"userId='" + userId + '\'' +
			", password='" + password + '\'' +
			'}';
	}
}
