package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class AnswerRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("답변ID 와 삭제여부로 조회")
    void findByIdAndDeletedFalse() {
        //Given
        User user1 = new User("test1", "1234", "홍길동", "hong@test.com");
        User user2 = new User("test2", "1234", "정훈희", "jhh992000@gmail.com");

        userRepository.save(user1);
        userRepository.save(user2);

        Question question = new Question("질문있어요", "내용입니다.", user2);
        questionRepository.save(question);

        Answer answer = new Answer(user1, question, "답변입니다.");
        answerRepository.save(answer);

        entityManager.flush();
        entityManager.clear();

        //When
        Answer actual = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

        //Then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(answer.getContents())
        );
    }

    @Test
    @DisplayName("질문ID 와 삭제여부로 조회")
    void findByQuestionIdAndDeletedFalse() {
        //Given
        User user1 = new User("test1", "1234", "홍길동", "hong@test.com");
        User user2 = new User("test2", "1234", "정훈희", "jhh992000@gmail.com");

        userRepository.save(user1);
        userRepository.save(user2);

        Question question = new Question("질문있어요", "내용입니다.", user2);
        questionRepository.save(question);

        Answer answer1 = new Answer(user1, question, "답변입니다.");
        Answer answer2 = new Answer(user2, question, "답변입니다.");
        answerRepository.save(answer1);
        answerRepository.save(answer2);

        entityManager.flush();
        entityManager.clear();

        //When
        List<Answer> savedAnswers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        //Then
        assertThat(savedAnswers).hasSize(2);
    }

    @Test
    @DisplayName("writer 연관관계")
    void relation_writer() {
        //Given
        User user1 = new User("test1", "1234", "홍길동", "hong@test.com");
        User user2 = new User("test2", "1234", "정훈희", "jhh992000@gmail.com");

        userRepository.save(user1);
        userRepository.save(user2);

        Question question = new Question("질문있어요", "내용입니다.", user2);
        questionRepository.save(question);

        Answer answer = new Answer(user1, question, "답변입니다.");
        answerRepository.save(answer);

        entityManager.flush();
        entityManager.clear();

        //When
        Answer savedAnswer = answerRepository.findById(answer.getId()).get();
        User writer = savedAnswer.getWriter();

        //Then
        assertAll(
            () -> assertThat(writer).isNotNull(),
            () -> assertThat(writer).isEqualTo(user1)
        );
    }

    @Test
    @DisplayName("question 연관관계")
    void relation_question() {
        //Given
        User user1 = new User("test1", "1234", "홍길동", "hong@test.com");
        User user2 = new User("test2", "1234", "정훈희", "jhh992000@gmail.com");

        userRepository.save(user1);
        userRepository.save(user2);

        Question question = new Question("질문있어요", "내용입니다.", user2);
        questionRepository.save(question);

        Answer answer = new Answer(user1, question, "답변입니다.");
        answerRepository.save(answer);

        entityManager.flush();
        entityManager.clear();

        //When
        Question savedQuestion = questionRepository.findById(question.getId()).get();
        List<Answer> answers = savedQuestion.getAnswers();

        //Then
        assertAll(
            () -> assertThat(answers).isNotNull(),
            () -> assertThat(answers).hasSize(1)
        );
    }

}
