package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteHistoriesTest {
    private User questionUser;
    private Question question;
    private User answerUser;
    private Answer answer1;
    private DeleteHistory deleteHistory1;
    private DeleteHistory deleteHistory2;
    private DeleteHistory deleteHistory3;

    private DeleteHistories deleteHistories;

    @BeforeEach
    void setUp() {
        questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);
        answerUser = new User("user2", "user2Pass", "User2", "user2@gmail.com");
        answer1 = new Answer(questionUser, question, "Answers Contents1");
        deleteHistory1 = new DeleteHistory(ContentType.QUESTION, question.getId(), questionUser, LocalDateTime.now());
        deleteHistory2 = new DeleteHistory(ContentType.ANSWER, question.getId(), answerUser, LocalDateTime.now());
        deleteHistory3 = new DeleteHistory(ContentType.ANSWER, answer1.getId(), answerUser, LocalDateTime.now());

    }

    @Test
    void addHistory() {
        // given
        deleteHistories = new DeleteHistories();
        List<DeleteHistory> deleteHistoryList
                = new ArrayList<>(Arrays.asList(deleteHistory1, deleteHistory2, deleteHistory3));
        // when
        deleteHistories.addHistory(deleteHistory1);
        deleteHistories.addHistory(deleteHistory2);
        deleteHistories.addHistory(deleteHistory3);
        // then
        assertThat(deleteHistories).extracting("deleteHistories").asList().size().isEqualTo(3);
//        assertThat(deleteHistories).extracting("deleteHistories").asList().containsExactly(deleteHistoryList);
    }

    @Test
    void addHistories() {
        // given
        deleteHistories = new DeleteHistories();
        List<DeleteHistory> deleteHistoryList
                = new ArrayList<>(Arrays.asList(deleteHistory1, deleteHistory2, deleteHistory3));
        // when
        deleteHistories.addHistories(deleteHistoryList);
        // then
        assertThat(deleteHistories).extracting("deleteHistories").asList().size().isEqualTo(3);
    }
}
