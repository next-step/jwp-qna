package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {
    private User user;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setup() {
        user = userRepository.save(new User(1L, "user1", "password", "name", "user1@slipp.net"));
    }

    @Test
    @DisplayName("Question Entity Create 및 ID 생성 테스트")
    void save() {
        final Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        assertThat(question.getId()).isNotNull();
    }

    @Test
    @DisplayName("Question Entity Read 테스트")
    void findById() {
        final Question saved = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        final Question found = questionRepository.findById(saved.getId()).orElseGet(()->null);
        assertThat(found).isEqualTo(saved);
    }

    @Test
    @DisplayName("Question Entity Update 테스트")
    void update() {
        final Question saved = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        saved.setContents("updated!");
        final Question found = questionRepository.findById(saved.getId()).orElseThrow(() -> new RuntimeException("테스트실패"));
        assertThat(found.getContents()).isEqualTo("updated!");
    }

    @Test
    @DisplayName("Question Entity Delete 테스트")
    void delete() {
        final Question saved = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        questionRepository.delete(saved);
        questionRepository.flush();
        final Question found = questionRepository.findById(saved.getId()).orElseGet(() -> null);
        assertThat(found).isNull();
    }

    @Test
    @DisplayName("Question Entity 연관관계매핑 객체 업데이트 테스트")
    void updateAnswers() {
        Question question1 = questionRepository.save(new Question("t1","c1").writeBy(user));
        Answer answer1 = answerRepository.save(new Answer(user, question1, "answer c1"));
        Answer answer2 = answerRepository.save(new Answer(user, question1, "answer c2"));
        Answer answer3 = answerRepository.save(new Answer(user, question1, "answer c3"));

        Question actual = questionRepository.findById(question1.getId()).orElseThrow(() -> new RuntimeException("테스트실패"));
        assertThat(actual.getAnswers()).contains(answer1, answer2, answer3);
    }
}
