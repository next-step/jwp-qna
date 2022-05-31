package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private User savedWriter;
    private Question savedQuestion;

    @BeforeEach
    void setUp() {
        savedWriter = userRepository.save(
            new User("userjigi", "password", "USERJIGI", "userjigi@slipp.net"));
        savedQuestion = questionRepository.save(
            new Question("question title1", "question content1").writeBy(savedWriter));
    }

    @AfterEach
    void afterEach() {
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("생성된 Question은 실제 저장한 Question과 동일해야 한다.")
    void saveTest() {
        assertAll(
            () -> assertThat(savedQuestion.getTitle()).isEqualTo("question title1"),
            () -> assertThat(savedQuestion.getContents()).isEqualTo("question content1")
        );
    }

    @Test
    @DisplayName("저장된 question List 조회시 요소가 일치해야 한다.")
    void findByDeletedFalseTest() {
        // given
        Question savedQuestion2 = questionRepository.save(
            new Question("question title2", "question content2").writeBy(savedWriter));
        Question savedQuestion3 = questionRepository.save(
            new Question("question title3", "question content3").writeBy(savedWriter));

        // when
        List<Question> questions = questionRepository.findByDeletedFalse();

        // then
        assertThat(questions).containsExactly(savedQuestion, savedQuestion2, savedQuestion3);
    }

}
