package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import qna.config.JpaAuditingConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
@DirtiesContext
class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("삭제 내역 저장 성공")
    void save() {
        //given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, writer.getId(), writer, LocalDateTime.now());
        entityManager.persist(writer);

        //expect
        assertThat(deleteHistoryRepository.save(deleteHistory)).isNotNull();
    }
}