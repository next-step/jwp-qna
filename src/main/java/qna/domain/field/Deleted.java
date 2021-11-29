package qna.domain.field;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Deleted {

	@Column(nullable = false)
	private boolean deleted = false;

	protected Deleted() {}

	public Deleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean getDeleted() {
		return this.deleted;
	}
}
