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
        Question question = new Question("question", "contents");
        question.setWriter(savedQuestionWriter);
        Question savedQuestion = questionRepository.save(question);
        User savedAnswerWriter = userRepository.save(TestDummy.USER_JAVAJIGI);
        Answer answer = new Answer(TestDummy.USER_JAVAJIGI, savedQuestion, "answer contents");
        answer.setWriter(savedAnswerWriter);

        Answer savedAnswer = answerRepository.save(answer);

        assertAll(
            () -> assertThat(savedAnswer.getWriterId()).isEqualTo(answer.getWriterId()),
            () -> assertThat(savedAnswer.getQuestion().getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(savedAnswer.getQuestion().getContents()).isEqualTo(question.getContents()),
            () -> assertThat(savedAnswer.getQuestion().getWriter()).isEqualTo(question.getWriter()),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents()),
            () -> assertThat(savedAnswer.isDeleted()).isEqualTo(answer.isDeleted()),
            () -> assertThat(savedAnswer.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void read() {
        User savedQuestionWriter = userRepository.save(TestDummy.USER_SANJIGI);
        Question question = new Question("question", "contents");
        question.setWriter(savedQuestionWriter);
        Question savedQuestion = questionRepository.save(question);
        User savedAnswerWriter = userRepository.save(TestDummy.USER_JAVAJIGI);
        Answer answer = new Answer(TestDummy.USER_JAVAJIGI, savedQuestion, "answer contents");
        answer.setWriter(savedAnswerWriter);
        Long savedAnswerId = answerRepository.save(answer).getId();

        Answer savedAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswerId).get();

        assertAll(
            () -> assertThat(savedAnswer.getId()).isEqualTo(savedAnswer.getId()),
            () -> assertThat(savedAnswer.getWriterId()).isEqualTo(answer.getWriterId()),
            () -> assertThat(savedAnswer.getQuestion().getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(savedAnswer.getQuestion().getContents()).isEqualTo(question.getContents()),
            () -> assertThat(savedAnswer.getQuestion().getWriter()).isEqualTo(question.getWriter()),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents()),
            () -> assertThat(savedAnswer.isDeleted()).isEqualTo(answer.isDeleted()),
            () -> assertThat(savedAnswer.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }
}
