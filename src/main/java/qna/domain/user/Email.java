package qna.domain.user;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {


	private final Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");
	private final int LIMITED_MAXIMUM_TEXT = 40;

	@Column(name = "email", length = 40)
	private String value;

	public Email(String value) {
		setValue(value);
		this.value = value;
	}

	protected Email() {
	}

	private void setValue(String value) {
		if (value.length() > LIMITED_MAXIMUM_TEXT || emailPattern.asPredicate().test(value) == false){
			throw new IllegalArgumentException("Email 타입의 값이 아닙니다.");
		}

		this.value = value;
	}
}
