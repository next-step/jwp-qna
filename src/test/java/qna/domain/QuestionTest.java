package qna.domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.TestDataSourceConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@TestDataSourceConfig
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Q1.writeBy(userRepository.save(JAVAJIGI));
        Q2.writeBy(userRepository.save(SANJIGI));
    }

    @DisplayName("question 저장 검증")
    @Test
    void saveTest() {
        Question saved = questionRepository.save(Q1);

        assertNotNull(saved.getId());
        assertEquals(Q1.getTitle(), saved.getTitle());
        assertEquals(Q1.getContents(), saved.getContents());
    }

    @DisplayName("삭제되지 않은 데이터 찾아오기")
    @Test
    void findByIdAndDeletedFalseTest() {

        Question expected = questionRepository.save(Q1);
        Question actual = questionRepository.findByIdAndDeletedFalse(expected.getId())
                                            .orElseThrow(IllegalArgumentException::new);

        assertFalse(actual.isDeleted());
        equals(expected, actual);
    }

    @DisplayName("findByDeletedFalse 검증")
    @Test
    void findByDeletedFalseTest() {

        List<Question> expected = new ArrayList<>();
        expected.add(questionRepository.save(Q1));
        expected.add(questionRepository.save(Q2));

        List<Question> actual = questionRepository.findByDeletedFalse();

        for (int i = 0; i < expected.size(); i++) {
            assertFalse(actual.get(i).isDeleted());
            equals(expected.get(i), actual.get(i));
        }
    }

    private void equals(Question expected, Question actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getContents(), actual.getContents());
        assertEquals(expected.getWriter(), actual.getWriter());
    }
}
