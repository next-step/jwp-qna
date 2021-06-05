package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Deleted {

	@Column(name = "deleted", nullable = false)
	private boolean isDeleted = false;

	protected Deleted() {
	}

	public void delete() {
		this.isDeleted = true;
	}

	public boolean isDeleted() {
		return isDeleted;
	}
}
