package qna.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;

public class BaseEntity {

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
