package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.UnAuthorizedException;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class QuestionTest {

    @Autowired
    private QuestionRepository questionRepository;

    private static Stream<Arguments> providerQuestions() {
        User US = new User("최웅석", "A", "최웅석", "최웅석_email");

        Question QUESTION1 = new Question(US, "[1] JPA 질문있습니다.", "질문 내용");
        Question QUESTION2 = new Question(US, "[1] 엔티티 질문있습니다.", "질문 내용");

        return Stream.of(
                Arguments.of(QUESTION1),
                Arguments.of(QUESTION2)
        );
    }

    private static Stream<Arguments> providerQuestionsMapping() {
        User US = new User("최웅석", "A", "최웅석", "최웅석_email");

        Question QUESTION1 = new Question(US, "[1] JPA 질문있습니다.", "질문 내용");

        return Stream.of(
                Arguments.of(QUESTION1, US)
        );
    }

    private static Stream<Arguments> providerQuestionOtherWriter() {
        User US = new User("최웅석", "A", "최웅석", "최웅석_email");
        User JH = new User("지호님", "A", "지호님", "지호님_email");

        Question QUESTION1 = new Question(US, "[1] JPA 질문있습니다.", "질문 내용");

        Answer ANSWER1 = new Answer(JH, QUESTION1, "[1] JPA 답변 내용입니다.");

        return Stream.of(Arguments.of(US, QUESTION1, ANSWER1));
    }

    private static Stream<Arguments> providerQuestionSameWriter() {
        User US = new User("최웅석", "A", "최웅석", "최웅석_email");

        Question QUESTION1 = new Question(US, "[1] JPA 질문있습니다.", "질문 내용");

        Answer ANSWER1 = new Answer(US, QUESTION1, "[1] JPA 답변 내용입니다.");

        return Stream.of(Arguments.of(US, QUESTION1, ANSWER1));
    }

    @ParameterizedTest
    @MethodSource("providerQuestions")
    @DisplayName("저장을 시도하는 객체와 저장후 반환되는 객체가 동일한지 체크한다.")
    public void 질문_생성(Question excepted) {
        Question actual = questionRepository.save(excepted);

        assertThat(actual.equals(excepted)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("providerQuestionsMapping")
    @DisplayName("Question Entity 연관관계 정상 확인")
    public void 연관관계_확인(Question QUESTION1, User US) {
        Question save = questionRepository.save(QUESTION1);
        Optional<Question> findOneQuestion = questionRepository.findById(save.getId());

        Question actual = findOneQuestion.orElse(null);

        assertThat(actual.getWriter()).isEqualTo(US);
    }

    @Test
    @DisplayName("질문 객체 생성시 작성자 검증")
    public void 필수값_검증() {
        assertThatThrownBy(() -> {
            new Question(null, "제목", "내용");
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @ParameterizedTest
    @MethodSource("providerQuestionOtherWriter")
    @DisplayName("다른 작성자 삭제 방지 검증")
    public void 다른_작성자_삭제_방지(User loginUser, Question exceptedQuestion) {
        assertThatThrownBy(() -> {
            exceptedQuestion.delete(loginUser);
            exceptedQuestion.cascadeDeleteAnswers(loginUser);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @ParameterizedTest
    @MethodSource("providerQuestionSameWriter")
    @DisplayName("다른 작성자 삭제 검증")
    public void 같은_작성자_삭제(User loginUser, Question exceptedQuestion) {
        exceptedQuestion.delete(loginUser);
        exceptedQuestion.cascadeDeleteAnswers(loginUser);
    }

}
