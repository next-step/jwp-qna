package qna.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Deleted {

	private boolean deletedBool;

	protected Deleted() {
	}

	private Deleted(boolean deletedBool) {
		this.deletedBool = deletedBool;
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

		return deletedBool == deleted1.deletedBool;
	}

	@Override
	public int hashCode() {
		return (deletedBool ? 1 : 0);
	}

	public boolean toBoolean() {
		return this.deletedBool;
	}

}
