package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void init() {
        answerRepository.deleteAll();
        userRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Test
    @DisplayName("입력된 questionId를 가지고, 삭제상태가 아닌 Answer를 가져올 수 있어야 한다")
    void findByQuestionIdAndDeletedFalse() {
        // given
        Answer expected = answerRepository.save(getAnswer());

        // when
        List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(expected.getQuestion().getId());

        // then
        assertThat(actual).usingFieldByFieldElementComparator()
                .containsExactly(expected);
    }

    @Test
    @DisplayName("입력된 AnswerId에 해당하면서 삭제상태가 아닌 Answer를 가져올 수 있어야 한다")
    void findByIdAndDeletedFalse() {
        // given
        Answer expected = getAnswer();
        answerRepository.save(expected);

        // when
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(expected.getId());

        // then
        assertThat(actual).contains(expected);
    }

    private Answer getAnswer() {
        User user = userRepository.save(JAVAJIGI);
        Question question = questionRepository.save(Q1.writeBy(user));
        Answer a1 = new Answer(user, question, "answer1");
        a1.toQuestion(question);
        return a1;
    }
}
