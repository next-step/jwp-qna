package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question question3;
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        question3 = new Question("title3", "contents3").writeBy(UserTest.SANJIGI);
        answer1 = new Answer(UserTest.SANJIGI, question3, "answer contents1");
        answer2 = new Answer(UserTest.SANJIGI, question3, "answer contents2");
    }

    @Test
    void deleteQuestion() {
        //when
        List<DeleteHistory> actual = question3.delete(UserTest.SANJIGI);

        //then
        assertThat(actual).containsExactly(DeleteHistory.ofQuestion(question3.getId(), UserTest.SANJIGI, LocalDateTime.now()));
    }

    @DisplayName("질문을 삭제할 때 본인이 만든 질문이 아닌 경우 예외를 발생시킨다.")
    @Test
    void deleteQuestionException() {
        assertThatThrownBy(() -> question3.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContainingAll(Question.DELETE_EXCEPTION_MESSAGE);
    }

    @Test
    void getAnswers() {
        //given
        question3.addAnswer(answer1);
        question3.addAnswer(answer2);

        //when
        Answers actual = question3.getAnswers();

        //then
        assertThat(actual.getAnswers()).containsExactly(answer1, answer2);
    }
}
