package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class EntityMappingTest {
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    EntityManager em;

    @Test
    void questionSaveAndFindTest() {
        Question expected = newQuestion();
        User writer = userRepository.save(newUser());
        expected.writeBy(writer);
        Question actual = questionRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
            () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
            () -> assertThat(actual.isDeleted()).isFalse(),
            () -> assertThat(actual.getWriterId()).isEqualTo(writer.getId()),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isEqualTo(expected.getCreatedAt())
        );
        assertThat(questionRepository.findByIdAndDeletedFalse(actual.getId()).orElseThrow(RuntimeException::new))
            .isEqualTo(actual);
    }

    @Test
    void answerSaveAndFindTest() {
        User user = newUser();
        User savedUser = userRepository.save(user);
        Question question = newQuestion();
        Question savedQuestion = questionRepository.save(question);
        Answer expected = new Answer(savedUser, savedQuestion, "contents");
        Answer actual = answerRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isEqualTo(actual.getCreatedAt()),
            () -> assertThat(actual.isDeleted()).isFalse(),
            () -> assertThat(actual.getQuestionId()).isEqualTo(savedQuestion.getId()),
            () -> assertThat(actual.getWriterId()).isEqualTo(savedUser.getId())
        );
        assertThat(answerRepository.findByIdAndDeletedFalse(actual.getId()).orElseThrow(RuntimeException::new))
            .isEqualTo(actual);
    }

    @Test
    void deleteHistorySaveAndFindTest() {
        User user = userRepository.save(newUser());
        Question question = questionRepository.save(newQuestion());
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, question.getId(), user.getId(),
            LocalDateTime.now());
        DeleteHistory actual = deleteHistoryRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContentType()).isEqualTo(expected.getContentType()),
            () -> assertThat(actual.getContentId()).isEqualTo(expected.getContentId()),
            () -> assertThat(actual.getCreateDate()).isEqualTo(expected.getCreateDate()),
            () -> assertThat(actual.getDeletedById()).isEqualTo(user.getId())
        );
        assertThat(deleteHistoryRepository.findById(actual.getId()).orElseThrow(RuntimeException::new))
            .isEqualTo(actual);
    }

    private Question newQuestion() {
        return new Question("제목", "내용");
    }

    private User newUser() {
        return new User("userId", UUID.randomUUID().toString(), "ming", "ming@gmail.com");
    }

}