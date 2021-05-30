package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;
import qna.exception.UnAuthenticationException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    EntityManager entityManager;

    @DisplayName("저장하기")
    @Test
    void save() {
        Question question = new Question("testTitle", "testContent");

        Question saveQuestion = questionRepository.save(question);

        Question findQuestion = questionRepository.findById(saveQuestion.getId())
                .orElseThrow(() -> new IllegalStateException());

        assertThat(saveQuestion).isEqualTo(findQuestion);
        assertThat(saveQuestion).isSameAs(findQuestion);
    }

    @DisplayName("수정하기")
    @Test
    void update() {
        Question question = new Question("testTitle", "testContent");
        Question saveQuestion = questionRepository.save(question);

        saveQuestion.setContents("testUpdateContent");
        saveQuestion.setDeleted(true);

        Question findQuestion = questionRepository.findById(question.getId()).orElseThrow(() -> new IllegalStateException());
        assertThat(findQuestion.getContents()).isEqualTo("testUpdateContent");
        assertThat(findQuestion.isDeleted()).isTrue();
    }

    @DisplayName("삭제하기")
    @Test
    void delete() {
        Question question = new Question("testTitle", "testContent");
        Question saveQuestion = questionRepository.save(question);

        questionRepository.delete(saveQuestion);

        assertThat(questionRepository.findById(saveQuestion.getId())).isEqualTo(Optional.empty());
    }

    @DisplayName("User 연관관계")
    @Test
    void user_relation() throws UnAuthenticationException {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        userRepository.save(writer);
        Question question = Question.createQuestion("testTitle", "testContent", writer);
        Question saveQuestion = questionRepository.save(question);
        entityManager.flush();
        entityManager.clear();

        User findWriter = questionRepository.findById(saveQuestion.getId())
                .orElseThrow(() -> new IllegalStateException()).getWriter();

        assertThat(findWriter.getUserId()).isEqualTo("testUserId");
    }

    @DisplayName("answers 연관관계")
    @Test
    void answers_relation() throws UnAuthenticationException {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        userRepository.save(writer);
        Question question = Question.createQuestion("testTitle", "testContent", writer);
        Question saveQuestion = questionRepository.save(question);
        Answer answer1 = Answer.createAnswer(writer, question, "testAnswer1");
        Answer answer2 = Answer.createAnswer(writer, question, "testAnswer2");
        answerRepository.save(answer1);
        answerRepository.save(answer2);
        entityManager.flush();
        entityManager.clear();

        List<Answer> findAnswers = questionRepository.findById(saveQuestion.getId())
                .orElseThrow(() -> new IllegalStateException()).getAnswers();

        assertThat(findAnswers.size()).isEqualTo(2);
    }

    @DisplayName("answers 고아객체")
    @Test
    void answers_orphanRemoval() throws UnAuthenticationException {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        userRepository.save(writer);
        Question question = Question.createQuestion("testTitle", "testContent", writer);
        Question saveQuestion = questionRepository.save(question);
        Long questionId = saveQuestion.getId();
        Answer answer1 = Answer.createAnswer(writer, question, "testAnswer1");
        Answer answer2 = Answer.createAnswer(writer, question, "testAnswer2");
        answerRepository.save(answer1);
        answerRepository.save(answer2);
        entityManager.flush();
        entityManager.clear();

        questionRepository.delete(saveQuestion);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
        assertThat(answers.size()).isEqualTo(0);
    }
}
