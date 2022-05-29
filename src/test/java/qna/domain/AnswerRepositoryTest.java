package qna.domain;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    User testUser;

    Question testQuestion;

    @BeforeEach
    void before() {
        testUser = new User("test", "pw", "테스트유저", "a@naver.com");
        userRepository.save(testUser);
        testQuestion = new Question("테스트질문", "java가 뭐에요?");
        questionRepository.save(testQuestion);
    }

    @DisplayName("Answer 저장 테스트")
    @Test
    void saveWithBaseTimeEntityTest() {
        Answer expected = new Answer(testUser, testQuestion, "컴퓨터 언어 입니다.");
        Answer result = answerRepository.save(expected);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getCreatedAt()).isNotNull(),
                () -> assertThat(result.getUpdatedAt()).isNotNull(),
                () -> assertThat(result).isEqualTo(expected)
        );
    }

    @DisplayName("Answer 조회 테스트 / Id로 조회")
    @Test
    void findTest01() {
        Answer expected = answerRepository.save(new Answer(testUser, testQuestion, "컴퓨터 언어 입니다."));
        Optional<Answer> resultOptional = answerRepository.findByIdAndDeletedFalse(expected.getId());
        assertThat(resultOptional).isNotEmpty();

        Answer result = resultOptional.get();
        assertThat(result.getId()).isNotNull();
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Answer 조회 테스트 / QuestionId로 조회")
    @Test
    void findTest02() {
        Answer answer1 = new Answer(testUser, testQuestion, "컴퓨터 언어 입니다.");
        Answer expected = answerRepository.save(answer1);
        List<Answer> results = answerRepository.findByQuestionIdAndDeletedFalse(expected.getQuestion().getId());
        assertThat(results)
                .hasSize(1)
                .contains(expected);

        Answer answer2 = new Answer(testUser, testQuestion, "객체지향 언어 입니다.");
        Answer expected2 = answerRepository.save(answer2);
        results = answerRepository.findByQuestionIdAndDeletedFalse(expected.getQuestion().getId());
        assertThat(results)
                .hasSize(2)
                .contains(expected, expected2);
    }
}