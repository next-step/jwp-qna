package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
  @OneToMany(mappedBy = "question")
  private List<Answer> answers = new ArrayList<>();

  protected Answers() {}

  public Answers(List<Answer> answers) {
    this.answers = answers;
  }

  public void add(Answer answer) {
    answers.add(answer);
  }

  public DeleteHistories deleteAll(User loginUser) throws CannotDeleteException {
    checkDeletableAnswersByUser(loginUser);
    return new DeleteHistories(answers.stream().map(answer -> answer.delete(loginUser)).collect(Collectors.toList()));
  }

  private void checkDeletableAnswersByUser(User loginUser) throws CannotDeleteException {
    if (answers != null && !isAllMatchWriter(loginUser)) {
      throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
  }

  public boolean isEmpty() {
    return answers.isEmpty();
  }

  public boolean isAllMatchWriter(User writer) {
    return answers.stream().allMatch(answer -> answer.isOwner(writer));
  }

  public boolean isAllDeleted() {
    return answers.stream().allMatch(Answer::isDeleted);
  }

  public List<Answer> getAnswers() {
    return answers;
  }
}
