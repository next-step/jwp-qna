package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.User;
import qna.fixture.TestQuestionFactory;
import qna.fixture.TestUserFactory;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

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
}
