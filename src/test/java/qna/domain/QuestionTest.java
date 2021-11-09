package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.*;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question(null, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(null, "title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;

    private static Stream<Arguments> providerQuestions() {
        return Stream.of(
                Arguments.of(Q1),
                Arguments.of(Q2)
        );
    }

    @ParameterizedTest
    @MethodSource("providerQuestions")
    @DisplayName("저장을 시도하는 객체와 저장후 반환되는 객체가 동일한지 체크한다.")
    public void 질문_생성(Question excepted) {
        Question actual = questionRepository.save(excepted);

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(excepted.getId()),
                () -> assertThat(actual.getWriter()).isEqualTo(excepted.getWriter()),
                () -> assertThat(actual.getContents()).isEqualTo(excepted.getContents()),
                () -> assertThat(actual.getTitle()).isEqualTo(excepted.getTitle())
        );
    }

    @BeforeEach
    void setup() {
        userRepository.save(US);
        userRepository.save(JH);
        questionRepository.save(QUESTION);
        answerRepository.save(ANSWER);
    }

    @Test
    @DisplayName("Question Entity 연관관계 정상 확인")
    public void 연관관계_확인() {
        Optional<Question> findOneQuestion = questionRepository.findById(QUESTION.getId());

        Question actual = findOneQuestion.orElse(null);

        assertAll(
                () -> assertThat(actual.getWriter()).isEqualTo(US),
                () -> assertThat(actual.getContents()).isEqualTo(QUESTION.getContents()),
                () -> assertThat(actual.getTitle()).isEqualTo(QUESTION.getTitle())
        );
    }
}
