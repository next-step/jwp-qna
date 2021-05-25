package qna.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DeleteHistoriesTest {

  @DisplayName("삭제 내역을 추가할 수 있다.")
  @Test
  void addHistory() {
    //given
    DeleteHistory givenFirstHistory = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
    DeleteHistory givenSecondHistory = new DeleteHistory(ContentType.ANSWER, 2L, UserTest.JAVAJIGI, LocalDateTime.now());
    DeleteHistories givenDeleteHistories = new DeleteHistories(Lists.newArrayList(givenFirstHistory));

    //when & then
    assertThat(givenDeleteHistories.addHistory(givenSecondHistory).toList()).isEqualTo(Lists.newArrayList(givenFirstHistory, givenSecondHistory));
  }
}
