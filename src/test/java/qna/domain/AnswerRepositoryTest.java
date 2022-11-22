package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class AnswerRepositoryTest {

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

    @BeforeEach
    void setUp() {
        user = users.save(new User("testUser", "qwerty1234", "김철수", "testUser@nextstep.com"));
        question = questions.save(new Question("testUser question", "질문내용123").writeBy(user));
        answer = answers.save(new Answer(user, question, "답변123"));
    }

    @DisplayName("answer 저장 확인")
    @Test
    void save() {
        User user2 = new User("testUser2", "qwerty12345", "김영희", "testUser2@nextstep.com");
        Question question2 = new Question("testUser2 question", "질문내용456").writeBy(user);
        String answerContents = "답변456";
        final Answer answer2 = answers.save(new Answer(user2, question2, answerContents));
        assertAll(
                () -> assertThat(answer2.getId()).isNotNull(),
                () -> assertThat(answer2.getContents()).isEqualTo(answerContents)
        );
    }

    @DisplayName("answer 테이블 question id로 deleted = false인 row select 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        final List<Answer> result = answers.findByQuestionAndDeletedFalse(question);
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(result.contains(answer)).isTrue()
        );
    }

    @DisplayName("answer 테이블 id로 deleted = false인 row select 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        final Answer actual = answers.findByIdAndDeletedFalse(answer.getId()).get();
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.isDeleted()).isFalse()
        );
    }

    @DisplayName("answer 테이블 contents 수정 테스트")
    @Test
    void update_contents() {
        String expected = "답변 수정";
        answer.setContents(expected);
        final Answer actual = answers.findByIdAndDeletedFalse(answer.getId()).get();
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected)
        );
    }

    @DisplayName("answer 테이블 deleted 수정 테스트")
    @Test
    void update_deleted() {
        boolean expected = true;
        answer.setDeleted(expected);
        flushAndClear();
        final Answer actual = answers.findById(answer.getId()).get();
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(actual.isDeleted()).isEqualTo(expected)
        );
    }

    @DisplayName("answer 삭제 테스트")
    @Test
    void delete() {
        answers.delete(answer);
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answers.findByIdAndDeletedFalse(answer.getId())).isEmpty()
        );
    }

    private void flushAndClear() {
        manager.flush();
        manager.clear();
    }

}