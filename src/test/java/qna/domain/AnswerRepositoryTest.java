package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private User savedWriter;
    private Question savedQuestion;
    private Answer savedAnswer1;
    private Answer savedAnswer2;

    @BeforeEach
    void setUp() {
        savedWriter = userRepository.save(UserTest.JAVAJIGI);
        savedQuestion = questionRepository.save(
            new Question("question title3", "question contents3").writeBy(savedWriter));
        savedAnswer1 = answerRepository.save(
            new Answer(savedWriter, savedQuestion, "answer contents3"));
        savedAnswer2 = answerRepository.save(
            new Answer(savedWriter, savedQuestion, "answer contents4"));

        System.out.println(savedQuestion.getAnswers().size());
    }

    @AfterEach
    void afterEach() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("생성된 Answer와 그 id로 조회한 Answer는 동일성이 보장되어야 한다.")
    void findByIdTest() {
        assertAll(
            () -> assertThat(savedAnswer1.getWriter()).isEqualTo(savedWriter),
            () -> assertThat(savedAnswer1.getQuestion()).isEqualTo(savedQuestion),
            () -> assertThat(savedAnswer1.getContents()).isEqualTo("answer contents3")
        );
    }

    @Test
    @DisplayName("not null 컬럼들은 반드시 값이 존재해야 한다.")
    void notNullColumnsTest() {
        assertAll(
            () -> assertThat(savedAnswer1.getCreatedAt()).isBefore(LocalDateTime.now()),
            () -> assertThat(savedAnswer1.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("Update된 Contents가 일치해야 한다.")
    void updateAnswerTest() {
        // given
        savedAnswer1.setContents("answer contents3 - Updated");

        // then
        Assertions.assertAll(
            () -> assertThat(savedAnswer1).isNotNull(),
            () -> assertThat(savedAnswer1.getContents()).isSameAs("answer contents3 - Updated")
        );
    }

}
