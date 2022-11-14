package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DeleteHistoriesTest {
    public static User writer;
    public static Question question;

    @BeforeAll
    static void beforeAll() {
        writer = TestFixture.createUser();
        question = TestFixture.createQuestion(writer);

    }

    @Test
    void history_Add() throws Exception {
        //given
        Answer answer1 = new Answer(1L, writer, question, "test1");
        Answer answer2 = new Answer(2L, writer, question, "test2");
        Answers answers = new Answers(Arrays.asList(answer1, answer2));
        DeleteHistories deleteHistories = answers.delete(writer);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter());

        //when
        deleteHistories.addQueue(deleteHistory);

        //then
        assertThat(deleteHistories.contains(deleteHistory)).isTrue();
    }
}
