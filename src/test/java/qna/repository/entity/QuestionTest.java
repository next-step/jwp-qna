package qna.repository.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.domain.ContentType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    User loginUser;
    Question question;

    @BeforeEach
    void setUp() {
        loginUser = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question(1L, "title1", "contents1");
    }

    @Test
    @DisplayName("질문이 삭제되는 지")
    void delete_question() {
        question.writeBy(loginUser);
        question.delete(loginUser);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("다른 사람이 쓴 글인 경우 삭제시 오류가 반환되는지")
    void throw_error_on_not_permitted() {
        User otherUser = new User(2L, "steve", "password", "steve", "steve@steve.com");
        question.writeBy(otherUser);
        assertThatThrownBy(() -> question.delete(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제 이력이 반환되는 지")
    void returns_delete_history() {
        question.writeBy(loginUser);
        Answer answer = new Answer(1L, loginUser, question, "Answers Contents2");
        List<DeleteHistory> deleteHistories = question.delete(loginUser);
        assertThat(deleteHistories).containsOnly(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter())
        );
    }
}
