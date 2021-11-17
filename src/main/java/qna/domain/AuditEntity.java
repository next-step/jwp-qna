package qna.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditEntity {

	@Column(nullable = false)
	@CreationTimestamp
	protected LocalDateTime createdAt;

	@Column
	@UpdateTimestamp
	protected LocalDateTime updatedAt;
	
}
