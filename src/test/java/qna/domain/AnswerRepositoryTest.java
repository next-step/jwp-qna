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
    static final User writer = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    static final Question question = new Question(1L, "title1", "contents1").writeBy(writer);

    @Autowired
    AnswerRepository answerRepository;

    Answer answer;

    @BeforeEach
    void setUp() {
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
