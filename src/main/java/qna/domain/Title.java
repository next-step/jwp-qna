package qna.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Title {

	private String title;

	public static final int MAX_LENGTH = 100;

	protected Title() {
	}

	private Title(String title) {
		this.title = title;
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

		return Objects.equals(this.title, title.title);
	}

	@Override
	public int hashCode() {
		return title != null ? title.hashCode() : 0;
	}

	public String toString() {
		return this.title;
	}

}
