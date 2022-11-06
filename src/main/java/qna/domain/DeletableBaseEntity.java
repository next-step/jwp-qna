package qna.domain;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class DeletableBaseEntity extends BaseEntity {

	@Column(nullable = false)
	private boolean deleted = false;

	protected void delete() {
		this.deleted = true;
	}

	public boolean isDeleted() {
		return deleted;
	}
}
