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

        /*
            Question객체와 첫번째 Answer객체는 로그인 유저가 작성한 것이기 때문에 유효성 검사를 통과했고, 두번째 Answer 객체만 다른 유저가 작성
            한 것이므로, 앞의 2객체의 deleted 필드는 이미 true로 변경되었기 때문에 혹시 삭제가 되면 어떡하지?라는 마음으로 테스트 코드를 작성했습니다.

            처음엔 아래와 같은 코드를 추가했는데 테스트가 실패하길래 '아 객체 상태는 이미 바뀌어서 어떻게 안되는 구나. 그래도 DB에는 안날라가겠지'라고
            생각했습니다.
            assertThat(question.isDeleted()).isFalse();
            assertThat(answer.isDeleted()).isFalse();

            그래서 아래와 같은 코드를 추가했는데 또 실패했습니다. 근데 지금 생각해보니 하나의 Transaction안에서 영속성 컨텍스트를 공유하다보니 어차피
            똑같은 게 나오는 거였습니다.
            Question refreshedQuestion = questions.findById(question.getId()).get();
            Answer refreshedAnswer = answers.findById(answer.getId()).get();
            assertThat(refreshedQuestion.isDeleted()).isFalse();
            assertThat(refreshedAnswer.isDeleted()).isFalse();

            제가 의문인 건, 저 2가지 코드 중 하나를 테스트 코드에 추가하면 콘솔에 업데이트 쿼리가 찍힌다는 겁니다;; 익셉션이 발생한 범위 밖에서 엔티티
            객체가 다시 참조되면서 익셉션과 관련 없다고 JPA가 판단하는 걸까요? 만약 그렇다고 한다면 실제 코드에서는 상관 없을 것 같네요. 엔티티가
            익셉션 범위 밖에 있는 있을 일은 없을테니까요.(catch하지 않고 throw하는 게 맞다고 생각해서입니다.)
        */

        // 아래 주석을 풀어보시면 update 쿼리를 확인하실 수 있습니다.
        // assertThat(question.isDeleted()).isFalse();
        // assertThat(answer.isDeleted()).isFalse();

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
