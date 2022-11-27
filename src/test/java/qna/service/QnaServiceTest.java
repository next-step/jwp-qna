package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import qna.exceptions.CannotDeleteException;
import qna.domain.*;
import qna.helper.AnswerHelper;
import qna.helper.QuestionHelper;
import qna.helper.UserHelper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class QnaServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QnaService qnaService;

    @Nested
    class delete_성공 {
        private User user1;
        private Question question1;

        @BeforeEach
        void setup() {
            User savedUser1 = new UserHelper(userRepository)
                    .createUser("javajigi", "password", "name", "javajigi@slipp.net");

            final QuestionHelper questionHelper = new QuestionHelper(questionRepository);
            Question savedQuestion1 = questionHelper.createQuestion("title1", "contents1", savedUser1);
            Question savedQuestion2 = questionHelper.createQuestion("title2", "contents2", savedUser1);

            final AnswerHelper answerHelper = new AnswerHelper(answerRepository);
            answerHelper.createAnswer(savedUser1, savedQuestion1, "Answers Contents1");
            answerHelper.createAnswer(savedUser1, savedQuestion2, "Answers Contents2");
            answerHelper.createAnswer(savedUser1, savedQuestion1, "Answers Contents1-2");

            this.user1 = userRepository.save(savedUser1);
            this.question1 = questionRepository.save(savedQuestion1);
            questionRepository.save(savedQuestion2);
        }

        @Test
        void success() throws CannotDeleteException {
            // arrange
            final Question foundQuestion = questionRepository.findByIdAndDeletedFalse(question1.getId()).get();
            final List<Answer> foundAnswers = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());

            // assert
            assertThat(foundQuestion.isDeleted()).isFalse();
            assertAll(
                    () -> foundAnswers.forEach(foundAnswer -> assertThat(foundAnswer.isDeleted()).isFalse())
            );

            // act
            qnaService.deleteQuestion(user1, foundQuestion.getId());
            final Question deletedQuestion = questionRepository.findById(foundQuestion.getId()).get();

            // assert
            assertThat(deletedQuestion.isDeleted()).isTrue();
        }
    }

    @Nested
    class delete_다른_사람이_쓴_글 {
        private User user2;
        private Question question1;

        @BeforeEach
        void setup() {
            final UserHelper userHelper = new UserHelper(userRepository);
            User savedUser1 = userHelper.createUser("javajigi", "password", "name", "javajigi@slipp.net");
            User savedUser2 = userHelper.createUser("sanjigi", "password", "name", "sanjigi@slipp.net");

            Question savedQuestion1 = new QuestionHelper(questionRepository)
                    .createQuestion("title1", "contents1", savedUser1);

            userRepository.save(savedUser1);
            this.user2 = savedUser2;
            this.question1 = savedQuestion1;
        }

        @Test
        void fail() {
            // arrange
            final User otherWriter = userRepository.findById(user2.getId()).get();
            final Question questionToCheck = qnaService.findQuestionById(question1.getId());

            // assert
            assertThat(otherWriter).isNotNull();
            assertThat(questionToCheck.isDeleted()).isFalse();

            // act - assert
            assertThatThrownBy(() -> qnaService.deleteQuestion(otherWriter, questionToCheck.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class delete_성공_질문자_답변자_같음 {
        private User user1;
        private Question question1;
        private Answer answer1;

        @BeforeEach
        void setup() {
            User savedUser1 = new UserHelper(userRepository)
                    .createUser("javajigi", "password", "name", "javajigi@slipp.net");

            Question savedQuestion1 = new QuestionHelper(questionRepository)
                    .createQuestion("title1", "contents1", savedUser1);

            Answer savedAnswer1 = new AnswerHelper(answerRepository)
                    .createAnswer(savedUser1, savedQuestion1, "Answers Contents1");

            this.user1 = userRepository.save(savedUser1);
            this.answer1 = answerRepository.save(savedAnswer1);
            this.question1 = questionRepository.save(savedQuestion1);
        }

        @Test
        void success() throws CannotDeleteException {
            // arrange - act
            qnaService.deleteQuestion(user1, question1.getId());
            final Question deletedQuestion = questionRepository.findById(question1.getId()).get();
            final List<Answer> deletedAnswers = answerRepository.findByQuestionIdAndDeletedFalse(deletedQuestion.getId());

            // assert
            assertThat(deletedQuestion.isDeleted()).isTrue();
            assertAll(
                    () -> deletedAnswers.forEach(deletedAnswer -> assertThat(answer1.isDeleted()).isTrue())
            );
        }
    }

    @Nested
    class delete_답변_중_다른_사람이_쓴_글 {
        private User user1;
        private Question question1;

        @BeforeEach
        void setup() {
            final UserHelper userHelper = new UserHelper(userRepository);
            User savedUser1 = userHelper.createUser("javajigi", "password", "name", "javajigi@slipp.net");
            User savedUser2 = userHelper.createUser("sanjigi", "password", "name", "sanjigi@slipp.net");

            Question savedQuestion1 = new QuestionHelper(questionRepository)
                    .createQuestion("title1", "contents1", savedUser1);

            final AnswerHelper answerHelper = new AnswerHelper(answerRepository);
            answerHelper.createAnswer(savedUser1, savedQuestion1, "Answers Contents By User 1");
            answerHelper.createAnswer(savedUser2, savedQuestion1, "Answers Contents By User 2");

            this.user1 = userRepository.save(savedUser1);
            userRepository.save(savedUser2);
            this.question1 = questionRepository.save(savedQuestion1);
        }

        @Test
        void fail() {
            assertThatThrownBy(() -> qnaService.deleteQuestion(user1, question1.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
