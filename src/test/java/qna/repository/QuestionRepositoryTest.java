package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.*;
import qna.fixture.TestAnswerFactory;
import qna.fixture.TestQuestionFactory;
import qna.fixture.TestUserFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(showSql = false)
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("질문을 저장할 수 있다")
    @Test
    void save() {
        User writer = userRepository.save(TestUserFactory.create("서정국"));
        Question expect = TestQuestionFactory.create(writer);

        Question result = questionRepository.save(expect);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.isDeleted()).isEqualTo(expect.isDeleted()),
                () -> assertThat(result.getWriter()).isEqualTo(expect.getWriter()),
                () -> assertThat(result.getContents()).isEqualTo(expect.getContents()),
                () -> assertThat(result.getTitle()).isEqualTo(expect.getTitle())
        );
    }

    @DisplayName("전체 질문을 조회할 수 있다")
    @Test
    void findAll() {
        User writer = userRepository.save(TestUserFactory.create("서정국"));
        Question question1 = questionRepository.save(TestQuestionFactory.create(writer));
        Question question2 = questionRepository.save(TestQuestionFactory.create(writer));

        List<Question> results = questionRepository.findByDeletedFalse();
        assertThat(results).contains(question1, question2);
    }

    @DisplayName("삭제된 질문은 findByDeletedFalse 함수로 검색되지 않는다")
    @Test
    void deletedFindAll() {
        User writer = userRepository.save(TestUserFactory.create("서정국"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        question.setDeleted(true);

        assertThat(questionRepository.findByDeletedFalse()).hasSize(0);
    }

    @DisplayName("id로 조회할 수 있다")
    @Test
    void findById() {
        User writer = userRepository.save(TestUserFactory.create("서정국"));
        Question expect = questionRepository.save(TestQuestionFactory.create(writer));

        Question result = questionRepository.findByIdAndDeletedFalse(expect.getId()).get();

        assertEquals(expect, result);
    }

    @DisplayName("삭제된 질문은 findByIdAndDeletedFalse 함수로 조회할 수 없다")
    @Test
    void findDeletedById() {
        User writer = userRepository.save(TestUserFactory.create("서정국"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        question.setDeleted(true);

        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(question.getId());

        assertThat(result).isNotPresent();
    }

    @DisplayName("질문자가 아닌 사람이 질문을 삭제하면 예외가 발생한다")
    @Test
    void noOwnerDeleteException() {
        User writer = userRepository.save(TestUserFactory.create("서정국"));
        User hacker = userRepository.save(TestUserFactory.create("나쁜놈"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));

        assertThatThrownBy(() -> question.delete(hacker))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @DisplayName("답변이 없는 경우 질문을 삭제할 수 있다")
    @Test
    void delete() throws CannotDeleteException {
        User writer = userRepository.save(TestUserFactory.create("서정국"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));

        question.delete(writer);

        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isNotPresent();
    }

    @DisplayName("질문을 삭제하면 답변도 같이 삭제된다")
    @Test
    void deleteWithAnswers() throws CannotDeleteException {
        User writer = userRepository.save(TestUserFactory.create("서정국"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer answer = answerRepository.save(TestAnswerFactory.create(writer, question));

        question.delete(writer);

        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).isNotPresent();
    }

    @DisplayName("질문자와 답변자가 다르면 삭제할 수 없다")
    @Test
    void differentUserDelete() {
        User questionWriter = userRepository.save(TestUserFactory.create("질문자"));
        User answerWriter = userRepository.save(TestUserFactory.create("답변자"));
        Question question = questionRepository.save(TestQuestionFactory.create(questionWriter));
        answerRepository.save(TestAnswerFactory.create(answerWriter, question));

        assertThatThrownBy(() -> question.delete(questionWriter))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("한명의 답변자라도 질문자와 다르면 삭제할 수 없다")
    @Test
    void differentOneUserDelete() {
        User questionWriter = userRepository.save(TestUserFactory.create("질문자"));
        User answerWriter = userRepository.save(TestUserFactory.create("답변자"));
        Question question = questionRepository.save(TestQuestionFactory.create(questionWriter));
        answerRepository.save(TestAnswerFactory.create(questionWriter, question));
        answerRepository.save(TestAnswerFactory.create(answerWriter, question));

        assertThatThrownBy(() -> question.delete(questionWriter))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("질문이 삭제되면, 질문과 답변 모두에 대한 삭제이력이 반환된다")
    @Test
    void deleteHistory() throws CannotDeleteException {
        User writer = userRepository.save(TestUserFactory.create("서정국"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer answer = answerRepository.save(TestAnswerFactory.create(writer, question));

        DeleteHistories deleteHistories = question.delete(writer);

        List<Long> results = deleteHistories.get().stream()
                .map(DeleteHistory::getContentId)
                .collect(Collectors.toList());
        assertThat(results).contains(question.getId(), answer.getId());
    }
}
