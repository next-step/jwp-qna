package qna.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("삭제이력 엔티티")
public class DeleteHistoryTest extends JpaSliceTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;


    private User questionUser;
    private User answerUser;

    @BeforeEach
    void setUp() {
        questionUser = userRepository.save(new User(1L, "dominiqn", "password", "dongmin", "dmut7691@gmail.com"));
        answerUser = userRepository.save(new User(2L, "javajigi", "password", "pobi", "javajigi@slipp.net"));
    }

    @DisplayName("삭제이력을 저장할 수 있다.")
    @Test
    void save() {
        final DeleteHistory history1 = new DeleteHistory(ContentType.QUESTION, 1L, questionUser);
        final DeleteHistory history2 = new DeleteHistory(ContentType.ANSWER, 3L, answerUser);
        final List<DeleteHistory> histories = Arrays.asList(history1, history2);

        assertAll(
                () -> assertThatNoException().isThrownBy(() -> deleteHistoryRepository.saveAll(histories)),
                () -> assertThat(deleteHistoryRepository.count()).isEqualTo(2)
        );
    }

    @DisplayName("질문 삭제이력을 저장할 수 있다.")
    @Test
    void saveQuestionHistory() {
        final Question question = new Question(2L, "질문입니다", "별 내용 없습니다.").writeBy(questionUser);
        final DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 2L, questionUser);

        final DeleteHistory actual = DeleteHistory.of(question);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("질문은 null이 아니어야 한다.")
    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @NullSource
    void nullQuestion(Question question) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DeleteHistory.of(question));
    }

    @DisplayName("답변 삭제이력을 저장할 수 있다.")
    @Test
    void saveAnswerHistory() {
        final Question question = new Question(10L, "질문입니다", "별 내용 없습니다.").writeBy(questionUser);
        final Answer answer = new Answer(20L, answerUser, question, "자문자답");
        final DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 20L, answerUser);

        final DeleteHistory actual = DeleteHistory.of(answer);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("답변은 null이 아니어야 한다.")
    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @NullSource
    void nullAnswer(Answer answer) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DeleteHistory.of(answer));
    }
}
