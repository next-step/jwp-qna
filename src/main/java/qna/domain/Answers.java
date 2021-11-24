package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
  @OneToMany
  private List<Answer> answers = new ArrayList<>();

  protected Answers() {}

  public Answers(List<Answer> answers) {
    this.answers = answers;
  }

  public void add(Answer answer) {
    answers.add(answer);
  }

  public DeleteHistories deleteAll(User loginUser) {
    return new DeleteHistories(
      answers.stream()
        .map(answer -> answer.delete(loginUser))
        .collect(Collectors.toList())
    );
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
