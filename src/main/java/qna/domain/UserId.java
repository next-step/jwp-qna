package qna.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class UserId {

	public static final int MAX_LENGTH = 20;

	private String userId;

	protected UserId() {
	}

	private UserId(String userId) {
		this.userId = userId;
	}

	public static UserId of(String userIdText) {
		validate(userIdText);
		return new UserId(userIdText);
	}

	public static void validate(String userIdText) {
		if (Objects.isNull(userIdText)) {
			throw new IllegalArgumentException("null일 수 없습니다");
		}

		if (userIdText.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("길이 " + MAX_LENGTH + "을 초과할 수 없습니다");
		}
	}

	public String toString() {
		return this.userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		UserId userId = (UserId)o;

		return Objects.equals(this.userId, userId.userId);
	}

	@Override
	public int hashCode() {
		return userId != null ? userId.hashCode() : 0;
	}
}
