package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class QuestionContents {

	@Column(name = "contents")
	@Lob
	private String value;

	protected QuestionContents() {

	}

	private QuestionContents(String value) {
		this.value = value;
	}

	public static QuestionContents of(String value) {
		return new QuestionContents(value);
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
		QuestionContents that = (QuestionContents)o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
