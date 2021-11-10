package qna.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime {

	@CreatedDate
	@Column(name = "create_at", nullable = false)
	private LocalDateTime create = LocalDateTime.now();

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updated = LocalDateTime.now();

}
