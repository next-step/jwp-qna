package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class AnswersTest {

    private Question question1;
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        question1 = new Question("title3", "contents3").writeBy(UserTest.JAVAJIGI);
        answer1 = new Answer(UserTest.JAVAJIGI, question1, "answer contents1");
        answer2 = new Answer(UserTest.JAVAJIGI, question1, "answer contents2");
    }

    @DisplayName("답변 일급컬렉션에서 꺼낸 컬렉션에 직접 추가할 수 없다.")
    @Test
    void getAnswersException() {
        //given
        question1.addAnswer(answer1);
        Answers answers = question1.getAnswers();
        List<Answer> actual = answers.getAnswers();

        //when
        assertThatThrownBy(() -> actual.add(answer2))
                .isInstanceOf(UnsupportedOperationException.class); //then
    }

    @Test
    void deleteAll() {
        //given
        Answers answers = question1.getAnswers();
        answers.add(answer1);
        answers.add(answer2);

        //when
        List<DeleteHistory> actual = answers.deleteAll(UserTest.JAVAJIGI);

        //then
        assertAll(() -> {
            assertThat(actual.get(0).getId()).isEqualTo(answer1.getId());
            assertThat(actual.get(0).getDeletedBy()).isEqualTo(UserTest.JAVAJIGI);
            assertThat(actual.get(1).getId()).isEqualTo(answer2.getId());
            assertThat(actual.get(1).getDeletedBy()).isEqualTo(UserTest.JAVAJIGI);
        });
    }
}