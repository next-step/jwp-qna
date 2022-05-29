package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;

    User testWriter;
    Question question1, question2;

    @BeforeEach
    void setup(){
        testWriter = userRepository.save(UserTest.ROCKPRO87);
        question1 = questionRepository.save(new Question("질문 제목1", "질문 내용1").writeBy(testWriter));
        question2 = questionRepository.save(new Question("질문 제목2", "질문 내용2").writeBy(testWriter));
    }

    @Test
    void save() {
        Question expected = new Question(
                "질문 제목이에요",
                "질문 내용이에요.")
                .writeBy(testWriter);
        Question actual = questionRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(actual.getWriter()).isEqualTo(testWriter)
        );
    }

    @Test
    void findByDeletedFalse() {
        List<Question> actual = questionRepository.findByDeletedFalse();
        assertThat(actual).contains(question1, question2);
    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question1.getId());
        assertThat(actual).contains(question1);
    }
}
