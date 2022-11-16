package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Question question;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("iamsojung", "password", "sojung", "email@gmail.com"));
        question = questionRepository.save(new Question("title", "contents").writeBy(user));

    }


    @Test
    @DisplayName("save 검증 테스트")
    void saveTest() {
        assertAll(
            () -> assertThat(question.getId()).isNotNull(),
            () -> assertThat(question.getContents()).isEqualTo(question.getContents()),
            () -> assertThat(question.isOwner(user)).isTrue(),
            () -> assertThat(question.getTitle()).isEqualTo(question.getTitle())

        );
    }

    @Test
    @DisplayName("저장한 question 와 id로 조회한 question 이 같은지 동등성 테스트")
    void read() {
        Question findQuestion = questionRepository.findById(question.getId()).get();
        assertThat(question).isEqualTo(findQuestion);
    }


    @Test
    @DisplayName("findByDeletedFalse 테스트")
    void findByDeletedFalseTest() {
        List<Question> result = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).contains(question),
            () -> assertThat(result.get(0)).isEqualTo(question)
        );
    }


    @Test
    @DisplayName("findByIdAndDeletedFalse 테스트")
    void findByIdAndDeletedFalse() {
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertThat(actual).isPresent();
    }
}
