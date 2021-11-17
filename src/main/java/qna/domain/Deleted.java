package qna.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Deleted {

	private boolean deleted;

	protected Deleted() {
	}

	private Deleted(boolean deleted) {
		this.deleted = deleted;
	}

	public static Deleted of(boolean deleted) {
		return new Deleted(deleted);
	}

	public static Deleted ofFalse() {
		return new Deleted(false);
	}

	public static Deleted ofTure() {
		return new Deleted(true);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Deleted deleted1 = (Deleted)o;

		return deleted == deleted1.deleted;
	}

	@Override
	public int hashCode() {
		return (deleted ? 1 : 0);
	}

	public boolean toBoolean() {
		return this.deleted;
	}

}
