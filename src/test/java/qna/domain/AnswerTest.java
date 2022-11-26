package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.exceptions.CannotDeleteException;
import qna.helper.AnswerHelper;
import qna.helper.QuestionHelper;
import qna.helper.UserHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void save() {
        final UserHelper userHelper = new UserHelper(userRepository);
        final User JAVAJIGI = userHelper.createUser("javajigi", "password", "name", "javajigi@slipp.net");
        final User SANJIGI = userHelper.createUser("sanjigi", "password", "name", "sanjigi@slipp.net");

        final QuestionHelper questionHelper = new QuestionHelper(questionRepository);
        final Question Q1 = questionHelper.createQuestion("title1", "contents1", JAVAJIGI);
        final Question Q2 = questionHelper.createQuestion("title2", "contents2", SANJIGI);

        final AnswerHelper answerHelper = new AnswerHelper(answerRepository);
        final Answer A1 = answerHelper.createAnswer(JAVAJIGI, Q1, "Answers Contents1");
        final Answer A2 = answerHelper.createAnswer(SANJIGI, Q2, "Answers Contents2");

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
            final User savedUser = new UserHelper(userRepository)
                    .createUser("ndka134yg", "1234", "사용자 1", "user-1@email.com");
            final List<Question> questions = new ArrayList<>(Arrays.asList(
                    new Question("question title 1", "question content 1").writeBy(savedUser),
                    new Question("question title 2", "question content 2").writeBy(savedUser)
            ));
            savedQuestions = questionRepository.saveAll(questions);
            final List<Answer> answers = new ArrayList<>(Arrays.asList(
                    new Answer(savedUser, questions.get(0), "answer content 1"),
                    new Answer(savedUser, questions.get(1), "answer content 2"),
                    new Answer(savedUser, questions.get(0), "answer content 3").setDeleted(true)
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

    @Nested
    @DisplayName("delete 메서드 테스트")
    class DeleteMethod {
        @Nested
        @DisplayName("성공하는 경우")
        class Success {
            private User user1;
            private Answer answer;

            @BeforeEach
            void setup() {
                final UserHelper userHelper = new UserHelper(userRepository);
                User user1 = userHelper.createUser("hahaha", "pwd", "name 1", "user-1@email.com");

                final QuestionHelper questionHelper = new QuestionHelper(questionRepository);
                Question question1 = questionHelper.createQuestion("title 1", "contents of a question", user1);

                final AnswerHelper answerHelper = new AnswerHelper(answerRepository);
                Answer answer1 = answerHelper.createAnswer(user1, question1, "answer 1");

                this.user1 = userRepository.save(user1);
                questionRepository.save(question1);
                this.answer = answerRepository.save(answer1);
            }

            @Test
            void success() {
                assertDoesNotThrow(() -> answer.delete(user1));
            }
        }

        @Nested
        @DisplayName("Answer 만들지 않은 사람이 삭제 시도하면 실패")
        class FailureDeleteByAnotherUser {
            private User user2;
            private Answer answer;

            @BeforeEach
            void setup() {
                final UserHelper userHelper = new UserHelper(userRepository);
                User user1 = userHelper.createUser("hahaha", "pwd", "name 1", "user-1@email.com");
                User user2 = userHelper.createUser("hihihi", "pwd", "name 2", "user-2@email.com");

                final QuestionHelper questionHelper = new QuestionHelper(questionRepository);
                Question question1 = questionHelper.createQuestion("title 1", "contents of a question", user1);

                final AnswerHelper answerHelper = new AnswerHelper(answerRepository);
                Answer answer1 = answerHelper.createAnswer(user1, question1, "answer 1");

                userRepository.save(user1);
                this.user2 = userRepository.save(user2);
                questionRepository.save(question1);
                this.answer = answerRepository.save(answer1);
            }

            @Test
            void failure() {
                assertThrows(CannotDeleteException.class, () -> answer.delete(user2));
            }
        }
    }
}
