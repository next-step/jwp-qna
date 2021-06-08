package qna.domain.vo;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {

	@Column
	@Lob
	private String contents;

	protected Contents() {
	}

	protected Contents(String contents) {
		this.contents = contents;
	}

	public static Contents generate(String contents) {
		return new Contents(contents);
	}

	public String value() {
		return contents;
	}

	public void changeContents(String contents) {
		this.contents = contents;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Contents)) {
			return false;
		}
		Contents contents1 = (Contents) object;
		return Objects.equals(contents, contents1.contents);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contents);
	}
}
