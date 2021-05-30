package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    @Test
    @DisplayName("답변저장")
    void save() {
        // given
        User user1 = new User("USER1", "123456", "LDS", "lds@test.com");
        Question question = new Question("질문테스트", "질문테스트내용");

        // when
        Answer answer = new Answer(user1, question, "답변테스트내용");
        Answer actual = answers.save(answer);

        // then
        assertThat(actual).isSameAs(answer);
    }

    @Test
    @DisplayName("답변저장 - 작성자 매핑")
    void saveWithWriterAndQuestion() {
        // given
        User sangJigi = users.save(UserTest.SANJIGI);
        Question question1 = questions.save(QuestionTest.Q1);

        // when
        Answer answer = new Answer(sangJigi, question1, "답변테스트");
        Answer actual = answers.save(answer);

        // then
        assertThat(actual.writer()).isSameAs(sangJigi);
    }
}