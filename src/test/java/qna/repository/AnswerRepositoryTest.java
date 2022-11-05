package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;
import qna.fixture.TestAnswerFactory;
import qna.fixture.TestQuestionFactory;
import qna.fixture.TestUserFactory;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void clear() {
        answerRepository.deleteAll();
        userRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @DisplayName("답변을 저장할 수 있다")
    @Test
    void save() {
        User writer = userRepository.save(TestUserFactory.create("writer"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer expect = TestAnswerFactory.create(writer, question);

        Answer actual = answerRepository.save(expect);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.isDeleted()).isEqualTo(expect.isDeleted()),
                () -> assertThat(actual.getContents()).isEqualTo(expect.getContents()),
                () -> assertThat(actual.getQuestion()).isEqualTo(expect.getQuestion()),
                () -> assertThat(actual.getWriter()).isEqualTo(expect.getWriter())
        );
    }

    @DisplayName("id로 조회할 수 있다")
    @Test
    void findById() {
        User writer = userRepository.save(TestUserFactory.create("writer"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer expect = answerRepository.save(TestAnswerFactory.create(writer, question));

        Answer result = answerRepository.findByIdAndDeletedFalse(expect.getId()).get();

        assertEquals(expect, result);
    }

    @DisplayName("답변이 삭제되었을 경우, findByIdAndDeletedFalse 함수로 조회할 수 없다")
    @Test
    void findDeletedById() {
        User writer = userRepository.save(TestUserFactory.create("writer"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer answer = answerRepository.save(TestAnswerFactory.create(writer, question));
        answer.setDeleted(true);

        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answer.getId());

        assertThat(result).isNotPresent();
    }

    @DisplayName("질문의 id로 조회할 수 있다")
    @Test
    void findByQuestionId() {
        User writer = userRepository.save(TestUserFactory.create("writer"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer expect = answerRepository.save(TestAnswerFactory.create(writer, question));

        Answer result = answerRepository.findByQuestionIdAndDeletedFalse(expect.getQuestion().getId()).get(0);

        assertEquals(expect, result);
    }

    @DisplayName("답변이 삭제되었을 경우, findByQuestionIdAndDeletedFalse 함수로 조회할 수 없다")
    @Test
    void findDeletedByQuestionId() {
        User writer = userRepository.save(TestUserFactory.create("writer"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer answer = answerRepository.save(TestAnswerFactory.create(writer, question));
        answer.setDeleted(true);

        List<Answer> result = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestion().getId());

        assertThat(result).hasSize(0);
    }
}
