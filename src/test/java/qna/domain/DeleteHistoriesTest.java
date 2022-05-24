package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoriesTest {

    private Question question;
    private Answer answer;
    private DeleteHistories deleteHistories;

    @BeforeEach
    public void setUp() {
        question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);

        deleteHistories = new DeleteHistories();
        deleteHistories.add(DeleteHistory.ofQuestion(question.getId(), question.writer(), LocalDateTime.now()));
    }

    @DisplayName("DeleteHistories에 DeleteHistory를 추가한다.")
    @Test
    void add() {
        //given & when
        deleteHistories.add(DeleteHistory.ofAnswer(answer.getId(), answer.writer(), LocalDateTime.now()));

        //then
        assertThat(deleteHistories.elements()).hasSize(2);
    }

    @DisplayName("DeleteHistories에 DeleteHistories를 추가한다.")
    @Test
    void addAll() {
        //given
        Answer answer2 = new Answer(2L, UserTest.JAVAJIGI, question, "Answers Contents2");
        question.addAnswer(answer2);
        DeleteHistories addDeleteHistories = new DeleteHistories(Arrays.asList(
                DeleteHistory.ofAnswer(answer.getId(), answer.writer(), LocalDateTime.now()),
                DeleteHistory.ofAnswer(answer2.getId(), answer2.writer(), LocalDateTime.now())
        ));

        //when
        deleteHistories.addAll(addDeleteHistories);

        //then
        assertThat(deleteHistories.elements()).hasSize(3);
    }

}
