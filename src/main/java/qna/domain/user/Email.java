package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {

	@Column(name = "email", length = 40)
	private String value;

	public Email(String value) {
		this.value = value;
	}

	protected Email() {
	}
}
