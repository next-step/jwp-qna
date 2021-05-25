package qna.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoriesTest {

  @DisplayName("삭제 내역을 추가할 수 있다.")
  @Test
  void addHistory() {
    //given
    DeleteHistory firstHistory = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
    DeleteHistory secondHistory = new DeleteHistory(ContentType.ANSWER, 2L, UserTest.JAVAJIGI, LocalDateTime.now());
    DeleteHistories givenFirstDeleteHistories = new DeleteHistories(Lists.newArrayList(firstHistory));
    DeleteHistories givenSecondDeleteHistories = new DeleteHistories(Lists.newArrayList(secondHistory));

    //when & then
    assertThat(givenFirstDeleteHistories.concat(givenSecondDeleteHistories)).isEqualTo(new DeleteHistories(Lists.newArrayList(firstHistory, secondHistory)));
  }
}
