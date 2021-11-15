package qna.domain;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
public abstract class BaseTimeEntity {

  @CreatedDate
  @Column(updatable = false)
  private Timestamp createdAt;

  @LastModifiedDate
  private Timestamp updatedAt;
}
