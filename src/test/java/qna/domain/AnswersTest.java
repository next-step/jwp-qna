package qna.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AnswersTest {

  @DisplayName("작성자가 아닌 답변이 있으면 삭제할 수 없다.")
  @Test
  void deleteAnswersFailTest() {
    Answers givenAnswers = new Answers(Lists.newArrayList(AnswerTest.A1, AnswerTest.A2));
    assertThatThrownBy(() -> givenAnswers.deleteAnswers(UserTest.JAVAJIGI)).isInstanceOf(CannotDeleteException.class)
                                                                          .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
  }

  @DisplayName("삭제 하면 삭제 내역 목록을 반환한다.")
  @Test
  void deleteAnswersTest() {
    //given
    Answer givenFirstAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    Answer givenSecondAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents2");
    Answers givenAnswers = new Answers(Lists.newArrayList(givenFirstAnswer, givenSecondAnswer));

    DeleteHistory givenFirstHistory = new DeleteHistory(ContentType.ANSWER, givenFirstAnswer.getId(), UserTest.JAVAJIGI, LocalDateTime.now());
    DeleteHistory givenSecondHistory = new DeleteHistory(ContentType.ANSWER, givenSecondAnswer.getId(), UserTest.JAVAJIGI, LocalDateTime.now());
    DeleteHistories givenDeleteHistories = new DeleteHistories(Lists.newArrayList(givenFirstHistory, givenSecondHistory));

    //when & then
    assertAll(
        () -> assertThat(givenAnswers.deleteAnswers(UserTest.JAVAJIGI)).isEqualTo(givenDeleteHistories),
        () -> assertThat(givenFirstAnswer.isDeleted()).isTrue(),
        () -> assertThat(givenSecondAnswer.isDeleted()).isTrue()
    );
  }

}
