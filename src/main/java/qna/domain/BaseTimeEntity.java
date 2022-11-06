package qna.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

//DB에 저장하거나 업데이트 하는 경우 자동으로 데이터베이스에 반영해주는 ANNOTATION
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseTimeEntity {
    //Entity 생성 시간 자동 저장
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    //Entity값 수정한 시간 자동 저장
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
