package qna.domain;

import qna.ForbiddenException;
import subway.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Question extends BaseDateEntity{
    private static final int TITLE_LENGTH = 100;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;
    @Column(nullable = false, length = TITLE_LENGTH)
    private String title;
    @OneToMany(mappedBy = "question")
    List<Answer> answers = new ArrayList<Answer>();
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "WRITER_ID")
    private User user;

    protected Question() {}

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(String title, String contents, User writer) {
        this(null, title, contents, writer);
    }

    public Question(Long id, String title, String contents) {
        if (Objects.isNull(title) || title.isEmpty()) {
            throw new ForbiddenException();
        }

        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question(Long id, String title, String contents, User writer) {
        if (Objects.isNull(title) || title.isEmpty()) {
            throw new ForbiddenException();
        }

        this.id = id;
        this.title = title;
        this.contents = contents;
        this.user = writer;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public boolean isOwner(User writer) {
        return this.user.equals(writer);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                ", writerId=" + user.getId() +
                ", deleted=" + deleted +
                '}';
    }
}
