package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.ContentType.*;

import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question(UserTest.JAVAJIGI, "title1", "contents1");

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Autowired
    private AnswerRepository answers;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        answers.deleteAll();
        questions.deleteAll();
        users.deleteAll();
    }

    @Test
    void save() {
        User user = users.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        Question expected = new Question(user, "title1", "contents1");

        Question actual = questions.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getContents()).isEqualTo(expected.getContents());
        assertThat(actual.getContents().getWriter()).isSameAs(user);
    }

    @Test
    @DisplayName("JPA가 식별자가 같은 엔티티에 대한 동일성을 보장하는지 테스트")
    void identity() {
        Question expected = saveNewDefaultQuestion();

        Question actual = questions.findById(expected.getId()).get();

        assertThat(actual == expected).isTrue();
    }

    @Test
    @DisplayName("ID로 삭제 후, 조회가 되지 않는지 테스트")
    void delete() {
        Question expected = saveNewDefaultQuestion();
        questions.deleteById(expected.getId());

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> questions.findById(expected.getId()).get()
        );
    }

    @Test
    void addAnswer() {
        Question question = saveNewDefaultQuestion();
        User user = users.save(new User("minseoklim", "password", "임민석", "mslim@naver.com"));
        Answer answer = answers.save(new Answer(user, "Answers Contents1"));
        question.addAnswer(answer);

        assertThat(question.getAnswers().contains(answer)).isTrue();
    }

    @Test
    void deleteBy() {
        Question question = saveNewDefaultQuestion();
        User writer = question.getContents().getWriter();
        Answer answer = answers.save(new Answer(writer, "이제는 잘 시간입니다"));
        question.addAnswer(answer);

        List<DeleteHistory> deleteHistories = question.deleteBy(writer);

        assertThat(deleteHistories).contains(
            new DeleteHistory(QUESTION, question.getId(), writer), new DeleteHistory(ANSWER, answer.getId(), writer)
        );
        assertThat(question.isDeleted()).isTrue();
        assertThat(question.getAnswers().allMatch(Answer::isDeleted)).isTrue();
    }

    @Test
    @DisplayName("질문자와 로그인 유저가 다를 때 예외 발생")
    void deleteByInvalidUser() {
        Question question = saveNewDefaultQuestion();
        User invalidUser = users.save(new User("minseoklim", "1234", "임민석", "mslim@slipp.net"));

        assertThatThrownBy(() -> question.deleteBy(invalidUser))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("로그인 유저가 아닌 다른 유저가 작성한 답변이 있을 경우 예외 발생")
    void deleteQuestionHavingAnswersFromOthers() {
        Question question = saveNewDefaultQuestion();
        User writer = question.getContents().getWriter();
        Answer answer = answers.save(new Answer(writer, "이제는 잘 시간입니다"));
        question.addAnswer(answer);
        User other = users.save(new User("minseoklim", "1234", "임민석", "mslim@slipp.net"));
        question.addAnswer(answers.save(new Answer(other, "이제는 잘 시간입니다")));

        assertThatThrownBy(() -> question.deleteBy(writer))
            .isInstanceOf(CannotDeleteException.class);

        entityManager.clear();
        Question refreshedQuestion = questions.findById(question.getId()).get();
        Answer refreshedAnswer = answers.findById(answer.getId()).get();
        assertThat(refreshedQuestion.isDeleted()).isFalse();
        assertThat(refreshedAnswer.isDeleted()).isFalse();
    }

    private Question saveNewDefaultQuestion() {
        User user = users.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        Question defaultQuestion = new Question(user, "title1", "contents1");
        return questions.save(defaultQuestion);
    }
}
