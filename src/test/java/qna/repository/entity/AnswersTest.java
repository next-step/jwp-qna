package qna.repository.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.ContentType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnswersTest {
    User loginUser;
    Question question;
    Answer answer1;
    Answer answer2;
    Answers answers;

    @BeforeEach
    void setUp() {
        loginUser = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question(1L, "title1", "contents1");
        answer1 = new Answer(1L, loginUser, question, "Answers Contents1");
        answer2 = new Answer(2L, loginUser, question, "Answers Contents2");
        answers = new Answers();
        answers.add(answer1);
        answers.add(answer2);
    }

    @Test
    @DisplayName("답변이 삭제 되는지")
    void delete_answer() {
        answers.delete(loginUser);
        assertThat(answer1.isDeleted()).isTrue();
        assertThat(answer2.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제 이력이 반환되는지")
    void returns_delete_history() {
        List<DeleteHistory> deleteHistories = answers.delete(loginUser);
        assertThat(deleteHistories).containsOnly(
                new DeleteHistory(ContentType.ANSWER, answer1.getId(), answer1.getWriter()),
                new DeleteHistory(ContentType.ANSWER, answer2.getId(), answer2.getWriter())
        );
    }
}
