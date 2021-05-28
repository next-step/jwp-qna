package qna;

import javax.persistence.EntityManager;

public class EntityManagerHelper {
    private final EntityManager entityManager;

    public EntityManagerHelper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
