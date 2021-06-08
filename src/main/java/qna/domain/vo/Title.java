package qna.domain.vo;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

	private static final int MAX_LENGTH = 100;

	@Column(nullable = false, length = MAX_LENGTH)
	private String title;

	protected Title() {
	}

	protected Title(String title) {
		this.title = title;
	}

	public static Title generate(String title) {
		validateTitleIsNotNull(title);
		validateMaxLength(title);
		return new Title(title);
	}

	private static void validateTitleIsNotNull(String title) {
		if (Objects.isNull(title)) {
			throw new IllegalArgumentException("제목은 반드시 null이 아니어야 합니다.");
		}
	}

	private static void validateMaxLength(String title) {
		if (MAX_LENGTH < title.getBytes().length) {
			throw new IllegalArgumentException("제목은 반드시 " + MAX_LENGTH + "byte 이하여야 합니다.");
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Title)) {
			return false;
		}
		Title title1 = (Title) object;
		return Objects.equals(title, title1.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title);
	}
}
