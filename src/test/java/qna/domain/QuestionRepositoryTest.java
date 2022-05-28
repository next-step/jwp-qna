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

    User javaJigi = UserTest.getJavajigi();
    User sanJigi = UserTest.getSanjigi();

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void afterEach() {
        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("질문에 답변 달기")
    void addAnswer() {
        User user1 = userRepository.save(javaJigi);
        User user2 = userRepository.save(sanJigi);

        Question question1 = questionRepository.save(QuestionTest.getQuestion1(user1));
        Answer answer1 = AnswerTest.getAnswer1(user1, question1);

        Answer answer = answerRepository.save(answer1);

        Question question2 = QuestionTest.getQuestion2(user2);
        Question question = questionRepository.save(question2);

        question.addAnswer(answer);
        Answer answerFind = answerRepository.findById(answer.getId()).get();
        assertThat(answerFind.getQuestion().getId()).isEqualTo(question.getId());
    }

    @Test
    @DisplayName("저장하기")
    void save() {
        User user = userRepository.save(javaJigi);
        Question q1 = QuestionTest.getQuestion1(user);

        Question question = questionRepository.save(q1);
        Optional<Question> questionOptional = questionRepository.findById(question.getId());

        assertThat(questionOptional.get().getId()).isEqualTo(question.getId());
    }

    @Test
    @DisplayName("미삭제 건 전체 조회")
    void notDeleted() {
        User user1 = userRepository.save(javaJigi);
        User user2 = userRepository.save(sanJigi);

        Question question1 = QuestionTest.getQuestion1(user1);
        Question question2 = QuestionTest.getQuestion2(user2);

        Question saveQuestion1 = questionRepository.save(question1);
        Question saveQuestion2 = questionRepository.save(question2);
        List<Question> deletedFalse = questionRepository.findByDeletedFalse();
        assertThat(deletedFalse).contains(saveQuestion1, saveQuestion2);
    }

    @Test
    @DisplayName("미삭제 질문 id로 조회")
    void findByIdAndNotDeleted() {
        User user = userRepository.save(javaJigi);
        Question question1 = QuestionTest.getQuestion1(user);

        Question saveQuestion1 = questionRepository.save(question1);

        Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion1.getId());
        assertThat(optionalQuestion.get().getId()).isEqualTo(saveQuestion1.getId());
    }
}
