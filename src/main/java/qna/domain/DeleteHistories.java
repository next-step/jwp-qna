package qna.domain;

import java.util.List;
import java.util.Objects;

public class DeleteHistories {

  private final List<DeleteHistory> histories;

  public DeleteHistories(List<DeleteHistory> histories) {
    this.histories = histories;
  }

  public List<DeleteHistory> toList() {
    return histories;
  }

  public DeleteHistories addHistory(DeleteHistory toAddHistory) {
    histories.add(toAddHistory);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DeleteHistories that = (DeleteHistories) o;
    return histories.equals(that.histories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(histories);
  }
}
