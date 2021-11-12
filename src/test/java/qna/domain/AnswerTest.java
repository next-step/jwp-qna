package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        목업_자바지기와_상지기_유저등록(UserTest.JAVAJIGI);
        목업_자바지기와_상지기_유저등록(UserTest.SANJIGI);
        목업_질문등록(QuestionTest.Q1);
        목업_질문등록(QuestionTest.Q2);
    }

    private void 목업_자바지기와_상지기_유저등록(final User user) {
        //given - save user
        given(userRepository.save(user)).willReturn(user);

        //when - save user
        userRepository.save(user);

        //then - save user
        verify(userRepository, times(1)).save(user);
    }

    private void 목업_질문등록(final Question question) {
        //given - save question
        given(questionRepository.save(question)).willReturn(question);

        //when - save question
        questionRepository.save(question);

        //then - save question
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void save() {
        //when
        final Answer javajigiAnswer = answerRepository.save(A1);
        final Answer sanjigiAnswer = answerRepository.save(A2);

        //then
        assertAll(
            () -> assertThat(javajigiAnswer.isOwner(UserTest.JAVAJIGI)).isTrue(),
            () -> assertThat(javajigiAnswer.getQuestionId()).isEqualTo(QuestionTest.Q1.getId()),
            () -> assertThat(sanjigiAnswer.isOwner(UserTest.SANJIGI)).isTrue(),
            () -> assertThat(sanjigiAnswer.getQuestionId()).isEqualTo(QuestionTest.Q1.getId())
        );
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        //when
        final Answer javajigiAnswer = answerRepository.save(A1);
        final Answer sanjigiAnswer = answerRepository.save(A2);

        //when
        final List<Answer> actualAnswers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

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
        final Answer javajigiAnswer = answerRepository.save(A1);

        //then
        final Answer actual = answerRepository.findByIdAndDeletedFalse(javajigiAnswer.getId())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue();
    }
}
