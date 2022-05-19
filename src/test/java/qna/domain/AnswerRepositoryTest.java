package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;

    Answer answer;
    User writer;
    Question question;

    @BeforeEach
    void setUp() {
        writer = new User("javajigi", "password", "name", "javajigi@slipp.net");
        userRepository.save(writer);
        question = new Question("title1", "contents1").writeBy(writer);
        questionRepository.save(question);
        answer = new Answer(writer, question, "Answers Contents1");
        answerRepository.save(answer);
    }

    @Test
    void 생성() {
        assertThat(answer.getId()).isNotNull();
    }

    @Test
    void 수정() {
        answer.setContents("Contents11");
        Answer findAnswer = answerRepository.findById(answer.getId()).get();
        assertThat(findAnswer.getContents()).isEqualTo("Contents11");
    }

    @Test
    void 조회() {
        Answer findAnswer = answerRepository.findById(answer.getId()).get();
        assertThat(findAnswer).isSameAs(answer);
    }

    @Test
    void 삭제() {
        answerRepository.delete(answer);
        assertThat(answerRepository.findById(answer.getId())).isNotPresent();
    }
}
