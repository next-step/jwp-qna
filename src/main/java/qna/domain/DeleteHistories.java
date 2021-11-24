package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class DeleteHistories {
  @OneToMany(mappedBy = "deletedBy")
  private List<DeleteHistory> deleteHistories = new ArrayList<>();

  protected DeleteHistories() {
  }

  public DeleteHistories(List<DeleteHistory> deleteHistories) {
    this.deleteHistories = deleteHistories;
  }

  public static DeleteHistories ofQuestion(DeleteHistory questionDeleteHistory, DeleteHistories answerDeleteHistories) {
    List<DeleteHistory> deleteHistories = new ArrayList<>();
    deleteHistories.add(questionDeleteHistory);
    deleteHistories.addAll(answerDeleteHistories.deleteHistories);

    return new DeleteHistories(deleteHistories);
  }

  public void add(DeleteHistory deleteHistory) {
    deleteHistories.add(deleteHistory);
  }

  public List<DeleteHistory> getDeleteHistories() {
    return deleteHistories;
  }

  public void setDeleteHistories(List<DeleteHistory> deleteHistories) {
    this.deleteHistories = deleteHistories;
  }

  public void addAll(DeleteHistories deleteHistories) {
    this.deleteHistories.addAll(deleteHistories.deleteHistories);
  }
}
