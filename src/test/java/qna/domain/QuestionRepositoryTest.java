package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import qna.common.CommonRepositoryTest;

class QuestionRepositoryTest extends CommonRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    private User writer;

    @BeforeEach
    void setUp() {
        writer = userRepository.save(
            new User("writer", "123", "writer", "writer@email.com"));
    }

    @DisplayName("Question 을 저장한다")
    @Test
    void save() {
        // given
        Question question = createQuestion("question", "contents", writer);

        // when
        Question savedQuestion = questionRepository.save(question);

        // then
        assertEquals(question, savedQuestion);
    }

    @DisplayName("Question 을 아이디로 찾는다")
    @Test
    void findById() {
        // given
        Question savedQuestion = questionRepository.save(createQuestion("question", "contents", writer));

        // when
        Question findQuestion = questionRepository.findById(savedQuestion.getId()).orElse(null);

        // then
        assertNotNull(findQuestion);
        assertEquals(savedQuestion, findQuestion);
    }

    @DisplayName("삭제되지 않은 모든 Question 을 가져온다")
    @Test
    void findByDeletedFalse() {
        // given
        String firstTitle = "question1";
        String secondTitle = "question2";
        String firstContents = "contents1";
        String secondContents = "contents2";
        questionRepository.save(createQuestion(firstTitle, firstContents, writer));
        questionRepository.save(createQuestion(secondTitle, secondContents, writer));

        // when
        List<Question> questions = questionRepository.findByDeletedFalse();

        // then
        assertEquals(2, questions.size());
        assertThat(questions).extracting("title")
            .containsExactly(new Title(firstTitle), new Title(secondTitle));
        assertThat(questions).extracting("contents")
            .containsExactly(new Contents(firstContents), new Contents(secondContents));
    }

    @DisplayName("삭제되지 않은 Question 을 아이디로 가져온다")
    @Test
    void findByIdAndDeletedFalse() {
        // given
        Question savedQuestion = questionRepository.save(createQuestion("question", "contents", writer));

        // when
        Question findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId()).orElse(null);

        // then
        assertNotNull(findQuestion);
        assertEquals(savedQuestion, findQuestion);
    }

    @DisplayName("삭제된 Question 은 아이디로 가져오지 못한다")
    @Test
    void findByIdAndDeletedFalseNull() {
        // given
        Question savedQuestion = questionRepository.save(createQuestion("question", "contents", writer));
        savedQuestion.setDeleted(true);

        // when
        Question findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId()).orElse(null);

        // then
        assertNull(findQuestion);
    }

    private Question createQuestion(String title, String contents, User writer) {
        return new Question(title, contents, writer);
    }
}
