package qna.domain.question;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

	private final int LIMITED_MAXIMUM_TEXT = 100;
	@Column(length = LIMITED_MAXIMUM_TEXT, nullable = false)
	private String value;

	public Title(String value) {
		setValue(value);
	}

	protected void setValue(String value) {
		if (Objects.isNull(value) || value.length() > LIMITED_MAXIMUM_TEXT){
			throw new IllegalArgumentException("Invalid Title value");
		}
		this.value = value;
	}

	protected Title() { }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Title))
			return false;
		Title title = (Title)o;
		return Objects.equals(value, title.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
