package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.exception.CannotDeleteException;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;
    @Autowired
    private UserRepository users;
    @Autowired
    private QuestionRepository questions;

    @Autowired
    private DeleteHistoryRepository deleteHistories;


    private User u1;
    private Question q1;

    @BeforeEach
    void setUp() {
        u1 = new User("seungyeol", "password", "name", "beck33333@naver.com");
        users.save(u1);
        q1 = new Question("제목 이에요", "본문 입니다.").writeBy(u1);
        questions.save(q1);
    }


    @Test
    void save() {
        Answer expected =  new Answer(u1,q1,"contents");
        Answer actual = answers.save(expected);


        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getQuestion()).isEqualTo(expected.getQuestion()),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter())
        );
    }

    @Test
    void findByName() {
        Answer expected =  new Answer(u1,q1,"contents");
        answers.save(expected);
        Answer actual = answers.findById(expected.getId()).get();
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void update() {
        Answer expected = new Answer(u1,q1,"contents");
        Answer saved = answers.save(expected);

        saved.setContents("Answer Contents Changed");
        answers.flush();
    }

    @Test
    void delete() {
        Answer expected = new Answer(u1,q1,"contents");
        Answer saved = answers.save(expected);

        answers.delete(saved);
        answers.flush();
    }


    @Test
    @DisplayName("답변을 성공적으로 지우는 테스트")
    void deleteAnswerSuccess() {
        Answer expected = new Answer(u1,q1,"contents");
        expected.delete(u1);

        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, expected.getId(), u1,now());
        deleteHistories.save(deleteHistory);
    }


    @Test
    @DisplayName("답변을 지우는데 실패하는 테스트")
    void deleteAnswerFailed() {
        Answer expected = new Answer(u1,q1,"contents");
        User u2 = new User("baek", "password", "temp", "beck33@naver.com");


        assertThatThrownBy(() -> expected.delete(u2))
                .isInstanceOf(CannotDeleteException.class);

    }


}