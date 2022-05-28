package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void 답변_생성() {

        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        Answer expected = new Answer(user, question, "Answers Contents1");
        Answer actual = answerRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter().getId()).isEqualTo(expected.getWriter().getId()),
                () -> assertThat(actual.getQuestion().getId()).isEqualTo(expected.getQuestion().getId()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    void 질문_아이디로_삭제_되지_않은_답변_조회() throws CannotDeleteException {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));

        Answer deletedAnswer = answerRepository.save(
                new Answer(user, question, "Answers Contents1"));
        Answer notDeletedAnswer = answerRepository.save(
                new Answer(user, question, "Answers Contents2"));

        deletedAnswer.delete(user);

        List<Answer> foundAnswers = answerRepository.findByQuestionIdAndDeletedFalse(
                deletedAnswer.getQuestion().getId());

        assertThat(foundAnswers).containsExactly(notDeletedAnswer);
    }


}
