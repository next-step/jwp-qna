package qna.domain;




import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void save() {

        // when
        List<Question> questions = questionRepository.saveAll(Arrays.asList(Q1, Q2));

        // then
        assertAll(
            ()-> assertEquals(questions.get(0),Q1),
            ()-> assertEquals(questions.get(1),Q2)
        );
    }

    @Test
    void findByDeletedFalse() {
        List<Question> questions = questionRepository.saveAll(Arrays.asList(Q1, Q2));
        Question question = questions.get(0);
        question.setDeleted(true);

        // when
        List<Question> byDeletedFalse = questionRepository.findByDeletedFalse();

        // then
        assertEquals(byDeletedFalse.size(),1);
    }
}
