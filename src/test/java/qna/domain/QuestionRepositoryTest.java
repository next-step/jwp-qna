package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {
    private Question question;
    private User user;
    private Answer answer;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("vvsungho", "1234", "윤성호", "vvsungho@gmail.com"));
        question = new Question("질문제목", "질문내용");
        question.writeBy(user);

        answer = new Answer(user, question, "질문답변");
        question.addAnswer(answer);
        question = questionRepository.save(question);

        question = questionRepository.save(question);
    }

    @Test
    void save() {
        assertThat(question).isNotNull();
    }

    @Test
    void select() {
        Question question2 = questionRepository.findByTitle("질문제목").get();
        assertThat(question).isSameAs(question2);
    }

    @Test
    void saveWithAnswer() {
        Answer answer2 = answerRepository.findByQuestionIdAndDeletedFalse(question.getId()).stream().findFirst().get();
        assertThat(answer2.getId()).isNotNull();
    }
}
