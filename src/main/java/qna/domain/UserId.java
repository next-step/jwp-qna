package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserId {
	private static final int MAX_LENGTH = 20;

	@Column(name = "user_id", unique = true, nullable = false, length = MAX_LENGTH)
	private String value;

	protected UserId() {

	}

	private UserId(String value) {
		this.value = value;
	}

	public static UserId of(String value) {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException("사용자 ID는 빈 값일 수 없습니다.");
		}

		if (value.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("사용자 ID의 길이는 최대 20자까지 가능합니다.");
		}

		return new UserId(value);
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserId that = (UserId)o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
