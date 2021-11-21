package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.answer;
import static qna.domain.QuestionTest.question;

public class DeleteHistoriesTest {

    private static Question question;
    private static List<Answer> answers;

    @BeforeEach
    void setUp() {
        question = question(5);
        answers = new ArrayList<>();
        answers.add(answer(5));
    }

    @DisplayName("question 과 answers 를 delete 하는 정적 팩토리 메소드")
    @Test
    void createDeletedHistories() {
        DeleteHistories deleteHistories = DeleteHistories.createDeletedHistories(question, answers);
        assertThat(deleteHistories).isEqualTo(DeleteHistories.createDeletedHistories(question, answers));
    }

    @DisplayName("deleteQuestion 를 하면 question 의 deleted 컬럼 값이 true 변경되었는지 확인")
    @Test
    void deleteQuestion() {
        DeleteHistories.createDeletedHistories(question, answers);
        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("deleteAnswers 를 하면 Answer deleted 컬럼 값이 true 변경되었는지 확인")
    @Test
    void deleteAnswers() {
        DeleteHistories.createDeletedHistories(question, answers);
        assertThat(answers.get(0).isDeleted()).isTrue();
    }
}
