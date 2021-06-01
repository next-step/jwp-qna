package qna.domain.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserId {

	private final static int MAX_LENGTH = 20;

	@Column(unique = true, length = MAX_LENGTH, nullable = false)
	private String userId;

	protected UserId() { }

	private UserId(String userId) {
		validateLength(userId);
		this.userId = userId;
	}

	private void validateLength(String userId) {
		if (userId.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("유저 아이디 최대 길이를 초과 했습니다.");
		}
	}

	static UserId of(String userId) {
		return new UserId(userId);
	}

	String getUserId() {
		return userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserId other = (UserId)o;
		return Objects.equals(userId, other.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}
}
