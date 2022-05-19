package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    Question question1;
    Question question2;
    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder("javajigi", "password", "name")
                .email("javajigi@slipp.net")
                .build();
        user2 = User.builder("sanjigi", "password", "name")
                .email("sanjigi@slipp.net")
                .build();
        question1 = Question.builder("title1")
                .contents("contents1")
                .build()
                .writeBy(user1);
        question2 = Question.builder("title2")
                .contents("contents2")
                .build()
                .writeBy(user2);
    }

    @DisplayName("저장 테스트")
    @Test
    void save() {
        Question question = questionRepository.save(question1);
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getWriter()).isEqualTo(question1.getWriter()),
                () -> assertThat(question.getContents()).isEqualTo(question1.getContents()),
                () -> assertThat(question.getTitle()).isEqualTo(question1.getTitle())
        );
    }

    @DisplayName("삭제되지 않은 질문 조회 테스트")
    @Test
    void findByDeletedFalse() {
        questionRepository.save(question1);
        questionRepository.save(question2);
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).hasSize(2);
    }

    @DisplayName("delete 가 잘 되었는지 테스트")
    @Test
    void delete() {
        questionRepository.save(question1);
        Question question = questionRepository.save(question2);
        questionRepository.delete(question);
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).hasSize(1);
    }

    @DisplayName("삭제되지 않은 질문을 ID로 조회하는 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Question question = questionRepository.save(question1);
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertThat(actual.orElse(null)).isEqualTo(question);
    }
}
