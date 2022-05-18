package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @Test
    @DisplayName("저장한 테스트 데이터를 모두 조회 한다.")
    void findAll() {
        //given
        repository.save(A1);
        repository.save(A2);

        //when
        List<Answer> all = repository.findAll();

        //then
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("특정 Id의 레코드를 조회 한다.")
    void read_id() {
        //given
        Answer savedAnswer = repository.save(A1);
        em.flush();
        em.clear();

        //when
        Answer findAnswer = repository.findById(savedAnswer.getId()).get();

        //then
        assertThat(findAnswer.getId()).isEqualTo(savedAnswer.getId());
    }

    @Test
    @DisplayName("특정 Contents의 레코드를 조회 한다.")
    void read() {
        //given
        Answer save = repository.save(A1);
        //when
        Answer findAnswer = repository.findByContentsEquals(save.getContents()).get();

        //then
        assertThat(findAnswer.getContents()).isEqualTo(save.getContents());
    }

    @Test
    @DisplayName("특정 Id의 레코드를 업데이트 한다.")
    void update_id() {
        //given
        Answer save = repository.save(A1);
        Answer findAnswer = repository.findById(save.getId()).get();

        //when
        findAnswer.setContents("updated content");

        em.flush();
        em.clear();
        Answer updatedAnswer = repository.findById(save.getId()).get();

        //then
        assertThat(updatedAnswer.getContents()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("특정 Contents의 레코드를 업데이트 한다.")
    void update() {
        //given
        Answer save = repository.save(A1);

        //when
        Answer findAnswer = repository.findByContentsEquals(save.getContents()).get();

        findAnswer.setContents("updated content");

        Answer updatedAnswer = repository.findByContentsEquals("updated content").get();

        //then
        assertThat(updatedAnswer.getContents()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("모든 테스트 데이터를 삭제 한다.")
    void delete() {
        //given
        repository.save(A1);
        repository.save(A2);

        //when
        assertThat(repository.count()).isEqualTo(2);
        repository.deleteAll();

        //then
        assertThat(repository.count()).isZero();
    }
}
