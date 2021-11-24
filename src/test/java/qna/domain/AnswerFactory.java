package qna.domain;

import qna.domain.commons.Contents;

public class AnswerFactory {
  private AnswerFactory() {}

  static Answer create(User writer, Question question, Contents contents) {
    return new Answer(writer, question, contents);
  }
}
