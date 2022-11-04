package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Test
    @DisplayName("다대일(Answer:Question) 연관관계 설정 후 Answer 생성 테스트")
    void createWithQuestion() {
        Question question = new Question("title", "contents");
        questionRepository.save(question);

        User writer = new User("user1", "password", "name", "test@email.com");
        userRepository.save(writer);

        Answer answer = new Answer(writer, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);

        Assertions.assertThat(savedAnswer.getId()).isNotNull();
    }

    @Transactional
    @Test
    @DisplayName("저장한 Answer 와 조회한 Answer 가 같은지 (동일성)확인")
    void read() {
        Question question = new Question("title", "contents");
        questionRepository.save(question);

        User writer = new User("user1", "password", "name", "test@email.com");
        userRepository.save(writer);

        Answer answer = new Answer(writer, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);

        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());

        assertAll(
                () -> assertThat(findAnswer).isPresent(),
                () -> assertThat(savedAnswer).isSameAs(findAnswer.get())
        );
    }

    @Transactional
    @Test
    @DisplayName("저장한 Answer 의 Question 변경 후 조회한 Answer 의 Question 과 같은지 확인")
    void updateWithQuestion() {
        Question question = new Question("title", "contents");
        questionRepository.save(question);

        User writer = new User("user1", "password", "name", "test@email.com");
        userRepository.save(writer);

        Answer answer = new Answer(writer, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);

        savedAnswer.toQuestion(new Question("new title", "new contents"));

        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());

        Assertions.assertThat(savedAnswer.getQuestion()).isEqualTo(findAnswer.get().getQuestion());
    }

    @Transactional
    @Test
    @DisplayName("저장한 Answer 삭제 후 조회 시 Answer 가 없는지 확인")
    void delete() {
        Question question = new Question("title", "contents");
        questionRepository.save(question);

        User writer = new User("user1", "password", "name", "test@email.com");
        userRepository.save(writer);

        Answer answer = new Answer(writer, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);

        answerRepository.delete(savedAnswer);

        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());

        Assertions.assertThat(findAnswer).isNotPresent();
    }

    @Transactional
    @Test
    @DisplayName("Answer 의 deleted 가 false 일 때 findByIdAndDeletedFalse 로 조회된 Answer 가 있는지 확인")
    void findByIdAndDeletedFalse() {
        Question question = new Question("title", "contents");
        questionRepository.save(question);

        User writer = new User("user1", "password", "name", "test@email.com");
        userRepository.save(writer);

        Answer answer = new Answer(writer, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        Assertions.assertThat(savedAnswer.getId()).isEqualTo(findAnswer.get().getId());
    }

    @Transactional
    @Test
    @DisplayName("Answer 의 deleted 가 true 일 때 findByIdAndDeletedFalse 조회 시 empty 로 조회되는지 확인")
    void findByIdAndDeletedFalse2() {
        Question question = new Question("title", "contents");
        questionRepository.save(question);

        User writer = new User("user1", "password", "name", "test@email.com");
        userRepository.save(writer);

        Answer answer = new Answer(writer, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);
        savedAnswer.deleted();

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        Assertions.assertThat(findAnswer).isNotPresent();
    }

    @Transactional
    @Test
    @DisplayName("question id 로 Answer 조회 시 Answer 의 deleted 가 false 인 것만 조회되는지 확인 (모두 false 였을 때)")
    void findByQuestionAndDeletedFalse() {
        Question question = new Question("title", "contents");
        questionRepository.save(question);

        User writer1 = new User("user1", "password", "name", "test@email.com");
        userRepository.save(writer1);
        User writer2 = new User("user2", "password", "name", "test@email.com");
        userRepository.save(writer2);

        Answer savedAnswer1 = answerRepository.save(new Answer(writer1, question, "contents"));
        Answer savedAnswer2 = answerRepository.save(new Answer(writer2, question, "contents"));

        List<Answer> findAnswers = answerRepository.findByQuestionAndDeletedFalse(question);

        assertAll(
                () -> assertThat(findAnswers).hasSize(2),
                () -> assertThat(findAnswers).containsExactlyInAnyOrder(savedAnswer1, savedAnswer2)
        );
    }

    @Transactional
    @Test
    @DisplayName("question id 로 Answer 조회 시 Answer 의 deleted 가 false 인 것만 조회되는지 확인 (각각 true, false 였을 때)")
    void findByQuestionAndDeletedFalse2() {
        Question question = new Question("title", "contents");
        questionRepository.save(question);

        User writer1 = new User("user1", "password", "name", "test@email.com");
        userRepository.save(writer1);
        User writer2 = new User("user2", "password", "name", "test@email.com");
        userRepository.save(writer2);

        Answer savedAnswer1 = answerRepository.save(new Answer(writer1, question, "contents"));
        Answer savedAnswer2 = answerRepository.save(new Answer(writer2, question, "contents"));
        savedAnswer1.deleted();

        List<Answer> findAnswers = answerRepository.findByQuestionAndDeletedFalse(question);

        assertAll(
                () -> assertThat(findAnswers).hasSize(1),
                () -> assertThat(findAnswers).containsExactlyInAnyOrder(savedAnswer2)
        );
    }
}
