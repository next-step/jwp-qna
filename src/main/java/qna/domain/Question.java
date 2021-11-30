package qna.domain;

import qna.domain.field.Contents;
import qna.domain.field.Deleted;
import qna.domain.field.Title;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Contents contents;

    @Embedded
    private Deleted deleted;

    @Embedded
    private Title title;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<Answer>();

    // Arguments가 없는 Default Constructor 생성
    protected Question() {}

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = new Title(title);
        this.contents = new Contents(contents);
        this.deleted = new Deleted(false);
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title.getTitle();
    }

    public void registerTitle(String title) {
        this.title = new Title(title);
    }

    public String getContents() {
        return contents.getContents();
    }

    public void registerContents(String contents) {
        this.contents = new Contents(contents);
    }

    public Long getWriterId() {
        return writer.getId();
    }

    public User getWriter() { return this.writer; }

    public boolean isDeleted() {
        return deleted.getDeleted();
    }

    public void changeDeleted(boolean deleted) {
        this.deleted = new Deleted(deleted);
    }
    
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + this.writer.getId() +
                ", deleted=" + deleted +
                '}';
    }

}
