package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;
    @Autowired
    private UserRepository users;

    private Question q1;
    private User u1;
    @BeforeEach
    void setUp() {
        q1 = new Question("제목 이에요", "본문 입니다.");
        u1 = new User("seungyeol", "password", "name", "beck33333@naver.com");
    }

    @AfterEach
    void deleteAll() {
        questions.deleteAll();
        users.deleteAll();
    }

    @Test
    void save() {
        Question expected = q1;
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
    void saveWithWriter() {
        Question expected = q1;
        q1.setWriter(users.save(u1));

        Question actual = questions.save(expected);
        assertThat(actual.getWriter()).isEqualTo(expected.getWriter());
    }

    @Test
    void updateWithWriter() {
        Question given = q1;
        questions.save(given);

        given.setWriter(users.save(u1));
        questions.flush();
        assertThat(given.getWriter().getUserId()).isEqualTo("seungyeol");
    }


    @Test
    void findByName() {
        Question expected = q1;
        questions.save(expected);
        Question actual = questions.findById(expected.getId()).get();
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void update() {
        Question expected = q1;
        Question saved = questions.save(expected);

        saved.setContents("Question Contents Changed");
        questions.flush();
    }

    @Test
    void delete() {
        Question expected = q1;
        Question saved = questions.save(expected);

        questions.delete(saved);
        questions.flush();
    }


}