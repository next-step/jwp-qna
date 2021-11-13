package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class QuestionTitle {
	private static final int MAX_LENGTH = 100;

	@Column(name = "title", nullable = false, length = MAX_LENGTH)
	private String value;

	protected QuestionTitle() {

	}

	private QuestionTitle(String value) {
		this.value = value;
	}

	public static QuestionTitle of(String value) {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException("질문 제목은 빈 값일 수 없습니다.");
		}

		if (value.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("질문 제목의 길이는 최대 100자까지 가능합니다.");
		}

		return new QuestionTitle(value);
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
		QuestionTitle that = (QuestionTitle)o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
