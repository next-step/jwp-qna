package qna.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    BaseEntity(){}

    BaseEntity(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
