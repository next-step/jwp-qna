package qna.domain;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {

	@Lob
	private String content;

	protected Contents() {
	}

	public Contents(String content) {
		this.content = content;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Contents contents = (Contents)o;

		return Objects.equals(content, contents.content);
	}

	@Override
	public int hashCode() {
		return content != null ? content.hashCode() : 0;
	}
	
}
