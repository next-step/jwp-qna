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
class QuestionRepositoryTest {

    @Autowired
    UserRepository users;

    @Autowired
    QuestionRepository questions;

    @Autowired
    TestEntityManager manager;

    User user;
    Question question;

    @BeforeEach
    void setUp() {
        user = users.save(new User("testUser", "qwerty1234", "김철수", "testUser@nextstep.com"));
        question = questions.save(new Question("testUser question", "질문내용123").writeBy(user));
    }

    @DisplayName("question 저장 확인")
    @Test
    void save() {
        User user2 = new User("testUser2", "qwerty12345", "김영희", "testUser2@nextstep.com");
        String questionContents = "질문내용456";
        final Question question2 = questions.save(new Question("testUser2 question", questionContents).writeBy(user2));
        assertAll(
                () -> assertThat(question2.getId()).isNotNull(),
                () -> assertThat(question2.getContents()).isEqualTo(questionContents)
        );
    }

    @DisplayName("question 테이블 deleted = false인 row select 테스트")
    @Test
    void findByDeletedFalse() {
        final List<Question> result = questions.findByDeletedFalse();
        assertThat(result.contains(question)).isTrue();
    }

    @DisplayName("question 테이블 id로 deleted = false인 row select 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        final Question actual = questions.findByIdAndDeletedFalse(question.getId()).get();
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.isDeleted()).isFalse()
        );
    }

    @DisplayName("question 조회 후 매핑된 user 정보확인")
    @Test
    void findByIdWithUser() {
        manager.clear();
        final Question actual = questions.findByIdAndDeletedFalse(question.getId()).get();
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getWriter()).isNotNull(),
                () -> assertThat(actual.getWriter().getUserId()).isEqualTo(user.getUserId())
        );
    }

    @DisplayName("question 테이블 title 수정 테스트")
    @Test
    void update_title() {
        String expected = "제목 수정";
        question.setTitle(expected);
        final Question actual = questions.findByIdAndDeletedFalse(question.getId()).get();
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(expected)
        );
    }

    @DisplayName("question 테이블 contents 수정 테스트")
    @Test
    void update_contents() {
        String expected = "질문내용 수정";
        question.setContents(expected);
        final Question actual = questions.findByIdAndDeletedFalse(question.getId()).get();
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected)
        );
    }

    @DisplayName("question 테이블 deleted 수정 테스트")
    @Test
    void update_deleted() {
        boolean expected = true;
        question.setDeleted(expected);
        flushAndClear();
        final Question actual = questions.findById(question.getId()).get();
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(actual.isDeleted()).isEqualTo(expected)
        );
    }

    @DisplayName("question 삭제 테스트")
    @Test
    void delete() {
        questions.delete(question);
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(questions.findByIdAndDeletedFalse(question.getId())).isEmpty()
        );
    }

    private void flushAndClear() {
        manager.flush();
        manager.clear();
    }

}