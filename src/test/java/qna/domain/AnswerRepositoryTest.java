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
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;

    Question q1;
    Answer a1, a2;

    @BeforeEach
    void setup(){
        q1 = questionRepository.save(QuestionTest.Q1);
        a1 = answerRepository.save(new Answer(UserTest.JAVAJIGI, q1, "답변내용1"));
        a2 = answerRepository.save(new Answer(UserTest.SANJIGI, q1, "답변내용2"));
    }

    @Test
    void save() {
        Answer expected = new Answer(
                UserTest.JAVAJIGI,
                QuestionTest.Q1,
                "답변 내용이에요.");
        Answer actual = answerRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    void findByDeletedFalse() {
        List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(q1.getId());
        assertThat(actual).contains(a1, a2);
    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(a1.getId());
        assertThat(actual).contains(a1);
    }
}
