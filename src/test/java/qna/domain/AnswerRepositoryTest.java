package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("저장을 하고, 다시 가져왔을 때 원본 객체와 같아야 한다")
    public void 저장을_하고_다시_가져왔을_때_원본_객체와_같아야_한다() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        Answer foundAnswer = answerRepository.findById(savedAnswer.getId()).orElseThrow(EntityNotFoundException::new);

        assertSame(AnswerTest.A1, savedAnswer);
        assertSame(savedAnswer, foundAnswer);
    }
}
