package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import qna.CannotDeleteException;
import qna.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Sql(value = {"classpath:db/qna/truncate.sql"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.properties")
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private User user1;
    private User user2;
    private Question question1;
    private Question question2;
    private Question question3;

    @Autowired
    private TestEntityManager tem;

    @Autowired
    private QuestionRepository questions;

    @BeforeEach
    void beforeEach() {
        user1 = tem.persistAndFlush(new User("user1", "password", "user1", "user1@user.co.kr"));
        user2 = tem.persistAndFlush(new User("user2", "password", "user2", "user2@user.co.kr"));

        question1 = tem.persistAndFlush(new Question("title", "contents").writeBy(user1));
        question2 = tem.persistAndFlush(new Question("title", "contents").writeBy(user2));
        question3 = tem.persistAndFlush(new Question("title", "contents").writeBy(user2));

        tem.persistAndFlush(new Answer(user1, question1, "contents"));
        tem.persistAndFlush(new Answer(user2, question1, "contents"));
        tem.persistAndFlush(new Answer(user2, question3, "contents"));

        tem.clear();
    }

    @DisplayName("질문을 작성한 회원인지 확인할 수 있다.")
    @Test
    void confirmWriter() {
        assertAll(
                () -> assertTrue(Q1.isOwner(UserTest.JAVAJIGI)),
                () -> assertFalse(Q1.isOwner(UserTest.SANJIGI))
        );
    }

    @DisplayName("질문에는 0개 이상의 답변이 있을 수 있다.")
    @Test
    void answers() {
        assertAll(
                () -> assertThat(questions.findById(question1.getId()).orElseThrow(NotFoundException::new)
                                          .getAnswers()).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(questions.findById(question2.getId()).orElseThrow(NotFoundException::new)
                                          .getAnswers()).hasSize(0)
        );
    }

    @DisplayName("질문한 사람만이 삭제 상태로 변경할 수 있다.")
    @Test
    void deleteByOwner() {
        Question question = questions.findById(question2.getId()).orElseThrow(NotFoundException::new);
        question.deleteBy(user2);

        assertThatCode(() -> assertThat(question.isDeleted()).isTrue())
                .doesNotThrowAnyException();
    }

    @DisplayName("질문한 사람이 아닌 경우 삭제 상태로 변경할 수 없다.")
    @Test
    void isOwner() {
        Question question = questions.findById(question2.getId()).orElseThrow(NotFoundException::new);

        assertThatThrownBy(() -> question.deleteBy(user1))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("다른 회원의 답변이 있는 경우 삭제 상태로 변경할 수 없다.")
    @Test
    void deleteByOthers() {
        Question question = questions.findById(question1.getId()).orElseThrow(NotFoundException::new);

        assertThatThrownBy(() -> question.deleteBy(user1))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("질문을 삭제할 때 답변이 있으면 함께 삭제 상태로 변경해야 한다.")
    @Test
    void deleteAll() {
        Question question = questions.findById(question3.getId()).orElseThrow(NotFoundException::new);
        question.deleteBy(user2);

        assertThat(question.getAnswers()).allMatch(Answer::isDeleted);
    }

    @DisplayName("질문을 삭제할 때 반드시 삭제 이력을 남겨야 한다.")
    @Test
    void history() {
        Question question = questions.findById(question3.getId()).orElseThrow(NotFoundException::new);
        List<DeleteHistory> histories = question.deleteBy(user2);

        assertThat(histories).hasSize(2);
    }
}
