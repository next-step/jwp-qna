package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repository.entity.Question;
import qna.repository.entity.User;
import qna.repository.entity.UserTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static qna.repository.entity.QuestionTest.Q1;
import static qna.repository.entity.QuestionTest.Q2;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private Long questionId;

    @BeforeEach
    void setup() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        User sanjigi = userRepository.save(UserTest.SANJIGI);
        questionId = questionRepository.save(new Question(Q1.getTitle(), Q1.getContents()).writeBy(javajigi)).getId();
        questionRepository.save(new Question(Q2.getTitle(), Q2.getContents()).writeBy(sanjigi));
    }

    @Test()
    @DisplayName("정상 상태의 질문을 ID로 찾는다")
    void findByIdAndDeletedFalse() {
        Optional<Question> find = questionRepository.findByIdAndDeletedFalse(questionId);
        Question question = find.orElse(null);

        assertAll(
                () -> assertEquals(questionId, question.getId()),
                () -> assertEquals("contents1", question.getContents()),
                () -> assertEquals("title1", question.getTitle()),
                () -> assertThat(question.getWriter()).isNotNull(),
                () -> assertThat(question.isDeleted()).isFalse()
        );
    }

    @DisplayName("정상 상태의 질문을 모두 찾는다")
    @Test
    void findByDeletedFalse() {
        List<Question> questionList = questionRepository.findByDeletedFalse();

        assertEquals(2, questionList.size());
    }
}
