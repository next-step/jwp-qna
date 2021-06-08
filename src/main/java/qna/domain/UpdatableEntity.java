package qna.domain;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class UpdatableEntity extends AbstractEntity {

	@UpdateTimestamp
	protected LocalDateTime updatedAt;

}
