package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private Question question;
    private Answer firstAnswer;
    private Answer secondAnswer;

    @BeforeEach
    void setUp() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);

        question = questionRepository.save(Q1);

        firstAnswer = answerRepository.save(A1);
        firstAnswer.toQuestion(question);
        secondAnswer = answerRepository.save(A2);
        secondAnswer.toQuestion(question);
    }

    @Test
    @DisplayName("질문 글에 저장된 모든 답변을 조회한다.")
    void findByQuestionIdAndDeletedFalse() {
        //when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        //then
        assertThat(answers).containsExactlyInAnyOrder(
                firstAnswer,
                secondAnswer
        );
    }

    @Test
    @DisplayName("ID에 해당하는 답변을 조회한다.")
    void findByIdAndDeletedFalse() {
        //when
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(firstAnswer.getId());

        //then
        assertThat(findAnswer.get()).isEqualTo(firstAnswer);
    }
}
