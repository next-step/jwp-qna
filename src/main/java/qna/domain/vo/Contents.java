package qna.domain.vo;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {

	@Lob
	private String contents;

	protected Contents() { }

	private Contents(String contents) {
		this.contents = contents;
	}

	public static Contents of(String contents) {
		return new Contents(contents);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Contents other = (Contents)o;
		return Objects.equals(contents, other.contents);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contents);
	}
}
