package qna.domain.user;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {

	private static final Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");

	@Column(name = "email", length = 40)
	private String value;

	public Email(String value) {
		setValue(value);
		this.value = value;
	}

	protected Email() {
	}

	private void setValue(String value) {
		if (value.length() > 40 || emailPattern.asPredicate().test(value) == false){
			throw new IllegalArgumentException("Email 타입의 값이 아닙니다.");
		}

		this.value = value;
	}
}
