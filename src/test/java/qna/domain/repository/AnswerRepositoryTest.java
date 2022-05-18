package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
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
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    private Answer a1;
    private Answer a2;

    @BeforeEach
    void before() {
        //given
        User javajigi = userRepository.save(JAVAJIGI);
        User sanjigi = userRepository.save(SANJIGI);

        Question q1 = questionRepository.save(new Question(Q1.getTitle(), Q1.getContents()).writeBy(javajigi));
        Question q2 = questionRepository.save(new Question(Q2.getTitle(), Q2.getContents()).writeBy(sanjigi));

        a1 = answerRepository.save(new Answer(javajigi, q1, A1.getContents()));
        a2 = answerRepository.save(new Answer(sanjigi, q2, A2.getContents()));
    }



    @Test
    @DisplayName("저장한 테스트 데이터를 모두 조회 한다.")
    void findAll() {
        //when
        List<Answer> all = answerRepository.findAll();

        //then
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("특정 Id의 레코드를 조회 한다.")
    void read_id() {
        //when
        Answer findAnswer = answerRepository.findById(a1.getId()).get();

        //then
        assertThat(findAnswer.getId()).isEqualTo(a1.getId());
    }

    @Test
    @DisplayName("특정 Contents의 레코드를 조회 한다.")
    void read() {
        //when
        Answer findAnswer = answerRepository.findByContentsEquals(a1.getContents()).get();

        //then
        assertThat(findAnswer.getContents()).isEqualTo(a1.getContents());
    }

    @Test
    @DisplayName("특정 Id의 레코드를 업데이트 한다.")
    void update_id() {
        Answer findAnswer = answerRepository.findById(a1.getId()).get();

        //when
        findAnswer.setContents("updated content");

        em.flush();
        em.clear();
        Answer updatedAnswer = answerRepository.findById(a1.getId()).get();

        //then
        assertThat(updatedAnswer.getContents()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("특정 Contents의 레코드를 업데이트 한다.")
    void update() {
        //when
        Answer findAnswer = answerRepository.findByContentsEquals(a1.getContents()).get();

        findAnswer.setContents("updated content");

        Answer updatedAnswer = answerRepository.findByContentsEquals("updated content").get();

        //then
        assertThat(updatedAnswer.getContents()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("모든 테스트 데이터를 삭제 한다.")
    void delete() {
        //when
        assertThat(answerRepository.count()).isEqualTo(2);
        answerRepository.deleteAll();

        //then
        assertThat(answerRepository.count()).isZero();
    }
}
