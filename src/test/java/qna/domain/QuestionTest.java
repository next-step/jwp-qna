package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        DeleteHistory actual = question3.delete();

        //then
        assertThat(actual.getDeletedBy()).isSameAs(UserTest.SANJIGI);
    }

    @Test
    void getAnswers() {
        //given
        question3.addAnswer(answer1);
        question3.addAnswer(answer2);

        //when
        List<Answer> actual = question3.getAnswers();

        //then
        assertThat(actual).containsExactly(answer1, answer2);
    }

    @DisplayName("질문에서 얻어온 답변들 컬렉션에 추가할 수 없다.")
    @Test
    void getAnswersException() {
        //given
        question3.addAnswer(answer1);
        List<Answer> actual = question3.getAnswers();

        //when
        assertThatThrownBy(() -> actual.add(answer2))
                .isInstanceOf(UnsupportedOperationException.class); //then
    }
}
