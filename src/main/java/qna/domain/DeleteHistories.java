package qna.domain;

import java.util.List;

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
}
