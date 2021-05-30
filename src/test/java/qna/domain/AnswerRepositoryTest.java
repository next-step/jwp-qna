package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

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

    @Test
    @DisplayName("질문 글에 저장된 모든 답변을 조회한다.")
    void findByQuestionIdAndDeletedFalse() {
        //given
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);

        Question question = questionRepository.save(Q1);

        Answer firstAnswer = answerRepository.save(A1);
        firstAnswer.toQuestion(question);
        Answer secondAnswer = answerRepository.save(A2);
        secondAnswer.toQuestion(question);

        //when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        //then
        assertThat(answers).containsExactlyInAnyOrder(
                firstAnswer,
                secondAnswer
        );
    }
}
