package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Answers 테스트")
class AnswersTest {

    private Answer answer1;
    private Answer answer2;
    private Answers answers;

    @BeforeEach
    void setUp() {
        answer1 = new Answer(UserTest.JAVAJIGI, QuestionTest.TWO_ANSWERED_QUESTION, "Answers Contents1");
        answer2 = new Answer(UserTest.JAVAJIGI, QuestionTest.TWO_ANSWERED_QUESTION, "Answers Contents2");

        answers = new Answers();
        answers.add(answer1);
        answers.add(answer2);
    }

    @Test
    @DisplayName("모든 답변들을 삭제한다.")
    void deleteAnswers() {
        // when
        answers.deleteAnswers(UserTest.JAVAJIGI);

        // then
        assertAll(
                () -> assertThat(answer1.isDeleted()).isTrue(),
                () -> assertThat(answer2.isDeleted()).isTrue()
        );
    }

    @Test
    @DisplayName("모든 답변들에 대한 삭제 이력을 리턴한다.")
    void getDeleteHistories() {
        // when
        List<DeleteHistory> deleteHistories = answers.getDeleteHistories();

        // then
        assertThat(deleteHistories).containsAll(Arrays.asList(
                new DeleteHistory(ContentType.ANSWER, answer2.getId(), answer2.getWriter()),
                new DeleteHistory(ContentType.ANSWER, answer1.getId(), answer1.getWriter())
        ));
    }
}
