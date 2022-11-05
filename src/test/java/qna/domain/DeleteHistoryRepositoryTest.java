package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DeleteHistoryTest.D1;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;
import static qna.domain.UserTest.JAVAJIGI;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void init() {
        deleteHistoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("답변 삭제 이력이 정상적으로 저장되어야 한다")
    void answer_history_save_success() {
        // given
        User user = userRepository.save(JAVAJIGI);
        DeleteHistory history = getAnswerHistory(user);

        // when
        DeleteHistory actual = deleteHistoryRepository.save(history);

        // then
        Optional<DeleteHistory> expected = deleteHistoryRepository.findById(actual.getId());
        assertThat(expected).contains(history);
    }

    @Test
    @DisplayName("질문 삭제 이력이 정상적으로 저장되어야 한다")
    void question_history_save_success() {
        // given
        User user = userRepository.save(JAVAJIGI);
        DeleteHistory history = getQuestionHistory(user);

        // when
        DeleteHistory actual = deleteHistoryRepository.save(history);

        // then
        Optional<DeleteHistory> expected = deleteHistoryRepository.findById(actual.getId());
        assertThat(expected).contains(history);
    }

    private DeleteHistory getAnswerHistory(User user) {
        Question question = questionRepository.save(Q1.writeBy(user));
        Answer a1 = new Answer(user, question, "answer1");
        a1.toQuestion(question);
        return new DeleteHistory(ContentType.ANSWER, a1.getId(), user, LocalDateTime.now());
    }

    private DeleteHistory getQuestionHistory(User user) {
        Question question = questionRepository.save(Q2.writeBy(user));
        return new DeleteHistory(ContentType.QUESTION, question.getId(), user, LocalDateTime.now());
    }


}
