package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    public static final Question Q1 = new Question.QuestionBuilder("title1")
            .contents("contents1")
            .build()
            .writeBy(UserRepositoryTest.JAVAJIGI);
    public static final Question Q2 = new Question.QuestionBuilder("title2")
            .contents("contents2")
            .build()
            .writeBy(UserRepositoryTest.SANJIGI);
    public static final Question Q3 = new Question.QuestionBuilder("title3")
            .contents("contents3")
            .build()
            .writeBy(UserRepositoryTest.SANJIGI);

    @DisplayName("저장 테스트")
    @Test
    void save() {
        Question question = questionRepository.save(Q1);
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getWriterId()).isEqualTo(Q1.getWriterId()),
                () -> assertThat(question.getContents()).isEqualTo(Q1.getContents()),
                () -> assertThat(question.getTitle()).isEqualTo(Q1.getTitle())
        );
    }

    @DisplayName("질문 ID 로 조회 테스트")
    @Test
    void findByDeletedFalse() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        questionRepository.save(Q3);
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).hasSize(3);
    }

    @DisplayName("delete 가 잘 되었는지 테스트")
    @Test
    void delete() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        Question question = questionRepository.save(Q3);
        questionRepository.delete(question);
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).hasSize(2);
    }

    @DisplayName("ID로 조회 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Question question = questionRepository.save(Q1);
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertThat(actual.orElse(null)).isEqualTo(question);
    }
}
