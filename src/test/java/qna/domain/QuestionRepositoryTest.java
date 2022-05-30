package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {
    User user1;
    User user2;
    Question question1;
    Question question2;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(UserTest.getJavajigi());
        user2 = userRepository.save(UserTest.getSanjigi());

        question1 = questionRepository.save(new Question("title1", "contents1", user1));
        question2 = questionRepository.save(new Question("title2", "contents2", user2));
    }

    @AfterEach
    void afterEach() {
        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("질문에 답변 달기, 질문 변경")
    void addAnswer() {
        Answer answer1 = new Answer(user1, question1, "Answers Contents1");
        Answer answer = answerRepository.save(answer1);

        question2.addAnswer(answer);
        Answer answerFind = answerRepository.findById(answer.getId()).get();
        assertThat(answerFind.getQuestion().getId()).isEqualTo(question2.getId());
    }

    @Test
    @DisplayName("저장하기")
    void save() {
        Optional<Question> questionOptional = questionRepository.findById(question1.getId());

        assertThat(questionOptional.get().getId()).isEqualTo(question1.getId());
    }

    @Test
    @DisplayName("미삭제 건 전체 조회")
    void notDeleted() {
        List<Question> deletedFalse = questionRepository.findByDeletedFalse();
        assertThat(deletedFalse).contains(question1, question2);
    }

    @Test
    @DisplayName("미삭제 질문 id로 조회")
    void findByIdAndNotDeleted() {
        Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedFalse(question1.getId());
        assertThat(optionalQuestion.get().getId()).isEqualTo(question1.getId());
    }

    @Test
    @DisplayName("질문 삭제 처리 확인")
    void delete() {
        questionRepository.deleteById(question1.getId());
        assertThat(questionRepository.findByIdAndDeletedFalse(question1.getId())).isEmpty();
        questionRepository.flush();
    }
}
