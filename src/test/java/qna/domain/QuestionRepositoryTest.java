package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("질문 저장소 테스트")
public class QuestionRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private User user1;
    private User user2;
    private Question question;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User(1L, "chaeeun", "password", "name", "chaeeun@gmail.com"));
        user2 = userRepository.save(new User(2L, "chaeeun2", "password2", "name2", "chaeeun2@gmail.com"));
        question = new Question(1L, user1, "title", "contents");
    }

    @Test
    @DisplayName("질문 저장")
    void 저장() {
        Question saved = questionRepository.save(question);

        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(question.getContents()).isEqualTo(saved.getContents()),
                () -> assertThat(question.getWriteBy()).isEqualTo(saved.getWriteBy()),
                () -> assertThat(question.getTitle()).isEqualTo(saved.getTitle())
        );
    }

    @Test
    @DisplayName("질문 삭제")
    void 삭제() {
        Question target = questionRepository.save(question);
        questionRepository.delete(question);

        assertThat(questionRepository.findById(question.getId())).isEmpty();
    }

    @Test
    @DisplayName("삭제되지 않은 질문 목록 조회")
    void 삭제되지_않은_질문목록_조회() {
        Question savedQuestion1 = questionRepository.save(new Question(1L, user1, "title", "contents"));
        Question savedQuestion2 = questionRepository.save(new Question(2L, user2, "title", "contents"));
        List<Question> questionList = questionRepository.findByDeletedFalse();

        assertThat(questionList).hasSize(2);
        assertThat(questionList).containsAll(Arrays.asList(savedQuestion1, savedQuestion2));
    }

    @Test
    @DisplayName("질문 ID로 삭제되지 않은 질문 조회")
    void 질문_ID로_삭제되지_않은_질문_조회() {
        Question saved = questionRepository.save(question);
        Question expected = questionRepository.findByIdAndDeletedFalse(saved.getId()).get();

        assertAll(
                () -> assertThat(saved.getId()).isEqualTo(expected.getId()),
                () -> assertThat(saved.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(saved.getWriteBy()).isEqualTo(expected.getWriteBy()),
                () -> assertThat(saved.getTitle()).isEqualTo(expected.getTitle())
        );

    }
}
