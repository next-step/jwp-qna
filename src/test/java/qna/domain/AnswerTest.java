package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("save 테스트")
    @Test
    void save() {
        // given
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1.writeBy(user));
        Answer answer = Answer.of(user, question, "Answers Contents1");

        // when
        Answer newAnswer = answerRepository.save(answer);

        // then
        assertAll(
                () -> assertThat(newAnswer.getId()).isNotNull()
                , () -> assertThat(newAnswer.getWriter().getId()).isNotNull()
                , () -> assertThat(newAnswer.getQuestion().getId()).isNotNull()
                , () -> assertThat(newAnswer).isEqualTo(answer)
        );
    }

    @DisplayName("findByIdAndDeletedFalse 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        // given
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1.writeBy(user));
        Answer answer = Answer.of(user, question, "Answers Contents1");
        Answer newAnswer = answerRepository.save(answer);

        // when
        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(newAnswer.getId()).get();

        // then
        assertAll(
                () -> assertThat(findAnswer).isNotNull()
                , () -> assertThat(findAnswer.getWriter().getId()).isNotNull()
                , () -> assertThat(findAnswer.getQuestion().getId()).isNotNull()
                , () -> assertThat(findAnswer).isEqualTo(newAnswer)
                , () -> assertThat(findAnswer.isDeleted()).isFalse()
        );
    }

    @DisplayName("findByQuestionIdAndDeletedFalse 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        // given
        User user1 = userRepository.save(UserTest.JAVAJIGI);
        User user2 = userRepository.save(UserTest.SANJIGI);
        Question question1 = questionRepository.save(QuestionTest.Q1.writeBy(user1));
        Question question2 = questionRepository.save(QuestionTest.Q2.writeBy(user2));
        Answer newAnswer1 = answerRepository.save(Answer.of(user1, question1, "Answers Contents1"));
        Answer newAnswer2 = answerRepository.save(Answer.of(user2, question2, "Answers Contents2"));

        // when
        List<Answer> newAnswers = answerRepository.findByQuestionIdAndDeletedFalse(newAnswer1.getQuestion().getId());

        // then
        assertAll(
                () -> assertThat(newAnswers).hasSize(1)
                , () -> assertThat(newAnswers).containsExactly(newAnswer1)
                , () -> assertThat(newAnswers.get(0).isDeleted()).isFalse()
        );
    }
}
