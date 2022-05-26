package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    private User writer;

    @BeforeEach
    void setUp(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
        writer = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
    }

    @DisplayName("Question 저장")
    @Test
    void save() {
        final Question actual = questionRepository.save(new Question("title1", "contents1").writeBy(writer));

        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("id로 삭제되지 않은 Question 조회")
    @Test
    void findByIdAndDeletedFalse() {
        final Question expected = questionRepository.save(new Question("title1", "contents1").writeBy(writer));

        final Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(expected.getId());

        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @DisplayName("삭제되지 않은 Question 리스트 조회")
    @Test
    void findByDeletedFalse() {
        final Question question1 = questionRepository.save(new Question("title1", "contents1").writeBy(writer));
        final Question question2 = questionRepository.save(new Question("title2", "contents2").writeBy(writer));

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertThat(questions).hasSize(2);
        assertThat(questions).contains(question1, question2);
    }

}