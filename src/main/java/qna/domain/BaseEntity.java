package qna.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

  @Column(nullable= false)
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
