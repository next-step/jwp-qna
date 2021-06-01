package qna.domain.answer;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.lang.NonNull;

@Embeddable
public class Deleted {

	@NonNull
	@Column(name = "deleted", nullable = false)
	boolean value;

	public Deleted() { }

	public Deleted(boolean value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Deleted))
			return false;
		Deleted deleted = (Deleted)o;
		return value == deleted.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
