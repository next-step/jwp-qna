package qna.domain.user;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

	@Column(name = "name",length = 20, nullable = false)
	private String name;

	public Name() { }

	public Name(String name) {
		setName(name);
	}

	private void setName(String name) {
		if (Objects.isNull(name) || name.length() > 20){
			throw new IllegalArgumentException("Invalid Name value");
		}
		this.name = name;
	}
}
