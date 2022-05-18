package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository repository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("저장한 테스트 데이터를 모두 조회 한다.")
    void findAll() {
        //given
        repository.save(Q1);
        repository.save(Q2);

        //when
        List<Question> all = repository.findAll();

        //then
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("특정 Title을 이용해 조회 한다.")
    void read() {
        //given
        Question save = repository.save(Q1);

        //when
        Question findQuestion = repository.findByTitle(save.getTitle()).get();
        em.flush();
        em.clear();

        //then
        assertThat(findQuestion.getContents()).isEqualTo(save.getContents());
    }

    @Test
    @DisplayName("특정 Id를 이용해 조회 한다.")
    void read_id() {
        //given
        Question save = repository.save(Q1);

        //when
        Question findQuestion = repository.findById(save.getId()).get();
        em.flush();
        em.clear();

        //then
        assertThat(findQuestion.getContents()).isEqualTo(save.getContents());
    }

    @Test
    @DisplayName("특정 Title의 레코드를 업데이트 한다.")
    void update() {
        //given
        Question save = repository.save(Q1);

        //when
        Question findQuestion = repository.findByTitle(save.getTitle()).get();
        findQuestion.setContents("updated content");

        em.flush();
        em.clear();

        Question updatedFindQuestion = repository.findByTitle(save.getTitle()).get();
        //then
        assertThat(updatedFindQuestion.getContents()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("특정 Id의 레코드를 업데이트 한다.")
    void update_id() {
        //given
        Question save = repository.save(Q1);

        //when
        Question findQuestion = repository.findById(save.getId()).get();
        findQuestion.setContents("updated content");

        em.flush();
        em.clear();

        Question updatedFindQuestion = repository.findById(save.getId()).get();
        //then
        assertThat(updatedFindQuestion.getContents()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("모든 테스트 데이터를 삭제 한다.")
    void delete() {
        //given
        repository.save(Q1);
        repository.save(Q2);

        //when
        assertThat(repository.count()).isEqualTo(2);
        repository.deleteAll();

        //then
        assertThat(repository.count()).isZero();
    }
}
