package qna.domain.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {
	private static final int MAX_LENGTH = 20;

	@Column(length = MAX_LENGTH, nullable = false)
	private String name;

	protected Name() {}

	private Name(String name) {
		validateLength(name);
		this.name = name;
	}

	private void validateLength(String name) {
		if (name.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("이름의 최대 길이를 초과했습니다.");
		}
	}

	public static Name of(String name) {
		return new Name(name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Name other = (Name)o;
		return Objects.equals(name, other.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
