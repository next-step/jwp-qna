package qna.domain;

import java.util.*;

public class DeleteHistories {

  private final List<DeleteHistory> histories;

  public DeleteHistories(List<DeleteHistory> histories) {
    this.histories = Collections.unmodifiableList(histories);
  }

  public DeleteHistories(DeleteHistory deleteHistory) {
    this.histories = Collections.singletonList(deleteHistory);
  }

  public List<DeleteHistory> toUnmodifiableList() {
    return histories;
  }

  public DeleteHistories concat(DeleteHistories other) {
    ArrayList<DeleteHistory> concatList = new ArrayList<>(histories);
    concatList.addAll(other.histories);
    return new DeleteHistories(concatList);
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
