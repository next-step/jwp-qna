package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @Test
    @DisplayName("질문 저장")
    void save() {
        // given
        Question question = new Question("질문테스트", "질문테스트내용");

        // when
        Question actual = questions.save(question);

        // then
        assertThat(actual).isSameAs(question);
    }

    @Test
    @DisplayName("질문과 작성자 연결")
    void saveWithWriter() {
        // given
        User javaJigi = users.save(UserTest.JAVAJIGI);
        Question question = new Question("질문테스트", "질문테스트내용");
        question.writeBy(javaJigi);

        // when
        Question actual = questions.save(question);

        // then
        assertThat(actual.writer()).isSameAs(javaJigi);
    }
}