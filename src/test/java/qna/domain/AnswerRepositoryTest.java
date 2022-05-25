package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void save() {
        final Answer expected = new Answer();
        expected.setContents("hello");
        final Answer actual = answerRepository.save(expected);
        assertNotNull(actual.getId());
    }

}