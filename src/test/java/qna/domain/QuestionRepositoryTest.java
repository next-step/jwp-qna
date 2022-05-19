package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    Question question;
    User writer;

    @BeforeEach
    void setUp() {
        writer = new User("javajigi", "password", "name", "javajigi@slipp.net");
        userRepository.save(writer);
        question = new Question("title1", "contents1").writeBy(writer);
        questionRepository.save(question);
    }

    @Test
    void 생성() {
        assertThat(question.getId()).isNotNull();
    }

    @Test
    void 수정() {
        question.edit("content111");
        Question findQuestion = questionRepository.findById(question.getId()).get();
        assertThat(findQuestion.getContents()).isEqualTo("content111");
    }

    @Test
    void 조회() {
        Question findQuestion = questionRepository.findById(question.getId()).get();
        assertThat(findQuestion).isSameAs(question);
    }

    @Test
    void 삭제() {
        questionRepository.delete(question);
        assertThat(questionRepository.findById(question.getId())).isNotPresent();
    }

    @Test
    void 제목_NotNull() {
        Question question = new Question(null, "contents1").writeBy(writer);
        assertThatThrownBy(() -> questionRepository.save(question))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 제목_최대길이_100() {
        String size101 = new String(new char[101]).replace("\0", "a");
        Question question = new Question(size101, "contents1").writeBy(writer);
        assertThatThrownBy(() -> questionRepository.save(question))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
