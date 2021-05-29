package qna.domain;

import java.util.Date;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class BaseEntityListener {

    @PostLoad
    public void postLoad(BaseEntity baseEntity) {
        System.out.println("@PostLoad : " + baseEntity);
    }

    @PrePersist
    public void prePersist(BaseEntity baseEntity) {
        System.out.println("@PrePersist : " + baseEntity);
        baseEntity.setCreatedAt(new Date());
    }

    @PostPersist
    public void postPersist(BaseEntity baseEntity) {
        System.out.println("@PostPersist : " + baseEntity);
    }

    @PreUpdate
    public void preUpdate(BaseEntity baseEntity) {
        System.out.println("@PreUpdate : " + baseEntity);
    }

    @PostUpdate
    public void postUpdate(BaseEntity baseEntity) {
        System.out.println("@PostUpdate : " + baseEntity);
    }

    @PreRemove
    public void preRemove(BaseEntity baseEntity) {
        System.out.println("@PreRemove : " + baseEntity);
    }

    @PostRemove
    public void postRemove(BaseEntity baseEntity) {
        System.out.println("@PostRemove : " + baseEntity);
    }

}
