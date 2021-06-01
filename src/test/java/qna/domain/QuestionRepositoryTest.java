package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q2;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    @Test
    public void save_테스트() {

        User writer = new User("javajigi", "password", "name", "javajigi@slipp.net");
        users.save(writer);

        Question question = new Question("title1", "contents1").writeBy(writer);
        Question actual = questions.save(question);
        assertThat(actual.getTitle()).isEqualTo("title1");

/*        User expected = users.save(SANJIGI);
        Question actual = questions.save(Q2);
        assertThat(actual.getWriterId()).isEqualTo(expected.getId());*/
    }

    @Test
    public void update_deleted_테스트() {
        User user_saved = users.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        Question question_saved = questions.save(new Question("title1", "contents1").writeBy(user_saved));

        Question expected = questions.findById(question_saved.getId()).get();
        expected.setDeleted(true);
        Question actual = questions.save(expected);

        assertThat(actual.isDeleted()).isTrue();
    }
}