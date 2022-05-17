package qna.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Lob
    private String contents;
    private Long writerId;
    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {
    }

    private Question(QuestionBuilder questionBuilder) {
        this.id = questionBuilder.id;
        this.title = questionBuilder.title;
        this.contents = questionBuilder.contents;
    }

    public static class QuestionBuilder {
        private Long id;
        private final String title;
        private String contents;

        public QuestionBuilder(String title) {
            this.title = title;
        }

        public QuestionBuilder id(long id) {
            this.id = id;
            return this;
        }

        public QuestionBuilder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Question build() {
            return new Question(this);
        }
    }

    public Question writeBy(User writer) {
        this.writerId = writer.getId();
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer.getId());
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
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

    public String getContents() {
        return contents;
    }

    public Long getWriterId() {
        return writerId;
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
                ", writerId=" + writerId +
                ", deleted=" + deleted +
                '}';
    }
}
