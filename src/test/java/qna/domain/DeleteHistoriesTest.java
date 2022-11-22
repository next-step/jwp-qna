package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DeleteHistoriesTest {

    private User writer;
    private Question question;

    @BeforeEach
    void setUp() {
        writer = User.create("JAVAJIGI", "1234", "JAVAJIGI", "JAVAJIGI@gmail.com");
        question = Question.create("title", "contents", writer);
    }
    @Test
    void delete_history() {
        //given
        Answer answer1 = Answer.create(writer, question, "삭제 이력 추가 테스트");
        Answer answer2 = Answer.create(writer, question, "삭제 이력 추가 테스트");
        Answers answers = new Answers(answer1);
        answers.addAnswer(answer2);

        //when
        DeleteHistories deleteHistories = question.delete(writer);

        //then
        assertAll(
                () -> assertThat(deleteHistories.unmodifiedDeleteHistories()).containsAll(
                        deleteHistories.unmodifiedDeleteHistories()),
                () -> assertThat(deleteHistories.unmodifiedDeleteHistories()).contains(
                        deleteHistories.unmodifiedDeleteHistories().get(0)),
                () -> assertThat(deleteHistories.unmodifiedDeleteHistories()).hasSize(3)
        );
    }
}