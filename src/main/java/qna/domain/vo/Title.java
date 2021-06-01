package qna.domain.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.StringUtils;

@Embeddable
public class Title {

	private final int MAX_LENGTH = 100;

	@Column(nullable = false, length = MAX_LENGTH)
	private String title;

	protected Title() { }

	private Title(String title) {
		validateHasText(title);
		validateMaxLength(title);
		this.title = title;
	}

	private void validateHasText(String title) {
		if (!StringUtils.hasText(title)) {
			throw new IllegalArgumentException("제목은 빈 텍스트일 수 없습니다.");
		}
	}

	private void validateMaxLength(String title) {
		if (title.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("제목의 최대 길이를 넘었습니다.");
		}
	}

	public static Title of(String title) {
		return new Title(title);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Title title1 = (Title)o;
		return Objects.equals(title, title1.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title);
	}
}
