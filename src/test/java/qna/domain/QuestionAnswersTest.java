package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

class QuestionAnswersTest {

    private DateTimeStrategy dateTimeStrategy;

    @BeforeEach
    void setUp() {
        dateTimeStrategy = () -> LocalDateTime.of(2021, 6, 1, 0, 0, 0);
    }

    @DisplayName("답변이 등록되면 답변 목록에 추가된다.")
    @Test
    void addTest() {
        QuestionAnswers questionAnswers = new QuestionAnswers();
        List<Answer> answerList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            answerList.add(new Answer((long) i, JAVAJIGI, Q1, "contents"));
            questionAnswers.add(answerList.get(i));
        }

        assertThat(questionAnswers.getAnswers()).hasSize(3)
                                                .containsAll(answerList);
    }

    @DisplayName("답변이 삭제되면 답변 목록에서도 삭제된다.")
    @Test
    void removeTest() {
        QuestionAnswers questionAnswers = new QuestionAnswers();
        List<Answer> answerList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            answerList.add(new Answer((long) i, JAVAJIGI, Q1, "contents"));
            questionAnswers.add(answerList.get(i));
        }
        questionAnswers.delete(answerList.get(2), dateTimeStrategy.now());

        assertThat(questionAnswers.getAnswers()).hasSize(2)
                                                .contains(answerList.get(0), answerList.get(1))
                                                .doesNotContain(answerList.get(2));
    }

    @DisplayName("답변 중 질문자가 아닌 유저가 답변을 남기면 true")
    @Test
    void hasOtherUserAnswerTest() {
        QuestionAnswers questionAnswers = new QuestionAnswers();

        for (int i = 0; i < 2; i++) {
            questionAnswers.add(new Answer((long) i, JAVAJIGI, Q1, "contents"));
        }
        questionAnswers.add(new Answer(3L, SANJIGI, Q1, "contents"));

        assertThat(questionAnswers.hasOtherUserAnswer(JAVAJIGI)).isTrue();
    }

    @DisplayName("모든 답변을 삭제하면 목록이 초기화된다.")
    @Test
    void deleteAnswersTest() {
        QuestionAnswers questionAnswers = new QuestionAnswers();
        List<Answer> answerList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            answerList.add(new Answer((long) i, JAVAJIGI, Q1, "contents"));
            questionAnswers.add(answerList.get(i));
        }

        questionAnswers.deleteAnswers(LocalDateTime.now());
        assertThat(questionAnswers.getAnswers()).isEmpty();
        assertThat(answerList).allMatch(Answer::isDeleted);
    }
}
