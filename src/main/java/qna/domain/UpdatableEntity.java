package qna.domain;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class UpdatableEntity extends AbstractEntity {

	protected LocalDateTime updatedAt;

}
