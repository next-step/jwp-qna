package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repository.AnswerRepository;
import qna.repository.QuestionRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
public class AnswerTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("Answer 객체를 저장하면 Id가 자동생성 되어 Not Null 이다.")
    void save() {
        Question question = questionRepository.save(Q1);
        Answer answer = new Answer(JAVAJIGI, question, "Answers Contents1");
        assertThat(answer.getId()).isNull();

        Answer actual = answerRepository.save(answer);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNull()
        );
    }

    @Test
    @DisplayName("Answer 객체를 조회하면 데이터 여부에 따라 Optional 존재 여부가 다르다." +
            "또한 동일한 객체면 담긴 값도 동일하다.")
    void findByWriterId() {
        Question question = questionRepository.save(Q1);
        final Answer answer = new Answer(JAVAJIGI, question, "Answers Contents");
        answerRepository.save(answer);
        assertAll(
                () -> assertThat(answerRepository.findByWriterId(1L))
                        .isPresent().get().extracting(Answer::getContents).isEqualTo(answer.getContents()),
                () -> assertThat(answerRepository.findByWriterId(10L)).isEmpty()
        );
    }

    @Test
    @DisplayName("Answer 객체를 수정하면 수정된 데이터와 일치해야 하고 업데이트 날짜가 Not Null 이다.")
    void update() {
        Question question = questionRepository.save(Q1);
        Answer answer = new Answer(JAVAJIGI, question, "Answers Contents1");
        Answer actual = answerRepository.save(answer);

        Long writerId = 5L;
        actual.setWriterId(writerId);

        Answer updated = answerRepository.findByWriterId(writerId).get();
        assertAll(
                () -> assertThat(updated.getUpdatedAt()).isNotNull(),
                () -> assertThat(updated.getWriterId()).isEqualTo(writerId)
        );
    }

}
