package qna.domain.vo;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Deleted {

	@Column(nullable = false)
	private Boolean deleted = Boolean.FALSE;

	protected Deleted() {
	}

	public static Deleted generate() {
		return new Deleted();
	}

	public Boolean value() {
		return deleted;
	}

	public void changeDeleted() {
		this.deleted = Boolean.TRUE;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Deleted)) {
			return false;
		}
		Deleted deleted1 = (Deleted) object;
		return Objects.equals(deleted, deleted1.deleted);
	}

	@Override
	public int hashCode() {
		return Objects.hash(deleted);
	}
}
