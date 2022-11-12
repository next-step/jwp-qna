package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerEntityTest.provideAnswer;
import static qna.domain.QuestionEntityTest.provideQuestion;
import static qna.domain.UserEntityTest.provideUser;

@DisplayName("삭제 히스토리 클래스 테스트")
public class DeleteHistoryEntityTest {

    @DisplayName("질문 삭제 히스토리 생성")
    @Test
    void fromQuestion_deleteHistory_success() {
        //given:
        final User user = provideUser();
        final Question question = provideQuestion().writeBy(user);
        //when:
        final DeleteHistory deleteHistory = DeleteHistory.fromQuestion(question);
        //then:
        assertThat(deleteHistory.getContentId()).isEqualTo(question.getId());
    }

    @DisplayName("답변 삭제 히스토리 생성")
    @Test
    void fromAnswer_deleteHistory_success() {
        //given:
        final User user = provideUser();
        final Question question = provideQuestion().writeBy(user);
        final Answer answer = provideAnswer(1L, user, question);
        //when:
        final DeleteHistory deleteHistory = DeleteHistory.fromAnswer(answer);
        //then:
        assertThat(deleteHistory.getContentId()).isEqualTo(answer.getId());
    }

    @DisplayName("삭제 히스토리 목록 생성 성공")
    @Test
    void fromQuestion_deleteHistoryMaker_success() {
        //given:
        User owner = provideUser();
        Question question = provideQuestion().writeBy(owner);
        Answer answer1 = provideAnswer(1L, owner, question);
        Answer answer2 = provideAnswer(2L, owner, question);
        Answer answer3 = provideAnswer(3L, owner, question);
        question.updateAnswers(new HashSet<>(Arrays.asList(answer1, answer2, answer3)));
        //when:
        List<DeleteHistory> deleteHistoryList = DeleteHistory.listFromQuestion(question);
        //then:
        assertThat(deleteHistoryList.stream().map(DeleteHistory::getContentId)).contains(
                question.getId(), answer1.getId(), answer2.getId(), answer3.getId());
    }
}
