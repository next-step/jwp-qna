package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private UserRepository users;

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    public void save_테스트() {

        User writer = new User("javajigi", "password", "name", "javajigi@slipp.net");
        users.save(writer);

        Question question = new Question("title1", "contents1").writeBy(writer);
        Question actual = questions.save(question);
        assertThat(actual.getTitle()).isEqualTo("title1");
    }

    @Test
    public void update_deleted_테스트() {
        User user_saved = users.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        Question question_saved = questions.save(new Question("title1", "contents1").writeBy(user_saved));

        Question expected = questions.findById(question_saved.getId()).get();
        expected.setDeleted(true);
        Question actual = questions.save(expected);

        assertThat(actual.isDeleted()).isTrue();
    }

    @Test
    public void isPossibleDelete_작성자_아닌_유저가_삭제시도_오류확인() {
        User writer = new User("javajigi", "password", "name", "javajigi@slipp.net");
        users.save(writer);
        Question question = new Question(1L, "title1", "contents1").writeBy(writer);
        Question question_saved = questions.save(question);

        assertThatThrownBy(() -> question_saved.isPossibleDelete(UserTest.JUNSEONG))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void isPossibleDelete_질문자와_다른답변자_있는경우_삭제시도_오류확인() {
        User writer = new User("javajigi", "password", "name", "javajigi@slipp.net");
        users.save(writer);
        User answerUser = new User("chajs226", "password", "name", "chajs226@gmail.com");
        users.save(answerUser);

        Question question = new Question(1L, "title1", "contents1").writeBy(writer);
        Question questionSaved = questions.save(question);

        Answer answer = new Answer(answerUser, questionSaved, "contents");
        answer.toQuestion(questionSaved);

        assertThatThrownBy(() -> questionSaved.isPossibleDelete(writer))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void deleteQuestion_질문자_답변자_같은경우_삭제_성공확인() throws CannotDeleteException {
        User writer = new User("javajigi", "password", "name", "javajigi@slipp.net");
        users.save(writer);

        Question question = new Question(1L, "title1", "contents1").writeBy(writer);
        Question questionSaved = questions.save(question);

        Answer answer = new Answer(writer, questionSaved, "contents");

        answer.toQuestion(questionSaved);
        Answer answerSaved = answers.save(answer);

        questionSaved.deleteQuestion(questionSaved.getId(), writer);

        assertThat(questionSaved.isDeleted()).isTrue();
        assertThat(answerSaved.isDeleted()).isTrue();
    }
}