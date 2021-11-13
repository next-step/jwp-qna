package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    void save() {
        //given
        final User javajigi = 유저등록(UserTest.JAVAJIGI);
        final User sanjigi = 유저등록(UserTest.SANJIGI);

        final Question javajigiQuestion = 질문등록(javajigi, QuestionTest.Q1);

        //when
        final Answer javajigiAnswer = 답변등록(javajigi, javajigiQuestion, A1);
        final Answer sanjigiAnswer = 답변등록(sanjigi, javajigiQuestion, A2);

        //then
        assertAll(
            () -> assertThat(javajigiAnswer.isOwner(javajigi)).isTrue(),
            () -> assertThat(javajigiAnswer.getQuestion()).isEqualTo(javajigiQuestion),
            () -> assertThat(sanjigiAnswer.isOwner(sanjigi)).isTrue(),
            () -> assertThat(sanjigiAnswer.getQuestion()).isEqualTo(javajigiQuestion)
        );
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        //given
        final User javajigi = 유저등록(UserTest.JAVAJIGI);
        final User sanjigi = 유저등록(UserTest.SANJIGI);

        final Question javajigiQuestion = 질문등록(javajigi, QuestionTest.Q1);

        final Answer javajigiAnswer = 답변등록(javajigi, javajigiQuestion, A1);
        final Answer sanjigiAnswer = 답변등록(sanjigi, javajigiQuestion, A2);

        //when
        final List<Answer> actualAnswers = answerRepository.findByQuestionIdAndDeletedFalse(javajigiQuestion.getId());

        //then
        assertAll(
            () -> assertThat(actualAnswers).isNotEmpty(),
            () -> assertThat(actualAnswers).hasSize(2),
            () -> assertThat(actualAnswers).containsExactly(javajigiAnswer, sanjigiAnswer)
        );
    }

    @Test
    void findByIdAndDeletedFalse() {
        //given
        final User javajigi = 유저등록(UserTest.JAVAJIGI);
        final Question javajigiQuestion = 질문등록(javajigi, QuestionTest.Q1);
        final Answer javajigiAnswer = 답변등록(javajigi, javajigiQuestion, A1);

        //then
        final Answer actual = answerRepository.findByIdAndDeletedFalse(javajigiAnswer.getId())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(actual.isOwner(javajigi)).isTrue();
    }

    private User 유저등록(final User user) {
        return userRepository.save(user);
    }

    private Question 질문등록(final User user, final Question question) {
        return questionRepository.save(question.writeBy(user));
    }

    private Answer 답변등록(final User writer, final Question question, final Answer answer) {
        answer.setWriter(writer);
        answer.setQuestion(question);
        return answerRepository.save(answer);
    }
}
