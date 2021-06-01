package qna.domain.question;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {

	@Lob
	@Column(name = "contents")
	String value;

	public Contents(String value) {
		this.value = value;
	}

	protected Contents() { }
}
