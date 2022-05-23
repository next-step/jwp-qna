package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private User user;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("vvsungho", "1234", "윤성호", "vvsungho@gmail.com"));
        question = new Question("질문제목", "질문내용");
        question.writeBy(user);

        question = questionRepository.save(question);
        answer = answerRepository.save(new Answer(user, question, "질문답변"));
    }

    @Test
    void save() {
        assertThat(answer.getId()).isNotNull();
    }

    @Test
    void select() {
        Answer answer2 = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();
        assertThat(answer.getId()).isEqualTo(answer2.getId());
    }
}
