package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.exceptions.NotFoundException;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("질문의 활성화된 답변 목록 검색")
    @Test
    void findByQuestionAndDeletedFalse() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        Question question = new Question("title", "contents");
        Answer answer = new Answer(alice, question, "Answers Contents");
        userRepository.save(alice);
        questionRepository.save(question);

        entityManager.clear();

        Question actualQuestion = questionRepository
            .findById(question.getId())
            .orElseThrow(NotFoundException::new);
        List<Answer> activeAnswers = actualQuestion.getAnswers();

        assertThat(activeAnswers.size()).isEqualTo(1);
        assertThat(activeAnswers.get(0).getId()).isEqualTo(answer.getId());
    }

    @DisplayName("질문 ID로 활성화된 답변 목록 검색")
    @Test
    void findByQuestion() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        Question question = new Question("title", "contents");
        Answer answer = new Answer(alice, question, "Answers Contents");
        userRepository.save(alice);
        questionRepository.save(question);

        List<Answer> activeAnswers = answerRepository.findByQuestion(question);

        assertThat(activeAnswers.size()).isEqualTo(1);
        assertThat(activeAnswers).contains(answer);
    }

    @DisplayName("삭제된 답변으로 변경 후 질문의 활성화된 답변 목록 검색")
    @Test
    void findByQuestion_AfterDeleteAnswer() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        Question question = new Question("title", "contents");
        Answer answer = new Answer(alice, question, "Answers Contents");
        userRepository.save(alice);
        questionRepository.save(question);

        answer.delete(alice);
        answerRepository.flush();
        entityManager.clear();

        Question searchedQuestion = questionRepository
            .findById(question.getId())
            .orElseThrow(NotFoundException::new);
        List<Answer> activeAnswers = searchedQuestion.getAnswers();

        assertThat(activeAnswers).isEmpty();
    }

    @DisplayName("삭제된 답변으로 변경 후 질문 ID로 활성화된 답변 목록 검색")
    @Test
    void findByQuestionIdAndDeletedFalse_AfterDeleteAnswer() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        Question question = new Question("title", "contents");
        Answer answer = new Answer(alice, question, "Answers Contents1");
        userRepository.save(alice);
        questionRepository.save(question);

        answer.delete(alice);
        List<Answer> activeAnswers = answerRepository.findByQuestion(question);

        assertThat(activeAnswers).isEmpty();
    }

    @DisplayName("답변의 ID로 활성화된 답변 검색")
    @Test
    void findById() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        Question question = new Question("title", "contents");
        Answer answer = new Answer(alice, question, "Answers Contents1");
        userRepository.save(alice);
        questionRepository.save(question);

        Answer actual = answerRepository
            .findById(answer.getId())
            .orElseThrow(NotFoundException::new);

        assertThat(actual.getId()).isEqualTo(answer.getId());
        assertThat(actual.getQuestion()).isEqualTo(question);
    }

    @DisplayName("삭제된 답변으로 변경 후 답변의 ID로 활성화된 답변 검색")
    @Test
    void findById_AfterDeleteAnswer() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        Question question = new Question("title", "contents");
        Answer answer = new Answer(alice, question, "Answers Contents1");
        userRepository.save(alice);
        questionRepository.save(question);

        answer.delete(alice);
        answerRepository.flush();
        entityManager.clear();

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() ->
            answerRepository.findById(answer.getId()).get()
        );
    }

}
