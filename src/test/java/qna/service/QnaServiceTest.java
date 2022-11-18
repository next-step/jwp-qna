package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import qna.exceptions.CannotDeleteException;
import qna.domain.*;

import java.util.ArrayList;
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
    private DeleteHistoryService deleteHistoryService;
    @Autowired
    private QnaService qnaService;

    @Nested
    class delete_성공 {
        private User user1;
        private Question question1;

        @BeforeEach
        void setup() {
            final User user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
            final User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
            final User user3 = new User("pobi", "password", "name", "pobi@slipp.net");
            this.user1 = userRepository.save(user1);
            final List<User> users = new ArrayList<>(2);
            users.add(user2);
            users.add(user3);
            userRepository.saveAll(users);

            final Question question1 = new Question("title1", "contents1").writeBy(this.user1);
            final Question question2 = new Question("title2", "contents2").writeBy(this.user1);
            this.question1 = questionRepository.save(question1);
            questionRepository.save(question2);

            final Answer answer1 = new Answer(this.user1, question1, "Answers Contents1");
            final Answer answer2 = new Answer(this.user1, question2, "Answers Contents2");
            final Answer answer3 = new Answer(this.user1, question1, "Answers Contents1-2");
            question1.addAnswer(answer1);
            question2.addAnswer(answer2);
            question1.addAnswer(answer3);
            final List<Answer> answers = new ArrayList<>(3);
            answers.add(answer1);
            answers.add(answer2);
            answers.add(answer3);
            final List<Question> questions = new ArrayList<>(3);
            questions.add(question1);
            questions.add(question2);
            answerRepository.saveAll(answers);
            questionRepository.saveAll(questions);
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
            final User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
            final User savedUser1 = userRepository.save(user1);
            this.user2 = userRepository.save(user2);

            final Question question1 = new Question("title1", "contents1").writeBy(savedUser1);
            this.question1 = questionRepository.save(question1);
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
            this.user1 = userRepository.save(user1);

            final Question question1 = new Question("title1", "contents1").writeBy(this.user1);
            this.question1 = questionRepository.save(question1);

            answer1 = new Answer(this.user1, question1, "Answers Contents1");
            question1.addAnswer(answer1);
            this.answer1 = answerRepository.save(answer1);
            this.question1 = questionRepository.save(question1);
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
            final User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
            this.user1 = userRepository.save(user1);
            this.user2 = userRepository.save(user2);

            final Question question1 = new Question("title1", "contents1").writeBy(this.user1);
            this.question1 = questionRepository.save(question1);

            final Answer answer1 = new Answer(this.user1, question1, "Answers Contents By User 1");
            final Answer answer2 = new Answer(this.user2, question1, "Answers Contents By User 2");
            question1.addAnswer(answer1);
            question1.addAnswer(answer2);
            answerRepository.save(answer1);
            answerRepository.save(answer2);
            this.question1 = questionRepository.save(question1);
        }

        @Test
        void fail() {
            assertThatThrownBy(() -> qnaService.deleteQuestion(user1, question1.getId()))
                    .isInstanceOf(CannotDeleteException.class);
        }
    }
}
