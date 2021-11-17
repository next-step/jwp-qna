package qna.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Title {

	private String text;

	private static final int MAX_LENGTH = 100;

	protected Title() {
	}

	private Title(String text) {
		this.text = text;
	}

	public static Title of(String text) {
		if (text.length() > MAX_LENGTH) {
			throw new IllegalArgumentException(
				ErrorCode.TITLE_INIT_EXCEED_MAX_LENGTH.getMessage());
		}
		return new Title(text);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Title title = (Title)o;

		return Objects.equals(text, title.text);
	}

	@Override
	public int hashCode() {
		return text != null ? text.hashCode() : 0;
	}

	public String toString() {
		return this.text;
	}

}
