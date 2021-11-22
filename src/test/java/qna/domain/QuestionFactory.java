package qna.domain;

import qna.domain.commons.Contents;
import qna.domain.commons.Title;

public class QuestionFactory {
  private QuestionFactory() {}

  static Question create(Title title, Contents contents) {
    return new Question(title, contents);
  }
}
