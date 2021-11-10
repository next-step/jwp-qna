package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User savedQuestionWriter = userRepository.save(UserTest.SANJIGI);
        QuestionTest.Q1.setWriter(savedQuestionWriter);
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        User savedAnswerWriter = userRepository.save(UserTest.JAVAJIGI);
        A1.setQuestion(savedQuestion);
        A1.setWriter(savedAnswerWriter);

        Answer savedAnswer = answerRepository.save(A1);

        assertAll(
            () -> assertThat(savedAnswer.getWriterId()).isEqualTo(A1.getWriterId()),
            () -> assertThat(savedAnswer.getQuestion().getTitle()).isEqualTo(QuestionTest.Q1.getTitle()),
            () -> assertThat(savedAnswer.getQuestion().getContents()).isEqualTo(QuestionTest.Q1.getContents()),
            () -> assertThat(savedAnswer.getQuestion().getWriter()).isEqualTo(QuestionTest.Q1.getWriter()),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(A1.getContents()),
            () -> assertThat(savedAnswer.isDeleted()).isEqualTo(A1.isDeleted()),
            () -> assertThat(savedAnswer.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void read() {
        User savedQuestionWriter = userRepository.save(UserTest.SANJIGI);
        QuestionTest.Q1.setWriter(savedQuestionWriter);
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        User savedAnswerWriter = userRepository.save(UserTest.JAVAJIGI);
        A1.setQuestion(savedQuestion);
        A1.setWriter(savedAnswerWriter);
        Answer a = answerRepository.save(A1);

        Answer savedAnswer = answerRepository.findByIdAndDeletedFalse(a.getId()).get();

        assertAll(
            () -> assertThat(savedAnswer.getId()).isEqualTo(savedAnswer.getId()),
            () -> assertThat(savedAnswer.getWriterId()).isEqualTo(A1.getWriterId()),
            () -> assertThat(savedAnswer.getQuestion().getTitle()).isEqualTo(QuestionTest.Q1.getTitle()),
            () -> assertThat(savedAnswer.getQuestion().getContents()).isEqualTo(QuestionTest.Q1.getContents()),
            () -> assertThat(savedAnswer.getQuestion().getWriter()).isEqualTo(QuestionTest.Q1.getWriter()),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(A1.getContents()),
            () -> assertThat(savedAnswer.isDeleted()).isEqualTo(A1.isDeleted()),
            () -> assertThat(savedAnswer.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }
}
