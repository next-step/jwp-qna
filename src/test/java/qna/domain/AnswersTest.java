package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswersTest {

    private User javaJigi;
    private User sanJigi;
    private Answer javaJigiAnswer;
    private Answer sanJigiAnswer;
    private List<Answer> answerList;

    @BeforeEach
    void setUp() {
        javaJigi = UserTest.JAVAJIGI;
        sanJigi = UserTest.JAVAJIGI;
        javaJigiAnswer = AnswerTest.A1;
        sanJigiAnswer = AnswerTest.A2;
        answerList = new ArrayList<>();
    }

    @DisplayName("Answers 일급컬렉션 값 확인")
    @Test
    void containsAnswer() {
        answerList.add(javaJigiAnswer);
        Answers answers = new Answers(answerList);

        boolean isContains = answers.contains(javaJigiAnswer);

        assertThat(isContains).isTrue();
    }

    @DisplayName("Answer 값 추가")
    @Test
    void addAnswers() {
        answerList.add(javaJigiAnswer);
        Answers answers = new Answers(answerList);
        answers.add(sanJigiAnswer);

        boolean contains = answers.contains(sanJigiAnswer);

        assertThat(contains).isTrue();
    }

    @DisplayName("Answer list 삭제 상태 변경")
    @Test
    void deleteAnswers() throws CannotDeleteException {
        answerList.add(javaJigiAnswer);
        Answers answers = new Answers(answerList);
        answers.delete(javaJigi);

        boolean isAllDelete = answers.isAllDelete();

        assertThat(isAllDelete).isTrue();
    }

    @DisplayName("다른 사용자의 질문을 삭제할 경우")
    @Test
    void invalidDeleteAnswers() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> {
                answerList.add(sanJigiAnswer);
                Answers answers = new Answers(answerList);

                answers.delete(javaJigi);
            }).withMessageMatching("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
