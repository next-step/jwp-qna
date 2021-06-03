package qna.domain.answer;

import static java.util.Objects.*;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {

	@Lob
	@Column(name = "contents")
	String value;

	public Contents(String value) {
		requireNonNull(value, "Found Null Value in Contents");
		this.value = value;
	}

	protected Contents() { }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Contents))
			return false;
		Contents contents = (Contents)o;
		return Objects.equals(value, contents.value);
	}

	@Override
	public int hashCode() {
		return hash(value);
	}
}
