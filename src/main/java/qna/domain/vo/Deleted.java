package qna.domain.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Deleted {
	public static final Deleted TRUE = new Deleted(true);
	public static final Deleted FALSE = new Deleted(false);

	@Column(nullable = false)
	private boolean deleted = false;

	protected Deleted() { }

	private Deleted(boolean deleted) {
		this.deleted = deleted;
	}

	public static Deleted of(boolean deleted) {
		if (deleted) {
			return TRUE;
		}
		return FALSE;
	}

	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Deleted other = (Deleted)o;
		return deleted == other.deleted;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deleted);
	}
}
