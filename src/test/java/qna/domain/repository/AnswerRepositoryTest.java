package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository repository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void before() {
        repository.save(A1);
        repository.save(A2);
    }

    @Test
    @DisplayName("저장한 테스트 데이터를 모두 조회 한다.")
    void findAll() {
        List<Answer> all = repository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("특정 Contents를 조회 한다.")
    void read() {
        //when
        Answer findAnswer = repository.findByContentsEquals("Answers Contents1").get();

        //then
        assertThat(findAnswer).isNotNull();
    }

    @Test
    @DisplayName("특정 Contents의 레코드를 업데이트 한다.")
    void update() {
        Answer findAnswer = repository.findByContentsEquals("Answers Contents1").get();

        findAnswer.setContents("updated content");

        Answer updatedAnswer = repository.findByContentsEquals("updated content").get();

        assertThat(updatedAnswer.getContents()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("모든 테스트 데이터를 삭제 한다.")
    void delete() {
        assertThat(repository.count()).isEqualTo(2);

        repository.deleteAll();

        assertThat(repository.count()).isZero();
    }
}
