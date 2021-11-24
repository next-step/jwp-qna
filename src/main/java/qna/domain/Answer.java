package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.commons.BaseTimeEntity;
import qna.domain.commons.Contents;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
  private User writer;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
  private Question question;

  @Embedded
  private Contents contents;

  @Column(columnDefinition = "bit", nullable = false)
  private boolean deleted = false;

  protected Answer() {}

  public Answer(User writer, Question question, Contents contents) {
    this(null, writer, question, contents);
  }

  public Answer(Long id, User writer, Question question, Contents contents) {
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

  public DeleteHistory delete(User loginUser) {
    checkDeletableAnswersByUser(loginUser);
    setDeleted(true);
    return DeleteHistory.ofAnswer(id, loginUser);
  }

  private void checkDeletableAnswersByUser(User loginUser) {
    if (!isOwner(loginUser)) {
      throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
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

  public Contents getContents() {
    return contents;
  }

  public void setContents(Contents contents) {
    this.contents = contents;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public boolean isDeleted() {
    return deleted;
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
