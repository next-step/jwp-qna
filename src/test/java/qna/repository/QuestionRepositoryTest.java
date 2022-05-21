package qna.repository;

import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @BeforeEach
    void setup() {
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);
    }

    @Test
    void 질문_등록_테스트() {
        Question question = new Question("title3", "contents3").writeBy(UserTest.JAVAJIGI);
        questionRepository.save(question);
        assertThat(question.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @DisplayName("not null인 필드에 null을 넣었을 때")
    @Test
    void 질문_title_null_등록_테스트() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            Question question = new Question(null, "content3").writeBy(UserTest.JAVAJIGI);
            questionRepository.save(question);
        });
    }

    @DisplayName("deleted가 false인 객체들 검색")
    @Test
    void findByDeletedFalse_테스트() {
        List<Question> byDeletedFalse = questionRepository.findByDeletedFalse();

        assertThat(byDeletedFalse.size()).isEqualTo(2);
    }

    @Test
    void findByIdAndDeletedFalse_테스트() {
        Question question3 = new Question("title3", "contents3").writeBy(UserTest.JAVAJIGI);
        questionRepository.save(question3);
        Optional<Question> byIdAndDeletedFalse = questionRepository.findByIdAndDeletedFalse(question3.getId());

        assertThat(byIdAndDeletedFalse.isPresent()).isTrue();
        assertThat(byIdAndDeletedFalse.get() == question3).isTrue();
    }

    @Test
    void title_Over_Max_Length_테스트() {

        String title = new Random().ints(97, 122 + 1)
                .limit(200)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        assertThrows(DataIntegrityViolationException.class, () -> {
            Question question = new Question(title,"content3").writeBy(UserTest.JAVAJIGI);
            questionRepository.save(question);
        });
    }
}
