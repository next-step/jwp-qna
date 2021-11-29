package qna.domain.field;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {

	@Column(name = "user_id", length = 20, nullable = false, unique = true)
	private String userId;

	protected UserId() {}

	public UserId(String userId) {
		this.userId = userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		UserId userId1 = (UserId) o;
		return Objects.equals(this.userId, userId1.userId);
	}
}
