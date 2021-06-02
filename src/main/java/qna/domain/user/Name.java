package qna.domain.user;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

	private final int LIMITED_MAXIMUM_TEXT = 20;

	@Column(name = "name",length = LIMITED_MAXIMUM_TEXT, nullable = false)
	private String name;

	public Name() { }

	public Name(String name) {
		setName(name);
	}

	private void setName(String name) {
		if (Objects.isNull(name) || name.length() > LIMITED_MAXIMUM_TEXT){
			throw new IllegalArgumentException("Invalid Name value");
		}
		this.name = name;
	}
}
