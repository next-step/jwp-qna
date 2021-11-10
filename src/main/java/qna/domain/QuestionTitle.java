package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class QuestionTitle {
	public static final int CONTENTS_MAX_LENGTH = 100;
	public static final int CONTENTS_MIN_LENGTH = 0;
	private static final String INVALID_CONTENTS_GUIDE_MESSAGE = "contents 는 null 또는 100자를 초과한 값을 가질 수 없습니다. contents=%s";

	@Column(length = CONTENTS_MAX_LENGTH, nullable = false)
	private String title;

	protected QuestionTitle() {}

	private QuestionTitle(String title) {
		this.title = title;
	}

	public static QuestionTitle of(String title) {
		validateContents(title);
		return new QuestionTitle(title);
	}

	public int getLength() {
		return this.title.length();
	}

	private static void validateContents(String title) {
		if (title == null || CONTENTS_MAX_LENGTH < title.length()) {
			throw new IllegalArgumentException(String.format(INVALID_CONTENTS_GUIDE_MESSAGE, title));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		QuestionTitle that = (QuestionTitle)o;
		return Objects.equals(title, that.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title);
	}
}
