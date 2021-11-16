package qna.domain;

import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class AnswersTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void 질문자와_답변자가_다른_경우_답변을_삭제할_수_없다() {
        //given
        User writer = userRepository.save(TestUserFactory.create("donkey"));
        User otherWriter = userRepository.save(TestUserFactory.create("donkey2"));
        Question question = questionRepository.save(TestQuestionFactory.create("title", "content", writer));
        question.addAnswer(TestAnswerFactory.create(writer, "Answers"));
        question.addAnswer(TestAnswerFactory.create(otherWriter, "otherWriter"));
        Answers answers = question.getAnswers();

        //when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> answers.delete(writer);

        //then
        AssertionsForClassTypes.assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    public void 질문자와_답변자가_같은_경우_답변을_삭제할_수_있다() throws CannotDeleteException {
        //given
        User writer = userRepository.save(TestUserFactory.create("donkey"));
        Question question = questionRepository.save(TestQuestionFactory.create("title", "content", writer));
        question.addAnswer(TestAnswerFactory.create(writer, "Answers"));
        question.addAnswer(TestAnswerFactory.create(writer, "Answers"));

        //when
        question.delete(writer);
        Question deletedQuestion = questionRepository.findByIdAndDeletedTrue(question.getId()).orElse(null);
        List<Answer> deletedAnswers = answerRepository.findByDeletedTrue();

        //then
        assertAll(
                () -> assertThat(deletedQuestion).isEqualTo(question),
                () -> assertThat(deletedAnswers).hasSize(2)
        );
    }
}
