package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.UnAuthorizedException;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class AnswerTest {

    @Autowired
    private AnswerRepository answerRepository;

    private static Stream<Arguments> providerAnswer() {
        User US = new User("최웅석", "A", "최웅석", "최웅석_email");
        User JH = new User("지호님", "A", "지호님", "지호님_email");
        Question QUESTION = new Question(US, "JPA 질문있습니다.", "질문 내용");
        Answer ANSWER = new Answer(JH, QUESTION, "JPA 답변 내용입니다.");

        return Stream.of(
                Arguments.of(ANSWER)
        );
    }

    private static Stream<Arguments> providerAnswers() {
        User US = new User("최웅석", "A", "최웅석", "최웅석_email");
        User JH = new User("지호님", "A", "지호님", "지호님_email");

        Question QUESTION1 = new Question(US, "[1] JPA 질문있습니다.", "질문 내용");
        Question QUESTION2 = new Question(US, "[2] Entity 질문있습니다.", "질문 내용");

        Answer ANSWER1 = new Answer(JH, QUESTION1, "[1] JPA 답변 내용입니다.");
        Answer ANSWER2 = new Answer(JH, QUESTION2, "[2] Entity 답변 내용입니다.");
        return Stream.of(
                Arguments.of(ANSWER1),
                Arguments.of(ANSWER2)
        );
    }

    @ParameterizedTest
    @MethodSource("providerAnswer")
    @DisplayName("Answer Entity 연관관계 정상 확인")
    public void 연관관계_확인(Answer answer) {
        Answer save = answerRepository.save(answer);
        Optional<Answer> findOneAnswer = answerRepository.findById(save.getId());

        Answer actual = findOneAnswer.orElse(null);

        assertThat(actual.equals(answer)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("providerAnswers")
    @DisplayName("저장을 시도하는 객체와 저장후 반환되는 객체가 동일한지 체크한다.")
    public void 답변_생성(Answer excepted) {
        Answer actual = answerRepository.save(excepted);

        assertThat(actual.equals(excepted)).isTrue();
    }

    @Test
    @DisplayName("답변 객체 생성시 작성자 검증")
    public void 필수값_검증() {
        assertThatThrownBy(() -> {
            User US = new User("최웅석", "A", "최웅석", "최웅석_email");
            Question QUESTION1 = new Question(US, "[1] JPA 질문있습니다.", "질문 내용");

            new Answer(null, QUESTION1, "내용");
        }).isInstanceOf(UnAuthorizedException.class);
    }

}
