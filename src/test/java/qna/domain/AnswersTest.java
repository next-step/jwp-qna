package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

class AnswersTest {

    @DisplayName("답변이 등록되면 답변 목록에 추가된다.")
    @Test
    void addTest() {
        Answers answers = new Answers();
        List<Answer> answerList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            answerList.add(new Answer((long) i, JAVAJIGI, Q1, "contents"));
            answers.add(answerList.get(i));
        }

        assertThat(answers.getAnswers()).hasSize(3)
                                        .containsAll(answerList);
    }

    @DisplayName("답변이 삭제되면 답변 목록에서도 삭제된다.")
    @Test
    void removeTest() {
        Answers answers = new Answers();
        List<Answer> answerList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            answerList.add(new Answer((long) i, JAVAJIGI, Q1, "contents"));
            answers.add(answerList.get(i));
        }
        answers.remove(answerList.get(2));

        assertThat(answers.getAnswers()).hasSize(2)
                                        .contains(answerList.get(0), answerList.get(1))
                                        .doesNotContain(answerList.get(2));
    }

    @DisplayName("답변 중 질문자가 아닌 유저가 답변을 남기면 true")
    @Test
    void hasOtherUserAnswerTest() {
        Answers answers = new Answers();

        for (int i = 0; i < 2; i++) {
            answers.add(new Answer((long) i, JAVAJIGI, Q1, "contents"));
        }
        answers.add(new Answer(3L, SANJIGI, Q1, "contents"));

        assertThat(answers.hasOtherUserAnswer(JAVAJIGI)).isTrue();
    }

    @DisplayName("모든 답변을 삭제하면 목록이 초기화된다.")
    @Test
    void deleteAnswersTest() {
        Answers answers = new Answers();
        List<Answer> answerList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            answerList.add(new Answer((long) i, JAVAJIGI, Q1, "contents"));
            answers.add(answerList.get(i));
        }

        answers.deleteAnswers(LocalDateTime.now());
        assertThat(answers.getAnswers()).hasSize(0);
        assertThat(answerList).allMatch(Answer::isDeleted);
    }
}
