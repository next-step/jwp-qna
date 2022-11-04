package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    @Test
    @DisplayName("question 저장한다")
    void save_question() {
        Question question = new Question("test", "test").writeBy(UserTest.JAVAJIGI);
        Question expect = questions.save(question);
        assertAll(
                () -> assertThat(question.getId()).isEqualTo(expect.getId()),
                () -> assertThat(question.getWriterId()).isEqualTo(expect.getWriterId()),
                () -> assertThat(question.getContents()).isEqualTo(expect.getContents()),
                () -> assertThat(question.isDeleted()).isEqualTo(expect.isDeleted())
        );
    }

    @Test
    @DisplayName("question 삭제한다")
    void delete_question() {
        Question question = new Question("test", "test").writeBy(UserTest.JAVAJIGI);
        Question expect = questions.save(question);
        questions.delete(expect);
        assertThat(questions.findById(question.getId())).isEmpty();
    }

    @Test
    @DisplayName("question 제목 및 내용을 수정한다.")
    void update_question() {
        Question question = new Question("test", "test").writeBy(UserTest.JAVAJIGI);
        Question expect = questions.save(question);
        expect.updateTitleAndContents("change test", "chagnet contents");

        Question findQuestion = questions.getOne(expect.getId());
        assertAll(
                () -> assertThat(expect.getTitle()).isEqualTo(findQuestion.getTitle()),
                () -> assertThat(expect.getContents()).isEqualTo(findQuestion.getContents())
        );
    }

    @Test
    @DisplayName("삭제 되지 않은 question을 조회한다.")
    void find_all_not_deleted() {
        Question notDeleteQuestion = createQuestion("test", "test", UserTest.JAVAJIGI, false);
        Question deleteQuestion = createQuestion("test", "test", UserTest.JAVAJIGI, true);
        questions.save(notDeleteQuestion);
        questions.save(deleteQuestion);
        List<Question> saveQuestion = questions.findByDeletedFalse();
        assertThat(saveQuestion).hasSize(1);
    }

    @Test
    @DisplayName("question id 로 삭제되지 않은 question을 조회한다")
    void find_by_question_id() {
        Question notDeleteQuestion = createQuestion("test", "test", UserTest.JAVAJIGI, false);
        questions.save(notDeleteQuestion);
        Question expect = questions.findByIdAndDeletedFalse(notDeleteQuestion.getId()).orElseThrow(NotFoundException::new);
        assertAll(
                () -> assertThat(notDeleteQuestion.getId()).isEqualTo(expect.getId()),
                () -> assertThat(notDeleteQuestion.getWriterId()).isEqualTo(expect.getWriterId()),
                () -> assertThat(notDeleteQuestion.getContents()).isEqualTo(expect.getContents()),
                () -> assertThat(notDeleteQuestion.isDeleted()).isEqualTo(expect.isDeleted())
        );

    }

    private static Question createQuestion(String title, String contents, User user, boolean isDeleted) {
        Question question = new Question(title, contents).writeBy(user);
        question.setDeleted(isDeleted);
        return question;
    }
}
