package qna.repository;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;
    @Autowired
    private QuestionRepository questions;
    @Autowired
    private UserRepository users;
    @Autowired
    private EntityManager entityManager;

    private Answer answer;
    private Question question;
    private User user;

    @BeforeEach
    void init() {
        question = new Question("test_title", "contents");
        questions.save(question);
        user = new User("admin", "password", "seungki", "seungki1993@naver.com");
        users.save(user);
        answer = new Answer(user, question, "contents");
        answers.save(answer);
        entityManager.flush();
        entityManager.clear();
    }


    @Test
    @DisplayName("answer 저장")
    void answer_save() {
        Answer expect = answers.findById(answer.getId()).orElseThrow(NotFoundException::new);
        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(expect.getId()),
                () -> assertThat(answer.getWriter()).isEqualTo(Hibernate.unproxy(expect.getWriter())),
                () -> assertThat(answer.getQuestion()).isEqualTo(Hibernate.unproxy(expect.getQuestion())),
                () -> assertThat(answer.getContents()).isEqualTo(expect.getContents()),
                () -> assertThat(answer.isDeleted()).isEqualTo(expect.isDeleted())
        );
    }

    @Test
    @DisplayName("answer의 question lazy 로딩 확인")
    void answer_to_question_proxy() {
        Answer expect = answers.findById(answer.getId()).orElseThrow(NotFoundException::new);
        Question dbQuestion = expect.getQuestion();
        assertThat(Hibernate.isInitialized(dbQuestion)).isFalse();
    }

    @Test
    @DisplayName("answer의 user lazy 로딩 확인")
    void answer_to_user_proxy() {
        Answer expect = answers.findById(answer.getId()).orElseThrow(NotFoundException::new);
        User dbUser = expect.getWriter();
        assertThat(Hibernate.isInitialized(dbUser)).isFalse();
    }

    @Test
    @DisplayName("answer 삭제(deleted 상태 변경)")
    void answer_delete() {
        Answer dbSave = answers.findById(answer.getId()).orElseThrow(NotFoundException::new);
        dbSave.setDeleted(true);
        entityManager.flush();
        entityManager.clear();
        Answer expect = answers.findById(answer.getId()).orElseThrow(NotFoundException::new);
        assertTrue(expect.isDeleted());
    }

    @Test
    @DisplayName("question id에 일치하는 삭제되지 않은 Answer을 조회")
    void find_question_id_and_delete_false() {
        List<Answer> expect = answers.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(expect).hasSize(1).containsExactly(answer);
    }

    @Test
    @DisplayName("question id에 일치하는 삭제되지 않은 Answer을 조회(해당 결과 없음 일차하는 questionId가 없음)")
    void find_question_id_and_delete_false_return_nothing() {
        Question diffQuestion = new Question("diff", "diff");
        questions.save(diffQuestion);
        List<Answer> expect = answers.findByQuestionIdAndDeletedFalse(diffQuestion.getId());
        assertThat(expect).isEmpty();
    }

    @Test
    @DisplayName("answer id에 일치하는 삭제되지 않은 Answer을 조회")
    void find_answer_id_and_delete_false() {
        Answer expect = answers.findByIdAndDeletedFalse(answer.getId()).orElseThrow(NotFoundException::new);
        assertThat(answer).isEqualTo(expect);
    }

    @Test
    @DisplayName("answer id에 일치하는 삭제되지 않은 Answer을 조회(삭제된 Answer 이라 조회결과 없음)")
    void find_answer_id_and_delete_false_return_nothing() {
        Answer dbSave = answers.findById(answer.getId()).orElseThrow(NotFoundException::new);
        dbSave.setDeleted(true);
        entityManager.flush();
        entityManager.clear();
        assertThat(answers.findByIdAndDeletedFalse(answer.getId())).isEmpty();
    }

}
