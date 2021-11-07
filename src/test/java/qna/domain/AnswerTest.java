package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository repository;

    @Test
    @DisplayName("답변을 저장한다.")
    void save() {
        //given //when
        Answer saved = repository.save(A1);

        //then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
                () -> assertThat(saved.getQuestionId()).isEqualTo(QuestionTest.Q1.getId()),
                () -> assertThat(saved.getContents()).isEqualTo(A1.getContents())
        );
    }

    @Test
    @DisplayName("질문에 달린 답변 중 삭제되지 않은 답변 목록을 조회한다.")
    void findByQuestionIdAndDeletedFalse() {
        //given
        repository.save(A1);
        repository.save(A2);
        Long questionId = QuestionTest.Q1.getId();

        //when
        List<Answer> answers = repository.findByQuestionIdAndDeletedFalse(questionId);

        //then
        assertThat(answers).hasSize(2);
    }

    @Test
    @DisplayName("삭제되지 않은 답변 한 건을 조회한다.")
    void findByIdAndDeletedFalse() {
        //given
        repository.save(A2);

        //when
        Optional<Answer> findAnswer = repository.findByIdAndDeletedFalse(A2.getId());

        //then
        assertThat(findAnswer.isPresent()).isTrue();
        Answer answer = findAnswer.get();
        assertAll(
                () -> assertThat(answer.getWriterId()).isEqualTo(UserTest.SANJIGI.getId()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(QuestionTest.Q1.getId()),
                () -> assertThat(answer.getContents()).isEqualTo(A2.getContents())
        );
    }
}
