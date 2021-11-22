package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Questions {
  @OneToMany(mappedBy = "writer")
  private List<Question> questions = new ArrayList<>();

  protected Questions() {}

  public Questions(List<Question> questions) {
    this.questions = questions;
  }

  public void add(Question question) {
    questions.add(question);
  }
}
