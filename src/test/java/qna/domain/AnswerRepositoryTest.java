package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;
    @Autowired
    private QuestionRepository questions;
    @Autowired
    private UserRepository users;

    @BeforeEach
    void beforeSet() {
        User user1 = users.save(new User(1L, "hi", "hi", "you", "gg@gg.com"));
        User user2 = users.save(new User(2L, "bye", "hi", "too", "gg@gg.com"));
        Question question = questions.save(new Question("title1", "contents1").writeBy(user1));
        Answer answer1 = this.answers.save(new Answer(user1, question, "answer contents1"));
        Answer answer2 = this.answers.save(new Answer(user2, question, "answer contents2"));
        question.addAnswer(answer1);
        question.addAnswer(answer2);
    }

    @Test
    @DisplayName("댓글내용으로 조회")
    void findByContents() {
        Answer answer = answers.findByContents("answer contents1");
        assertThat(answer.getContents()).isEqualTo("answer contents1");
    }

    @Test
    @DisplayName("작성자로 answer 조회")
    void findByWriterId() {
        Answer answer = answers.findByWriter(users.findByUserId("hi").get());
        assertThat(answer).isNotNull();
        assertThat(answer.getId()).isEqualTo(1L);
    }

}
