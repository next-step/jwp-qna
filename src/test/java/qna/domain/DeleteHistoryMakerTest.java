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

@DisplayName("삭제 히스토리 목록 생성 유틸 클래스 테스트")
public class DeleteHistoryMakerTest {

    @DisplayName("삭제 히스토리 목록 생성 성공")
    @Test
    void fromQuestion_deleteHistoryMaker_success() {
        //given:
        User owner = provideUser();
        Question question = provideQuestion().writeBy(owner);
        Answer answer1 = provideAnswer(1L, owner, question);
        Answer answer2 = provideAnswer(2L, owner, question);
        Answer answer3 = provideAnswer(3L, owner, question);
        question.setAnswers(new HashSet<>(Arrays.asList(answer1, answer2, answer3)));
        //when:
        List<DeleteHistory> deleteHistoryList = DeleteHistoryMaker.fromQuestion(question);
        //then:
        assertThat(deleteHistoryList.stream().map(DeleteHistory::getContentId)).contains(
                question.getId(), answer1.getId(), answer2.getId(), answer3.getId());
    }
}
