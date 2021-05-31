package qna.domain;

import static qna.domain.UserTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {
    public static final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.save(JAVAJIGI);
        questionRepository.save(Q1);
    }

    @AfterEach
    void deleteAll() {
        userRepository.deleteAll();
        questionRepository.deleteAll();
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("답변 생성")
    void create() {
        //given
        //when
        Answer answer = answerRepository.save(A1);
        //then
        assertThat(answer.getId()).isEqualTo(A1.getId());
    }

    @Test
    @DisplayName("답변 조회 - id")
    void findById() {
        //given
        //when
        Answer save = answerRepository.save(A1);
        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(save.getId());
        //then
        assertThat(answer.isPresent()).isTrue();
    }

    @Test
    @DisplayName("답변 조회 - questionId")
    void findByQuestionId() {
        //given
        //when
        Answer save = answerRepository.save(A1);
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(save.getQuestion().getId());
        //then
        assertThat(answers.size() > 0).isTrue();
        assertThat(answers.get(0).getQuestion()).isEqualTo(save.getQuestion());
    }
}
