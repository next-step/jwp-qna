package qna.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class UserName {

	public static final int MAX_LENGTH = 20;

	private String name;

	protected UserName() {
	}

	private UserName(String name) {
		this.name = name;
	}

	public static UserName of(String name) {
		validate(name);
		return new UserName(name);
	}

	private static void validate(String name) {
		if (name.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("길이(20)를 초과하였습니다");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		UserName userName = (UserName)o;

		return Objects.equals(name, userName.name);
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
