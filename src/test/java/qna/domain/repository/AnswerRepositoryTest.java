package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answers;

    @Test
    void 엔티티_저장() {
        User javajigi = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(javajigi);
        Answer expected = new Answer(javajigi, question, question.getContents());
        Answer actual = answers.save(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual == expected).isTrue();
    }

    @Test
    void Id로_삭제() {
        User javajigi = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(javajigi);
        Answer actual = answers.save(new Answer(javajigi, question, question.getContents()));
        answers.delete(actual);
        answers.flush();
    }

    @Test
    @DisplayName("질문 게시글에 달린 답변 목록 조회")
    void findByQuestionIdAndDeletedFalse() {
        User javajigi = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(javajigi);
        Answer expected = answers.save(new Answer(javajigi, question, question.getContents()));
        List<Answer> findAnswers = answers.findByQuestionIdAndDeletedFalse(expected.getQuestionId());
        assertThat(findAnswers).hasSize(1);
    }

    @Test
    @DisplayName("삭제되지 않은 답변 조회")
    void findByIdAndDeletedFalse() {
        User javajigi = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(javajigi);
        Answer expected = answers.save(new Answer(javajigi, question, question.getContents()));
        Answer actual = answers.findByIdAndDeletedFalse(expected.getId()).get();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual == expected).isTrue();
    }

    @Test
    @DisplayName("Answer 는 User 와 Question 이 반드시 존재해야한다")
    void 익셉션_테스트() {
        User javajigi = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(javajigi);
        assertThatExceptionOfType(UnAuthorizedException.class)
                .isThrownBy(() -> new Answer(null, question, question.getContents()));
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> new Answer(javajigi, null, question.getContents()));
    }
}
