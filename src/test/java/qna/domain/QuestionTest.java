package qna.domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.TestDataSourceConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestDataSourceConfig
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("question 저장 검증")
    @Test
    void saveTest() {
        Question saved = questionRepository.save(Q1);

        assertNotNull(saved.getId());
        assertEquals(Q1.getTitle(), saved.getTitle());
        assertEquals(Q1.getContents(), saved.getContents());
    }

    @DisplayName("findByIdAndDeletedFalse 검증")
    @Test
    void findByIdAndDeletedFalseTest() {
        Question expected = questionRepository.save(Q1);
        Question actual = questionRepository.findByIdAndDeletedFalse(expected.getId())
                                            .orElseThrow(IllegalArgumentException::new);

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
            equals(expected.get(i), actual.get(i));
        }
    }

    private void equals(Question expected, Question actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getContents(), actual.getContents());
        assertEquals(expected.getWriterId(), actual.getWriterId());
    }
}
