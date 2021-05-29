package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writtenBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writtenBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private Question question;
    private User user;

    @BeforeEach
    void setup() {
        user = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writtenBy(user));
    }

    @AfterEach
    void deleteAll() {
        questionRepository.deleteAll();
    }

    @Test
    void deleteHistory_Test() {
        DeleteHistory deleteHistory = question.deleteAndHistory();

        assertAll(
                () -> assertThat(deleteHistory.getContentType().equals(question.getContents())),
                () -> assertThat(deleteHistory.getContentId().equals(question.getId())),
                () -> assertThat(deleteHistory.getUser().equals(question.getWriter())),
                () -> assertThat(deleteHistory.getCreateDate()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void question_삭제하려는_유저의_유효성_성공_Test() throws CannotDeleteException {
        question.validateQuestionProprietary(user);
    }

    @Test
    void question_삭제하려는_유저의_question_작성자_유효성_실패_Test() throws CannotDeleteException {
        assertThatThrownBy(() ->
                question.validateQuestionProprietary(UserTest.SANJIGI)
        ).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void question_삭제하려는_유저의_answer_작성자_유효성_실패_Test() {
        Answer answer = answerRepository.save(new Answer(userRepository.save(UserTest.SANJIGI), question, "contents"));
        question.addAnswer(answer);

        assertThatThrownBy(() ->
                question.validateAnswersProprietary(user)
        ).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void question_answer_다대일_양방향_Test_1() {
        Answer expected = answerRepository.save(new Answer(user, question, "contents"));
        question.addAnswer(expected);

        List<Answer> actuals = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        question = questionRepository.findById(question.getId()).get();
        question = questionRepository.findById(question.getId()).orElseThrow(NoSuchElementException::new);

        assertThat(actuals).containsExactly(expected);
        assertThat(question.getAnswers()).containsExactly(expected);
    }

    @Test
    void question_answer_다대일_양방향_Test_2() {
        List<Answer> answers = Arrays.asList(new Answer(user, question, "contents"),
                new Answer(user, question, "contents"),
                new Answer(user, question, "contents"));
        for (Answer answer : answers) {
            question.addAnswer(answerRepository.save(answer));
        }

        questionRepository.flush();

        List<Question> questions = questionRepository.findByDeletedFalse();
        Question actualQuestion = questions.get(0);
        List<Answer> actualAnswers = actualQuestion.getAnswers();

        assertThat(actualAnswers).hasSize(3);
    }

    @Test
    void save() {
        Question expected = new Question("title1", "contents1").writtenBy(user);
        Question actual = questionRepository.save(expected);

        questionRepository.findById(actual.getId())
                .orElseThrow(IllegalArgumentException::new);
    }

    @Test
    void save2() {
        Question expected = new Question("title1", "contents1").writtenBy(user);
        Question actual = questionRepository.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle())
        );
    }

    @Test
    void findById() {
        Question actual = questionRepository.findById(question.getId()).get();

        assertThat(actual).isEqualTo(question);
    }

    @Test
    void update() {
        assertThat(question.getWriter()).isEqualTo(user);

        User expected = userRepository.save(new User("userId", "password", "name", "email"));
        question.setWriter(expected);

        assertThat(questionRepository.findById(question.getId()).get().getWriter()).isEqualTo(expected);
    }

    @Test
    void delete() {
        questionRepository.delete(question);

        assertThat(questionRepository.findById(question.getId())).isNotPresent();
    }

}
