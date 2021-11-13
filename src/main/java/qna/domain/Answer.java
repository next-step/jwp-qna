package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private User writer;

  @ManyToOne
  private Question question;

  @Lob
  private String contents;

  @Column(columnDefinition = "bit", nullable = false)
  private boolean deleted = false;

  protected Answer() {}

  public Answer(User writer, Question question, String contents) {
    this(null, writer, question, contents);
  }

  public Answer(Long id, User writer, Question question, String contents) {
    this.id = id;

    if (Objects.isNull(writer)) {
      throw new UnAuthorizedException();
    }

    if (Objects.isNull(question)) {
      throw new NotFoundException();
    }

    this.writer = writer;
    this.question = question;
    this.contents = contents;
  }

  public boolean isOwner(User writer) {
    return this.writer.equals(writer);
  }

  public void toQuestion(Question question) {
    this.question = question;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getWriter() {
    return writer;
  }

  public void setWriter(User writer) {
    this.writer = writer;
  }

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  public String getContents() {
    return contents;
  }

  public void setContents(String contents) {
    this.contents = contents;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  @Override
  public String toString() {
    return "Answer{"
      + "id="
      + id
      + ", writer="
      + writer
      + ", question="
      + question
      + ", contents='"
      + contents
      + '\''
      + ", deleted="
      + deleted
      + ", createdAt="
      + super.getCreatedAt()
      + ", updatedAt="
      + super.getUpdatedAt()
      + '}';
  }
}
