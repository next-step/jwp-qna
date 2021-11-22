package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
  private List<DeleteHistory> deleteHistories = new ArrayList<>();

  public DeleteHistories(List<DeleteHistory> deleteHistories) {
    this.deleteHistories = deleteHistories;
  }

  public static DeleteHistories ofQuestion(DeleteHistory questionDeleteHistory, DeleteHistories answerDeleteHistories) {
    List<DeleteHistory> deleteHistories = new ArrayList<>();
    deleteHistories.add(questionDeleteHistory);
    deleteHistories.addAll(answerDeleteHistories.deleteHistories);

    return new DeleteHistories(deleteHistories);
  }

  public List<DeleteHistory> getList() {
    return deleteHistories;
  }
}
