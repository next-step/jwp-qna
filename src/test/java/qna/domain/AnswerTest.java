package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private static Stream<Arguments> providerAnswers() {
        return Stream.of(
                Arguments.of(A1),
                Arguments.of(A2)
        );
    }

    @ParameterizedTest
    @MethodSource("providerAnswers")
    @DisplayName("저장을 시도하는 객체와 저장후 반환되는 객체가 동일한지 체크한다.")
    public void 답변_생성(Answer excepted) {
        Answer actual = answerRepository.save(excepted);

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(excepted.getId()),
                () -> assertThat(actual.getWriter()).isEqualTo(excepted.getWriter()),
                () -> assertThat(actual.getContents()).isEqualTo(excepted.getContents()),
                () -> assertThat(actual.getQuestion()).isEqualTo(excepted.getQuestion())
        );
    }

    public static final User US = new User("최웅석", "A", "최웅석", "최웅석_email");
    public static final User JH = new User("지호님", "A", "지호님", "지호님_email");
    public static final Question QUESTION = new Question(US, "JPA 질문있습니다.", "질문 내용", false);
    public static final Answer ANSWER = new Answer(JH, QUESTION, "JPA 답변 내용입니다.");

    @BeforeEach
    void setup() {
        userRepository.save(US);
        userRepository.save(JH);
        questionRepository.save(QUESTION);
        answerRepository.save(ANSWER);
    }

    @Test
    @DisplayName("Answer Entity 연관관계 정상 확인")
    public void 연관관계_확인() {
        Optional<Answer> findOneAnswer = answerRepository.findById(ANSWER.getId());

        Answer excepted = findOneAnswer.orElse(null);

        assertAll(
                () -> assertThat(excepted.getWriter()).isEqualTo(JH),
                () -> assertThat(excepted.getQuestion()).isEqualTo(QUESTION),
                () -> assertThat(excepted.getQuestion().getWriter()).isEqualTo(US)
        );
    }

}
