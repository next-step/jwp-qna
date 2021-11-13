package qna.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false)
  private String title;

  @Lob
  private String contents;

  @ManyToOne
  private User writer;

  @Column(columnDefinition = "bit", nullable = false)
  private boolean deleted = false;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
  private final List<Answer> answers = new ArrayList<>();

  public Question(String title, String contents) {
    this(null, title, contents);
  }

  public Question(Long id, String title, String contents) {
    this.id = id;
    this.title = title;
    this.contents = contents;
  }

  protected Question() {}

  public Question writeBy(User writer) {
    this.writer = writer;
    return this;
  }

  public boolean isOwner(User writer) {
    return this.writer.equals(writer);
  }

  public void addAnswer(Answer answer) {
    answer.toQuestion(this);
    answers.add(answer);
  }

  public List<Answer> getAnswers() {
    return answers;
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
