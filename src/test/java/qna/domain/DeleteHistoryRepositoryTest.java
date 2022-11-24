package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistories;

    @Autowired
    AnswerRepository answers;

    @Autowired
    UserRepository users;

    @Autowired
    QuestionRepository questions;

    @Autowired
    TestEntityManager manager;

    User user;
    Question question;
    Answer answer;

    DeleteHistory deleteHistory1 = null;
    DeleteHistory deleteHistory2 = null;

    @BeforeEach
    void setUp() {
        user = users.save(new User("testUser", "qwerty1234", "김철수", "testUser@nextstep.com"));
        question = questions.save(new Question("testUser question", "질문내용123").writeBy(user));
        answer = answers.save(new Answer(user, question, "답변123"));
        answers.delete(answer);
        questions.delete(question);
        manager.flush();
        deleteHistory1 = deleteHistories.save(
                new DeleteHistory(ContentType.ANSWER, answer.getId(), user,
                        LocalDateTime.now()));
        deleteHistory2 = deleteHistories.save(
                new DeleteHistory(ContentType.QUESTION, answer.getId(), user,
                        LocalDateTime.now()));
    }

    @DisplayName("delete_history 저장 확인")
    @Test
    void save() {
        assertAll(
                () -> assertThat(deleteHistory1).isNotNull(),
                () -> assertThat(deleteHistory2).isNotNull()
        );
    }

    @DisplayName("delete_history 삭제 테스트")
    @Test
    void delete() {
        deleteHistories.delete(deleteHistory1);
        deleteHistories.delete(deleteHistory2);
        manager.flush();
        manager.clear();
        List<DeleteHistory> result = deleteHistories.findAll();
        assertThat(result).isEmpty();
    }

}