package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repository.AnswerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("Answer 객체를 저장하면 Id가 자동생성 되어 Not Null 이다.")
    void save() {
        assertThat(A1.getId()).isNotNull();
        assertThat(answerRepository.save(A1).getId()).isNotNull();
        assertThat(answerRepository.save(A2).getId()).isNotNull();
    }

    @Test
    @DisplayName("Answer 객체를 조회하면 데이터 여부에 따라 Optional 존재 여부가 다르다." +
            "또한 동일한 객체면 담긴 값도 동일하다.")
    void findByWriterId() {
        answerRepository.save(A1);
        assertThat(answerRepository.findByWriterId(1L).isPresent()).isTrue();
        assertThat(answerRepository.findByWriterId(1L).get().getContents()).isEqualTo(A1.getContents());
        assertThat(answerRepository.findByWriterId(10L).isPresent()).isFalse();
    }

    @Test
    @DisplayName("Answer 객체를 수정하면 수정된 데이터와 일치해야 하고 업데이트 날짜가 Not Null 이다.")
    void update() {
        Answer answer = new Answer(JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        Answer actual = answerRepository.save(answer);
        assertThat(actual.getUpdatedAt()).isNull();

        Long writerId = 5L;
        actual.setWriterId(writerId);

        Answer updated = answerRepository.findByWriterId(writerId).get();

        assertThat(updated.getUpdatedAt()).isNotNull();
        assertThat(updated.getWriterId()).isEqualTo(writerId);
    }

}
