package qna.domain;

public class AnswerFactory {
  private AnswerFactory() {}

  static Answer create(User writer, Question question, String contents) {
    return new Answer(writer, question, contents);
  }
}
