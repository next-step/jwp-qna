package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User savedQuestionWriter = userRepository.save(TestDummy.USER_SANJIGI);
        TestDummy.QUESTION1.setWriter(savedQuestionWriter);
        Question savedQuestion = questionRepository.save(TestDummy.QUESTION1);
        User savedAnswerWriter = userRepository.save(TestDummy.USER_JAVAJIGI);
        TestDummy.ANSWER1.setQuestion(savedQuestion);
        TestDummy.ANSWER1.setWriter(savedAnswerWriter);

        Answer savedAnswer = answerRepository.save(TestDummy.ANSWER1);

        assertAll(
            () -> assertThat(savedAnswer.getWriterId()).isEqualTo(TestDummy.ANSWER1.getWriterId()),
            () -> assertThat(savedAnswer.getQuestion().getTitle()).isEqualTo(TestDummy.QUESTION1.getTitle()),
            () -> assertThat(savedAnswer.getQuestion().getContents()).isEqualTo(TestDummy.QUESTION1.getContents()),
            () -> assertThat(savedAnswer.getQuestion().getWriter()).isEqualTo(TestDummy.QUESTION1.getWriter()),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(TestDummy.ANSWER1.getContents()),
            () -> assertThat(savedAnswer.isDeleted()).isEqualTo(TestDummy.ANSWER1.isDeleted()),
            () -> assertThat(savedAnswer.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void read() {
        User savedQuestionWriter = userRepository.save(TestDummy.USER_SANJIGI);
        TestDummy.QUESTION1.setWriter(savedQuestionWriter);
        Question savedQuestion = questionRepository.save(TestDummy.QUESTION1);
        User savedAnswerWriter = userRepository.save(TestDummy.USER_JAVAJIGI);
        TestDummy.ANSWER1.setQuestion(savedQuestion);
        TestDummy.ANSWER1.setWriter(savedAnswerWriter);
        Long savedAnswerId = answerRepository.save(TestDummy.ANSWER1).getId();

        Answer savedAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswerId).get();

        assertAll(
            () -> assertThat(savedAnswer.getId()).isEqualTo(savedAnswer.getId()),
            () -> assertThat(savedAnswer.getWriterId()).isEqualTo(TestDummy.ANSWER1.getWriterId()),
            () -> assertThat(savedAnswer.getQuestion().getTitle()).isEqualTo(TestDummy.QUESTION1.getTitle()),
            () -> assertThat(savedAnswer.getQuestion().getContents()).isEqualTo(TestDummy.QUESTION1.getContents()),
            () -> assertThat(savedAnswer.getQuestion().getWriter()).isEqualTo(TestDummy.QUESTION1.getWriter()),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(TestDummy.ANSWER1.getContents()),
            () -> assertThat(savedAnswer.isDeleted()).isEqualTo(TestDummy.ANSWER1.isDeleted()),
            () -> assertThat(savedAnswer.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }
}
