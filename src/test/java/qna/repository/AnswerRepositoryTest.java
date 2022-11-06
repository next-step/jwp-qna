package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;
    @Autowired
    private QuestionRepository questions;
    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("answer 저장")
    void answer_save() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answers.saveAndFlush(answer);
        entityManager.clear();
        Answer expect = answers.findById(answer.getId()).orElseThrow(NotFoundException::new);
        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(expect.getId()),
                () -> assertThat(answer.getWriterId()).isEqualTo(expect.getWriterId()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(expect.getQuestionId()),
                () -> assertThat(answer.getContents()).isEqualTo(expect.getContents()),
                () -> assertThat(answer.isDeleted()).isEqualTo(expect.isDeleted())
        );
    }

    @Test
    @DisplayName("answer 삭제(deleted 상태 변경)")
    void answer_delete() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answers.saveAndFlush(answer);
        answer.setDeleted(true);
        entityManager.flush();
        entityManager.clear();
        Answer expect = answers.findById(answer.getId()).orElseThrow(NotFoundException::new);
        assertTrue(expect.isDeleted());
    }

    @Test
    @DisplayName("question id에 일치하는 삭제되지 않은 Answer을 조회")
    void find_question_id_and_delete_false() {
        Question question = questions.saveAndFlush(new Question("title", "contents"));
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");
        answers.saveAndFlush(answer);
        entityManager.clear();
        List<Answer> expect = answers.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(expect).hasSize(1).containsExactly(answer);
    }

    @Test
    @DisplayName("question id에 일치하는 삭제되지 않은 Answer을 조회(해당 결과 없음 일차하는 questionId가 없음)")
    void find_question_id_and_delete_false_return_nothing() {
        Question question = questions.saveAndFlush(new Question("title", "contents"));
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");
        answers.saveAndFlush(answer);
        entityManager.clear();
        long diffQuestionId = 3L;
        List<Answer> expect = answers.findByQuestionIdAndDeletedFalse(diffQuestionId);
        assertThat(expect).isEmpty();
    }

    @Test
    @DisplayName("answer id에 일치하는 삭제되지 않은 Answer을 조회")
    void find_answer_id_and_delete_false() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answers.saveAndFlush(answer);
        entityManager.clear();
        Answer expect = answers.findByIdAndDeletedFalse(answer.getId()).orElseThrow(NotFoundException::new);
        assertThat(answer).isEqualTo(expect);
    }

    @Test
    @DisplayName("answer id에 일치하는 삭제되지 않은 Answer을 조회(삭제된 Answer 이라 조회결과 없음)")
    void find_answer_id_and_delete_false_return_nothing() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answer.setDeleted(true);
        answers.saveAndFlush(answer);
        entityManager.clear();
        assertThat(answers.findByIdAndDeletedFalse(answer.getId())).isEmpty();
    }

}
