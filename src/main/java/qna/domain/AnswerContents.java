package qna.domain;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

import org.springframework.util.StringUtils;

@Embeddable
public class AnswerContents {

	@Lob
	private String contents;

	protected AnswerContents() {}

	private AnswerContents(String contents) {
		this.contents = contents;
	}

	public static AnswerContents of(String contents) {
		return new AnswerContents(contents);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AnswerContents that = (AnswerContents)o;
		return Objects.equals(contents, that.contents);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contents);
	}

	public boolean isEmpty() {
		return !StringUtils.hasLength(this.contents);
	}
}
