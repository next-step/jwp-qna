package qna.domain.vo;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.logging.log4j.util.Strings;

@Embeddable
public class UserId {

	private static final int MAX_LENGTH = 20;

	@Column(name = "user_id", nullable = false, length = 20, unique = true)
	private String userId;

	protected UserId() {
	}

	private UserId(String userId) {
		this.userId = userId;
	}

	public static UserId generate(String userId) {
		validateUserId(userId);
		return new UserId(userId);
	}

	private static void validateUserId(String userId) {
		validateIsNotNull(userId);
		validateIsNotBlank(userId);
		validateMaxLength(userId);
	}

	private static void validateIsNotBlank(String userId) {
		if (Strings.isBlank(userId)) {
			throw new IllegalArgumentException("유저 아이디는 비어있는 문자열이 아닙니다.");
		}
	}

	private static void validateMaxLength(String userId) {
		if (MAX_LENGTH < userId.getBytes().length) {
			throw new IllegalArgumentException("유저 아이디는 반드시 " + MAX_LENGTH + "byte 이하여야 합니다.");
		}
	}

	private static void validateIsNotNull(String userId) {
		if (Objects.isNull(userId)) {
			throw new IllegalArgumentException("유저 아이디는 반드시 입력되어야 합니다.");
		}
	}

	public String value() {
		return userId;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof UserId)) {
			return false;
		}
		UserId userId1 = (UserId) object;
		return Objects.equals(userId, userId1.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}
}
