package qna.domain.field;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {

	@Lob
	private String contents;

	protected Contents() {}

	public Contents(String contents) {
		this.contents = contents;
	}

	public String getContents() {
		return this.contents;
	}
}
