package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AnswersTest {
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        question = new Question(1L, "title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
        answer = new Answer(1L, UserRepositoryTest.JAVAJIGI, question, "Answers Contents1");
        question.addAnswers(answer);
    }

    @Test
    void validLoginUser() {
        Answers answers = question.getAnswers();
        assertThatThrownBy(() -> {
            answers.validOwner(UserRepositoryTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    void delete() {
        LocalDateTime deletedTime = LocalDateTime.now();
        List<DeleteHistory> resultHistories = Arrays.asList(
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now())
        );
        Answers answers = question.getAnswers();
        DeleteHistories deleteHistories = answers.delete(deletedTime);
        assertThat(deleteHistories.getDeleteHistories()).containsAll(resultHistories);
    }
}