package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("답변을 저장한다.")
    @Test
    void save() {
        Answer saveA1 = answerRepository.save(A1);
        Answer saveA2 = answerRepository.save(A2);

        assertAll(
                () -> assertThat(saveA1.getId()).isNotNull(),
                () -> assertThat(saveA1.getContents()).isEqualTo(A1.getContents()),
                () -> assertThat(saveA2.getId()).isNotNull(),
                () -> assertThat(saveA2.getContents()).isEqualTo(A2.getContents())
        );
    }

    @DisplayName("질문ID로 해당 질문의 미삭제 답변들을 조회한다.")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        Answer saveA1 = answerRepository.save(A1);
        Answer saveA2 = answerRepository.save(A2);

        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(A1.getQuestionId()).size()).isEqualTo(2);
    }

    @DisplayName("ID로 미삭제 답변을 조회한다.")
    @Test
    void findByIdAndDeletedFalse() {
        Answer saveA1 = answerRepository.save(A1);

        assertThat(answerRepository.findByIdAndDeletedFalse(A1.getId()).get()).isEqualTo(saveA1);
    }

}
