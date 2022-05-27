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
    private DeleteHistory answerHistory, questionHistory;

    @BeforeEach
    void init() {
        //given
        User writer = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        questionHistory = new DeleteHistory(ContentType.QUESTION, 1L, writer, LocalDateTime.now());
        answerHistory = new DeleteHistory(ContentType.ANSWER, 1L, writer, LocalDateTime.now());
    }

    @Test
    @DisplayName("질문 삭제 히스토리 저장 및 값 비교 테스트")
    void save_by_question() {
        //when
        DeleteHistory actual = deleteHistoryRepository.save(questionHistory);

        //then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isEqualTo(questionHistory)
        );
    }

    @Test
    @DisplayName("답변 삭제 히스토리 저장 및 값 비교 테스트")
    void save_by_answer() {
        //when
        DeleteHistory actual = deleteHistoryRepository.save(answerHistory);

        //then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isEqualTo(answerHistory)
        );
    }

    @Test
    @DisplayName("질문 타입 저장 후 id로 삭제기록 목록 조회")
    void findById_question_type() {
        //when
        DeleteHistory expected = deleteHistoryRepository.save(questionHistory);
        Optional<DeleteHistory> actual = deleteHistoryRepository.findById(expected.getId());

        //then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get()).isSameAs(expected)
        );
    }

    @Test
    @DisplayName("답변 타입 저장 후 id로 삭제기록 목록 조회")
    void findById_answer_type() {
        //when
        DeleteHistory expected = deleteHistoryRepository.save(answerHistory);

        //when
        Optional<DeleteHistory> actual = deleteHistoryRepository.findById(expected.getId());

        //then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get()).isSameAs(expected)
        );
    }
}
