package qna.domain;

public class QuestionFactory {
  private QuestionFactory() {}

  static Question create(String title, String contents) {
    return new Question(title, contents);
  }
}
