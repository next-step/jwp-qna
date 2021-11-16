package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    Answer savedAnswer;
    @Autowired
    EntityManager em;
    Answer answer1;
    User user1;
    User user2;
    Question question1;

    @BeforeEach
    void init() {
        user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
        user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        question1 = new Question("title1", "contents1").writeBy(user1);
        answer1 = new Answer(user1, question1, "Answers Contents1");
        savedAnswer = answerRepository.save(answer1);
    }

    @Test
    void 저장() {
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(answer1.getContents())
        );
    }

    @Test
    void 검색() {
        assertThat(answerRepository.findById(savedAnswer.getId()).get().getId())
                .isEqualTo(savedAnswer.getId());
    }

    @Test
    void 검색_없을경우() {
        answer1.setDeleted(true);
        userRepository.save(user1);
        questionRepository.save(question1);
        assertThat(answerRepository.findByIdAndDeletedFalse(answer1.getId()).isPresent()).isFalse();
    }

    @Test
    void 수정() {
        User savedUser2 = userRepository.save(user2);
        savedAnswer.setWriterId(user2.getId());
        answerRepository.flush();
        em.clear();
        savedUser2 = userRepository.findById(savedUser2.getId()).get();
        Answer foundAnswer = answerRepository.findById(savedAnswer.getId()).get();
        assertThat(savedUser2.getId()).isEqualTo(foundAnswer.getWriterId());
    }

    @Test
    void 삭제() {
        answerRepository.delete(savedAnswer);
        assertThat(answerRepository.findById(savedAnswer.getId())).isEmpty();
    }
}
