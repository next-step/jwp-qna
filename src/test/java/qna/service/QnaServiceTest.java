package qna.service;

import org.junit.jupiter.api.BeforeEach;
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
    private DeleteHistoryService deleteHistoryService;
    @Autowired
    private QnaService qnaService;

    private User user1;
    private User user2;
    private Question question1;
    private Question question2;
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    public void setUp() throws Exception {
        final User user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
        final User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        assertThat(userRepository).isNotNull();
        this.user1 = userRepository.save(user1);
        this.user2 = userRepository.save(user2);

        final Question question1 = new Question("title1", "contents1").writeBy(this.user1);
        final Question question2 = new Question("title2", "contents2").writeBy(this.user1);
        this.question1 = questionRepository.save(question1);
        this.question2 = questionRepository.save(question2);

        answer1 = new Answer(this.user1, question1, "Answers Contents1");
        answer2 = new Answer(this.user2, question2, "Answers Contents2");
        question1.addAnswer(answer1);
        question2.addAnswer(answer2);
        this.answer1 = answerRepository.save(answer1);
        this.answer2 = answerRepository.save(answer2);
        this.question1 = questionRepository.save(question1);
        this.question2 = questionRepository.save(question2);
    }

    @Test
    public void delete_성공() throws Exception {
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

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        assertThatThrownBy(() -> qnaService.deleteQuestion(user2, question1.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
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

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        final Question question = new Question("title 2", "question contents x").writeBy(user1);
        final Answer answer = new Answer(user1, question, "answer contents x");
        question.addAnswer(answer);
        final Question savedQuestion = questionRepository.save(question2);

        // assert
        assertThatThrownBy(() -> qnaService.deleteQuestion(user1, savedQuestion.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }
}
