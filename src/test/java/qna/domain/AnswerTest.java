package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class AnswerTest {
    @Autowired
    private AnswerRepository answerRepository;

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("저장한_객체와_저장된_객체_비교")
    @Test
    void 저장한_객체와_저장된_객체_비교() {
        Answer answer = new Answer("zz", false);
        Answer actual = answerRepository.save(answer);
        assertThat(actual).isEqualTo(answer);
    }

    @DisplayName("BaseEntity")
    @Test
    void base_entity_test() {
        Answer answer = new Answer("zz", false);
        Answer actual = answerRepository.save(answer);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @DisplayName("update 테스트(변경감지)")
    @Test
    void update() {
        Answer answer = new Answer("zz", false);
        Answer actual = answerRepository.save(answer);

        answer.setContents("gtgt");
        assertThat(actual.getContents()).isEqualTo("gtgt");
    }

    @Test
    void base_entity_등록() {
        LocalDateTime now = LocalDateTime.now();
        Answer answer = new Answer("zz", false);
        answerRepository.save(answer);

        List<Answer> answers = answerRepository.findAll();

        Answer actual = answers.get(0);

        assertThat(actual.getCreatedAt()).isAfter(now);
        assertThat(actual.getUpdatedAt()).isAfter(now);
    }

    @DisplayName("답변 중 다른 사람이 쓴게 있는 경우")
    @Test
    public void delete_답변_중_다른_사람이_쓴_글() {
        assertThatThrownBy(() -> A1.isWrittenBySomeoneElse(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);

        assertThatThrownBy(() -> A2.isWrittenBySomeoneElse(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
