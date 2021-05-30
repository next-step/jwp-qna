package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

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

    @DisplayName("질문의 답변 목록 검색")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        userRepository.save(alice);
        Question question = new Question("title", "contents");
        questionRepository.save(question);
        Answer answer = new Answer(alice, question, "Answers Contents1");
        answerRepository.save(answer);

        List<Answer> activeAnswers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertThat(activeAnswers.size()).isEqualTo(1);
        assertThat(activeAnswers.contains(answer)).isTrue();
    }

    @DisplayName("삭제된 답변으로 변경 후 질문의 답변 목록 검색")
    @Test
    void findByQuestionIdAndDeletedFalse_AfterDeleteAnswer() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        userRepository.save(alice);
        Question question = new Question("title", "contents");
        questionRepository.save(question);
        Answer answer = new Answer(alice, question, "Answers Contents1");
        answerRepository.save(answer);

        answer.setDeleted(true);
        List<Answer> activeAnswers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertThat(activeAnswers).isEmpty();
    }

    @DisplayName("활성화된 답변 검색")
    @Test
    void findByIdAndDeletedFalse() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        userRepository.save(alice);
        Question question = new Question("title", "contents");
        questionRepository.save(question);
        Answer answer = new Answer(alice, question, "Answers Contents1");
        answerRepository.save(answer);

        Answer actual = answerRepository
            .findByIdAndDeletedFalse(answer.getId())
            .orElseThrow(NotFoundException::new);

        assertThat(actual.getId()).isEqualTo(answer.getId());
        assertThat(actual.getQuestionId()).isEqualTo(question.getId());
    }

    @DisplayName("삭제된 답변으로 변경 후 답변 검색")
    @Test
    void findByIdAndDeletedFalse_AfterDeleteAnswer() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        userRepository.save(alice);
        Question question = new Question("title", "contents");
        questionRepository.save(question);
        Answer answer = new Answer(alice, question, "Answers Contents1");
        answerRepository.save(answer);

        answer.setDeleted(true);

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() ->
            answerRepository.findByIdAndDeletedFalse(answer.getId()).get()
        );
    }

}
