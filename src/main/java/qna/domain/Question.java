package qna.domain;

import qna.CannotDeleteException;
import qna.domain.commons.BaseTimeEntity;
import qna.domain.commons.Contents;
import qna.domain.commons.Deleted;
import qna.domain.commons.Title;

import javax.persistence.*;

@Entity
public class Question extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private Title title;

  @Embedded
  private Contents contents;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
  private User writer;

  @Embedded
  private Deleted deleted = new Deleted();

  @Embedded
  @AttributeOverride( name = "question", column = @Column(name = "question_id"))
  private Answers answers = new Answers();

  protected Question() {}

  public Question(Title title, Contents contents) {
    this(null, title, contents);
  }

  public Question(Long id, Title title, Contents contents) {
    this.id = id;
    this.title = title;
    this.contents = contents;
  }

  public Question writeBy(User writer) {
    this.writer = writer;
    return this;
  }

  public boolean isOwner(User writer) {
    return this.writer.equals(writer);
  }

  public DeleteHistories delete(User loginUser) throws CannotDeleteException {
    checkDeletableQuestionByUser(loginUser);
    deleted.toTrue();

    return DeleteHistories.ofQuestion(DeleteHistory.ofQuestion(id, loginUser), answers.deleteAll(loginUser));
  }



  private void checkDeletableQuestionByUser(User loginUser) throws CannotDeleteException {
    if (!isOwner(loginUser)) {
      throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Title getTitle() {
    return title;
  }

  public void setTitle(Title title) {
    this.title = title;
  }

  public Contents getContents() {
    return contents;
  }

  public void setContents(Contents contents) {
    this.contents = contents;
  }

  public User getWriter() {
    return writer;
  }

  public void setWriter(User writer) {
    this.writer = writer;
  }

  public boolean isDeleted() {
    return deleted.is();
  }

  public void setDeleted(Deleted deleted) {
    this.deleted = deleted;
  }

  public void addAnswer(Answer answer) {
    answers.add(answer);
  }

  public void setAnswers(Answers answers) {
    this.answers = answers;
  }

  public Answers getAnswers() {
    return answers;
  }

  @Override
  public String toString() {
    return "Question{" +
      "id=" + id +
      ", title='" + title + '\'' +
      ", contents='" + contents + '\'' +
      ", writer=" + writer +
      ", deleted=" + deleted +
      '}';
  }

}
