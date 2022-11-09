package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);
    }

    @Test
    void 저장_및_조회() {
        Answer answer1 = answerRepository.save(A1);
        Answer answer2 = answerRepository.save(A2);

        Answer retrievedAnswer1 = answerRepository.findById(answer1.getId()).get();
        Answer retrievedAnswer2 = answerRepository.findById(answer2.getId()).get();

        assertAll(
                () -> assertThat(retrievedAnswer1.getId()).isEqualTo(A1.getId()),
                () -> assertThat(retrievedAnswer2.getId()).isEqualTo(A2.getId())
        );
    }
}
