package qna.domain.user;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserId {

	@Column(name = "userId",length = 20, nullable = false, unique = true)
	private String value;

	public UserId(String value) {
		setValue(value);
	}

	private void setValue(String value) {
		if (Objects.isNull(value) || value.length() > 20){
			throw new IllegalArgumentException("Invalid UserId value");
		}
		this.value = value;
	}

	public UserId() { }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UserId))
			return false;
		UserId userId = (UserId)o;
		return Objects.equals(value, userId.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	public String getValue() {
		return value;
	}
}
