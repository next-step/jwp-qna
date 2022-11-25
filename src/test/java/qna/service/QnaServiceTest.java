package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import qna.exceptions.CannotDeleteException;
import qna.domain.*;

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
            final User user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
            User savedUser1 = userRepository.save(user1);

            final Question question1 = new Question("title1", "contents1").writeBy(savedUser1);
            Question savedQuestion1 = questionRepository.save(question1);
            savedUser1.addQuestion(savedQuestion1);
            final Question question2 = new Question("title2", "contents2").writeBy(savedUser1);
            Question savedQuestion2 = questionRepository.save(question2);
            savedUser1.addQuestion(savedQuestion2);

            final Answer answer1 = new Answer(savedUser1, savedQuestion1, "Answers Contents1");
            Answer savedAnswer1 = answerRepository.save(answer1);
            savedUser1.addAnswer(savedAnswer1);
            savedQuestion1.addAnswer(savedAnswer1);
            final Answer answer2 = new Answer(savedUser1, savedQuestion2, "Answers Contents2");
            final Answer savedAnswer2 = answerRepository.save(answer2);
            savedUser1.addAnswer(savedAnswer2);
            savedQuestion2.addAnswer(savedAnswer2);
            final Answer answer3 = new Answer(savedUser1, savedQuestion1, "Answers Contents1-2");
            final Answer savedAnswer3 = answerRepository.save(answer3);
            savedUser1.addAnswer(savedAnswer3);
            savedQuestion1.addAnswer(savedAnswer3);

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
            final User user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
            User savedUser1 = userRepository.save(user1);
            final User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
            User savedUser2 = userRepository.save(user2);

            final Question question1 = new Question("title1", "contents1").writeBy(savedUser1);
            Question savedQuestion1 = questionRepository.save(question1);
            savedUser1.addQuestion(savedQuestion1);

            savedUser1 = userRepository.save(savedUser1);
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
                    .isInstanceOf(CannotDeleteException.class);
        }
    }

    @Nested
    class delete_성공_질문자_답변자_같음 {
        private User user1;
        private Question question1;
        private Answer answer1;

        @BeforeEach
        void setup() {
            final User user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
            User savedUser1 = userRepository.save(user1);

            final Question question1 = new Question("title1", "contents1").writeBy(savedUser1);
            Question savedQuestion1 = questionRepository.save(question1);
            savedUser1.addQuestion(savedQuestion1);

            final Answer answer1 = new Answer(savedUser1, savedQuestion1, "Answers Contents1");
            Answer savedAnswer1 = answerRepository.save(answer1);
            savedUser1.addAnswer(savedAnswer1);
            savedQuestion1.addAnswer(savedAnswer1);

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
        private User user2;
        private Question question1;

        @BeforeEach
        void setup() {
            final User user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
            User savedUser1 = userRepository.save(user1);
            final User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
            User savedUser2 = userRepository.save(user2);

            final Question question1 = new Question("title1", "contents1").writeBy(savedUser1);
            Question savedQuestion1 = questionRepository.save(question1);
            savedUser1.addQuestion(savedQuestion1);

            final Answer answer1 = new Answer(savedUser1, savedQuestion1, "Answers Contents By User 1");
            Answer savedAnswer1 = answerRepository.save(answer1);
            savedUser1.addAnswer(savedAnswer1);
            savedQuestion1.addAnswer(savedAnswer1);
            final Answer answer2 = new Answer(savedUser2, savedQuestion1, "Answers Contents By User 2");
            Answer savedAnswer2 = answerRepository.save(answer2);
            savedUser2.addAnswer(savedAnswer2);
            savedQuestion1.addAnswer(savedAnswer2);

            this.user1 = userRepository.save(savedUser1);
            this.user2 = userRepository.save(savedUser2);
            this.question1 = questionRepository.save(savedQuestion1);
        }

        @Test
        void fail() {
            assertThatThrownBy(() -> qnaService.deleteQuestion(user1, question1.getId()))
                    .isInstanceOf(CannotDeleteException.class);
        }
    }
}
