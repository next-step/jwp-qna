package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answers;
    @Autowired
    UserRepository users;
    @Autowired
    QuestionRepository questions;

    private User user;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        user = users.save(new User("lcjltj","password", "chanjun", "lcjltj@gmail.com"));
        question = questions.save(new Question("title1", "contents1").writeBy(user));
        answer = new Answer(user, question, "Answers Contents");
    }

    @Test
    void save() {
        answers.save(answer);
        List<Answer> answersAll = answers.findAll();
        assertThat(answersAll).hasSize(1);
    }

    @Test
    void findById() {
        answers.save(answer);
        Answer expected = answers.findById(answer.getId()).get();

        assertThat(expected).isSameAs(answer);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        answers.save(answer);
        List<Answer> list = answers.findByQuestionIdAndDeletedFalse(answer.getQuestion().getId());
        assertThat(list.get(0)).isSameAs(answer);
    }

    @Test
    void findByIdAndDeletedFalse() {
        Answer answer = answers.save(this.answer);
        Optional<Answer> expected = answers.findByIdAndDeletedFalse(answer.getId());
        assertThat(expected.get()).isSameAs(answer);
    }
}