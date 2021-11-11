package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class AnswerContents {

	@Column(name = "contents")
	@Lob
	private String value;

	protected AnswerContents() {

	}

	private AnswerContents(String value) {
		this.value = value;
	}

	public static AnswerContents of(String value) {
		return new AnswerContents(value);
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
		AnswerContents that = (AnswerContents)o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
