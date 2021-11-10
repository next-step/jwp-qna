package qna.domain;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

import org.springframework.util.StringUtils;

@Embeddable
public class Contents {

	@Lob
	private String contents;

	protected Contents() {}

	private Contents(String contents) {
		this.contents = contents;
	}

	public static Contents of(String contents) {
		return new Contents(contents);
	}

	public boolean isEmpty() {
		return !StringUtils.hasLength(this.contents);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Contents that = (Contents)o;
		return Objects.equals(contents, that.contents);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contents);
	}
}
