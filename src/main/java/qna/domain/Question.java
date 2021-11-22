package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Question extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false)
  private String title;

  @Lob
  private String contents;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
  private User writer;

  @Column(columnDefinition = "bit", nullable = false)
  private boolean deleted = false;

  @Embedded
  private Answers answers = new Answers();

  protected Question() {}

  public Question(String title, String contents) {
    this(null, title, contents);
  }

  public Question(Long id, String title, String contents) {
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
    setDeleted(true);

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContents() {
    return contents;
  }

  public void setContents(String contents) {
    this.contents = contents;
  }

  public User getWriter() {
    return writer;
  }

  public void setWriter(User writer) {
    this.writer = writer;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
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
