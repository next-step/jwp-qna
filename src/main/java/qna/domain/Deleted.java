package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Deleted {

	@Column(name = "deleted")
	private boolean deleted = false;

	public void delete() {
		this.deleted = true;
	}

	public boolean isDeleted() {
		return this.deleted == true;
	}
}
