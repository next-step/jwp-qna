package qna.domain.deleteHistory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.answer.Answer;
import qna.domain.content.Contents;
import qna.domain.content.Title;
import qna.domain.question.Question;
import qna.domain.user.User;
import qna.domain.user.UserRepository;
import qna.domain.user.UserTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.user.UserTest.JAVAJIGI;
import static qna.domain.user.UserTest.SANJIGI;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private Question question;

    @BeforeEach
    void init() {
        deleteHistoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("deleteHistory 저장")
    void save_delete_history() {
        user = userRepository.save(JAVAJIGI);
        DeleteHistory target = new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now());
        deleteHistoryRepository.save(target);

        List<DeleteHistory> savedHistories = deleteHistoryRepository.findAll();

        assertThat(savedHistories.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Question deleteHistories 저장")
    void save_question_delete_histories() {
        userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));
        question = new Question(1L, new Title("title1"), Contents.of("contents1")).writeBy(UserTest.JAVAJIGI);
        new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        new Answer(2L, UserTest.SANJIGI, question, "Answers Contents2");

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addDeleteQuestionHistory(question);
        deleteHistoryRepository.saveAll(deleteHistories);

        List<DeleteHistory> savedHistories = deleteHistoryRepository.findAll();

        assertThat(savedHistories.size()).isEqualTo(3);
    }
}
