package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.exception.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;
    @Autowired
    private UserRepository users;
    @Autowired
    private DeleteHistoryRepository deleteHistories;
    @Autowired
    private AnswerRepository answers;

    private User u1;

    @BeforeEach
    void setUp() {
        u1 = new User("seungyeol", "password", "name", "beck33333@naver.com");
        users.save(u1);
    }

    @AfterEach
    void deleteAll() {
        questions.deleteAll();
        users.deleteAll();
    }

    @Test
    void save() {
        Question expected = new Question("제목 이에요", "본문 입니다.").writeBy(u1);
        Question actual = questions.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isNotNull(),
                () -> assertThat(actual.isDeleted()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter())
        );
    }

    @Test
    @DisplayName("질문을 성공적으로 지우는 테스트")
    void deleteAnswerSuccess() {
        Question question = new Question("제목 이에요", "본문 입니다.").writeBy(u1);
        Answer answer = new Answer(u1, question, "contents");
        answers.save(answer);
        questions.save(question);

        DeleteHistories deleteHistories = new DeleteHistories();
        question.delete(u1, deleteHistories);

        assertThat(deleteHistories.getDeletedHistories().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("질문을 지우는데 실패하는 테스트")
    void deleteAnswerFailed() {
        Question question = new Question("제목 이에요", "본문 입니다.").writeBy(u1);
        Answer answer = new Answer(u1, question, "contents");
        answers.save(answer);
        questions.save(question);

        User u2 = new User("baek", "password", "temp", "beck33@naver.com");

        DeleteHistories deleteHistories = new DeleteHistories();

        assertThatThrownBy(() -> question.delete(u2, deleteHistories))
                .isInstanceOf(CannotDeleteException.class);

    }

    @Test
    void findByName() {
        Question expected = new Question("제목 이에요", "본문 입니다.").writeBy(u1);
        questions.save(expected);
        Question actual = questions.findById(expected.getId()).get();
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void update() {
        Question expected = new Question("제목 이에요", "본문 입니다.").writeBy(u1);
        Question saved = questions.save(expected);

        saved.setContents("Question Contents Changed");
        questions.flush();
    }

    @Test
    void delete() {
        Question expected = new Question("제목 이에요", "본문 입니다.").writeBy(u1);
        Question saved = questions.save(expected);

        questions.delete(saved);
        questions.flush();
    }


}