package qna.domain.vo;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

	private static final int MAX_LENGTH = 20;

	@Column(nullable = false, length = MAX_LENGTH)
	private String name;

	protected Name() {
	}

	protected Name(String name) {
		this.name = name;
	}

	public static Name generate(String name) {
		validateName(name);
		return new Name(name);
	}

	private static void validateName(String name) {
		validateIsNotNull(name);
		validateMaxLength(name);
	}

	private static void validateMaxLength(String name) {
		if (MAX_LENGTH < name.getBytes().length) {
			throw new IllegalArgumentException("이름은 반드시 " + MAX_LENGTH + "byte 이하여야 합니다.");
		}
	}

	private static void validateIsNotNull(String name) {
		if (Objects.isNull(name)) {
			throw new IllegalArgumentException("이름은 반드시 입력되어야 합니다.");
		}
	}

	public String value() {
		return name;
	}

	public void changeName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Name)) {
			return false;
		}
		Name name1 = (Name) object;
		return Objects.equals(name, name1.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
