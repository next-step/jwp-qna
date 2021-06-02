package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.exceptions.NotFoundException;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("진행 중인 질문 검색")
    @Test
    void findByDeletedFalse() {
        Question question1 = new Question("title", "contents");
        Question question2 = new Question("title2", "contents2");
        questionRepository.save(question1);
        questionRepository.save(question2);

        List<Question> activeQuestions = questionRepository.findByDeletedFalse();

        assertThat(activeQuestions.size()).isEqualTo(2);
        assertThat(activeQuestions).contains(question1, question2);
    }

    @DisplayName("삭제한 질문으로 변경 후 진행 중인 질문 검색")
    @Test
    void findByDeletedFalse_AfterDeleteQuestion() {
        Question question = new Question("title", "contents");
        questionRepository.save(question);
        question.delete();

        List<Question> activeQuestions = questionRepository.findByDeletedFalse();

        assertThat(activeQuestions).isEmpty();
    }

    @DisplayName("진행 중인 질문을 ID로 검색")
    @Test
    void findByIdAndDeletedFalse() {
        Question question = new Question("title", "contents");
        questionRepository.save(question);

        Question actual = questionRepository
            .findByIdAndDeletedFalse(question.getId())
            .orElseThrow(NotFoundException::new);

        assertThat(actual.getId()).isEqualTo(question.getId());
    }

    @DisplayName("삭제한 질문으로 변경 후 진행 중인 질문을 ID로 검색")
    @Test
    void findByIdAndDeletedFalse_AfterDeleteQuestion() {
        Question question = new Question("title", "contents");
        questionRepository.save(question);
        question.delete();

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() ->
            questionRepository.findByIdAndDeletedFalse(question.getId()).get()
        );
    }

    @DisplayName("질문 제목을 수정하기")
    @ParameterizedTest(name = "새 Title: {0}")
    @ValueSource(strings = {"New Title"})
    void update(String expected) {
        Question question = new Question("title", "contents");
        questionRepository.save(question);

        question.editTitle(expected);

        List<Question> activeQuestions = questionRepository.findByDeletedFalse();

        assertThat(activeQuestions.get(0).getTitle()).isEqualTo(expected);
    }

    @DisplayName("질문 제목을 수정한 시각을 기록하기")
    @ParameterizedTest(name = "새 Title: {0}")
    @ValueSource(strings = {"New Title"})
    void updateUpdatedAt(String expected) {
        Question question = new Question("title", "contents");
        questionRepository.save(question);

        assertThat(question.getUpdatedAt()).isEqualTo(question.getCreatedAt());

        question.editTitle(expected);
        questionRepository.flush();

        assertThat(question.getUpdatedAt()).isNotEqualTo(question.getCreatedAt());
    }

    @DisplayName("삭제하기")
    @Test
    void delete() {
        Question question = new Question("title", "contents");
        questionRepository.save(question);

        questionRepository.delete(question);

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() ->
            questionRepository.findById(question.getId()).get()
        );
    }

    @DisplayName("User의 활성화된 질문 목록 가져오기")
    @Test
    void getQuestionsAndAnswersByUser() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        userRepository.save(alice);
        Question question1 = new Question("title1", "contents1").writeBy(alice);
        Question question2 = new Question("title2", "contents2").writeBy(alice);
        questionRepository.save(question1);
        questionRepository.save(question2);

        User searchedUser = userRepository
            .findById(alice.getId())
            .orElseThrow(NotFoundException::new);
        List<Question> activeQuestions = searchedUser.getQuestions(Status.PUBLISHED);

        assertThat(activeQuestions.size()).isEqualTo(2);
        assertThat(activeQuestions).contains(question1, question2);
    }

}
