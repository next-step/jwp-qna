package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
public class AnswerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void save() {
        final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
        final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
        final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);
        final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
        final Answer A2 = new Answer(SANJIGI, Q2, "Answers Contents2");
        assertAll(
                () -> assertDoesNotThrow(() -> answerRepository.save(A1)),
                () -> assertDoesNotThrow(() -> answerRepository.save(A2))
        );
    }

    @Nested
    @DisplayName("find 관련 메서드 테스트")
    class FindMethods {
        private List<Question> savedQuestions;
        private List<Answer> savedAnswers;

        @BeforeEach
        void setup() {
            final User user = new User("ndka134yg", "1234", "사용자 1", "user-1@email.com");
            final User savedUser = userRepository.save(user);
            final List<Question> questions = new ArrayList<>(Arrays.asList(
                    new Question("question title 1", "question content 1").writeBy(savedUser),
                    new Question("question title 2", "question content 2").writeBy(savedUser),
                    new Question("question title 3", "question content 3").writeBy(savedUser).setDeleted(true)
            ));
            savedQuestions = questionRepository.saveAll(questions);
            final List<Answer> answers = new ArrayList<>(Arrays.asList(
                    new Answer(user, questions.get(0), "answer content 1"),
                    new Answer(user, questions.get(1), "answer content 2"),
                    new Answer(user, questions.get(0), "answer content 3").setDeleted(true)
            ));
            savedAnswers = answerRepository.saveAll(answers);
        }

        @Test
        void findByQuestionIdAndDeletedFalse() {
            final List<Answer> foundAnswers =
                    answerRepository.findByQuestionIdAndDeletedFalse(savedQuestions.get(0).getId());
            assertAll(
                    () -> assertThat(foundAnswers).hasSize(1),
                    () -> assertThat(foundAnswers.stream().noneMatch(Answer::isDeleted)).isTrue()
            );
        }

        @Test
        void findByIdAndDeletedFalse() {
            final Answer foundAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswers.get(0).getId()).get();
            assertAll(
                    () -> assertThat(foundAnswer.getId()).isEqualTo(savedAnswers.get(0).getId()),
                    () -> assertThat(foundAnswer.isDeleted()).isFalse()
            );
        }
    }
}
