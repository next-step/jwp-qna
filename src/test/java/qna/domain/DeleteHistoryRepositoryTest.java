package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    private User user;
    private Question question;
    private Answer answer;

    @BeforeEach
    void init() {
        user = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(QuestionTest.Q1);
        answer = answerRepository.save(AnswerTest.A1);
    }

    @Test
    @DisplayName("질문 삭제 히스토리 저장 및 값 비교 테스트")
    void save_by_question() {
        //given
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, question.getId(), user.getId(), LocalDateTime.now());

        //when
        DeleteHistory actual = deleteHistoryRepository.save(expected);

        //then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("답변 삭제 히스토리 저장 및 값 비교 테스트")
    void save_by_answer() {
        //given
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, answer.getId(), user.getId(), LocalDateTime.now());

        //when
        DeleteHistory actual = deleteHistoryRepository.save(expected);

        //then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("id로 삭제기록 목록 조회")
    void findById() {
        //given
        DeleteHistory expected = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, answer.getId(), user.getId(), LocalDateTime.now()));

        //when
        Optional<DeleteHistory> actual = deleteHistoryRepository.findById(user.getId());

        //then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get()).isSameAs(expected)
        );
    }
}
