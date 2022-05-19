package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    private Question q1;
    private Question q2;

    @BeforeEach
    void before() {
        //given
        User javajigi = userRepository.save(JAVAJIGI);
        User sanjigi = userRepository.save(SANJIGI);

        q1 = questionRepository.save(new Question(Q1.getTitle(), Q1.getContents()).writeBy(javajigi));
        q2 = questionRepository.save(new Question(Q2.getTitle(), Q2.getContents()).writeBy(sanjigi));

    }

    @Test
    @DisplayName("저장한 테스트 데이터를 모두 조회 한다.")
    void findAll() {
        //when
        List<Question> all = questionRepository.findAll();

        //then
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("특정 Title을 이용해 조회 한다.")
    void read() {
        //when
        Question findQuestion = questionRepository.findByTitle(q1.getTitle()).get();
        em.flush();
        em.clear();

        //then
        assertThat(findQuestion.getContents()).isEqualTo(q1.getContents());
    }

    @Test
    @DisplayName("특정 Id를 이용해 조회 한다.")
    void read_id() {
        //when
        Question findQuestion = questionRepository.findById(q1.getId()).get();
        em.flush();
        em.clear();

        //then
        assertThat(findQuestion.getContents()).isEqualTo(q1.getContents());
    }

    @Test
    @DisplayName("특정 Title의 레코드를 업데이트 한다.")
    void update() {
        //when
        Question findQuestion = questionRepository.findByTitle(q1.getTitle()).get();
        findQuestion.updateContents("updated content");

        em.flush();
        em.clear();

        Question updatedFindQuestion = questionRepository.findByTitle(q1.getTitle()).get();
        //then
        assertThat(updatedFindQuestion.getContents()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("특정 Id의 레코드를 업데이트 한다.")
    void update_id() {
        //when
        Question findQuestion = questionRepository.findById(q1.getId()).get();
        findQuestion.updateContents("updated content");

        em.flush();
        em.clear();

        Question updatedFindQuestion = questionRepository.findById(q1.getId()).get();
        //then
        assertThat(updatedFindQuestion.getContents()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("모든 테스트 데이터를 삭제 한다.")
    void delete() {
        //when
        assertThat(questionRepository.count()).isEqualTo(2);
        questionRepository.deleteAll();

        //then
        assertThat(questionRepository.count()).isZero();
    }
}
