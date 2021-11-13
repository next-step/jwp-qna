package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import qna.common.CommonRepositoryTest;

class AnswerRepositoryTest extends CommonRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    private User writer;
    private Question question;

    @BeforeEach
    void setUp() {
        this.writer = userRepository.save(
            new User(1L, "writer", "123", "writer", "writer@mail.com"));
        this.question = questionRepository.save(
            new Question(1L, "question title", "question contents"));
    }

    @DisplayName("Answer 를 저장한다")
    @Test
    void save() {
        // given
        String answerContents = "answer";
        Answer answer = createAnswer(answerContents);

        // when
        Answer savedAnswer = answerRepository.save(answer);

        // then
        assertAll(
            () -> assertNotNull(savedAnswer.getId()),
            () -> assertTrue(savedAnswer.isOwner(writer)),
            () -> assertEquals(question, savedAnswer.getQuestion()),
            () -> assertEquals(1, savedAnswer.getQuestion().getAnswers().size()),
            () -> assertEquals(answerContents, savedAnswer.getContents()),
            () -> assertFalse(savedAnswer.isDeleted()),
            () -> assertNotNull(savedAnswer.getCreatedAt())
        );
    }

    @DisplayName("Answer 를 아이디로 찾는다")
    @Test
    void findById() {
        // given
        Answer savedAnswer = answerRepository.save(createAnswer("answer"));

        // when
        Answer findAnswer = answerRepository.findById(savedAnswer.getId()).orElse(null);

        // then
        assertNotNull(findAnswer);
        assertEquals(savedAnswer, findAnswer);
    }

    @DisplayName("삭제되지 않은 Answer 를 아이디로 찾는다")
    @Test
    void findByIdAndDeletedFalse() {
        // given
        Answer savedAnswer = answerRepository.save(createAnswer("answer"));

        // when
        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId()).orElse(null);

        // then
        assertNotNull(findAnswer);
        assertEquals(savedAnswer, findAnswer);
    }

    @DisplayName("삭제된 Answer 는 아이디로 찾지 못한다")
    @Test
    void findByIdAndDeletedFalseNotFound() {
        // given
        Answer savedAnswer = answerRepository.save(createAnswer("answer"));
        savedAnswer.setDeleted(true);

        // when
        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId()).orElse(null);

        // then
        assertNull(findAnswer);
    }

    @DisplayName("question 아이디로 삭제되지 않은 answers 를 찾는다")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        // given
        String firstAnswerContents = "answer1";
        String secondAnswerContents = "answer2";
        String thirdAnswerContents = "answer3";
        answerRepository.save(createAnswer(firstAnswerContents));
        answerRepository.save(createAnswer(secondAnswerContents));
        answerRepository.save(createAnswer(thirdAnswerContents));

        // when
        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        // then
        assertEquals(3, findAnswers.size());
        assertThat(findAnswers).extracting("question").extracting("id").containsExactly(
            question.getId(), question.getId(), question.getId());
        assertThat(findAnswers).extracting("contents").containsExactly(
            firstAnswerContents, secondAnswerContents, thirdAnswerContents);
    }

    private Answer createAnswer(String answerContents) {
        return new Answer(writer, question, answerContents);
    }
}
