package qna.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Answers {

  private final List<Answer> answers;

  public Answers(List<Answer> answers) {
    this.answers = answers;
  }

  public DeleteHistories deleteAnswers(User loginUser) {
    List<DeleteHistory> histories = answers.stream()
        .map(answer -> answer.delete(loginUser))
        .collect(Collectors.toList());
    return new DeleteHistories(histories);
  }
}
