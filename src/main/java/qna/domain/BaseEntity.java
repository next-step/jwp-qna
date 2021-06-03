package qna.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners (AuditingEntityListener.class)
abstract class BaseEntity {
	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedBy
	private LocalDateTime updatedAt;
}
