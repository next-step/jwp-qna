package qna.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

	@Column(name = "name",length = 20, nullable = false)
	private String name;

	public Name(String name) {
		this.name = name;
	}

	public Name() { }
}
