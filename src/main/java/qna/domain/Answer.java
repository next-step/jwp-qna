package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long writerId;
    private Long questionId;
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;

    protected Answer() {
    }

    private Answer(AnswerBuilder AnswerBuilder) {
        this.id = AnswerBuilder.id;
        this.writerId = AnswerBuilder.writerId;
        this.questionId = AnswerBuilder.questionId;
        this.contents = AnswerBuilder.contents;
    }

    public static class AnswerBuilder {
        private Long id;
        private final Long writerId;
        private final Long questionId;
        private String contents;

        public AnswerBuilder(User writer, Question question) {
            if (Objects.isNull(writer)) {
                throw new UnAuthorizedException("작성자 정보가 없습니다.");
            }
            if (Objects.isNull(question)) {
                throw new NotFoundException("질문 정보가 없습니다.");
            }
            this.writerId = writer.getId();
            this.questionId = question.getId();
        }

        public AnswerBuilder id(long id) {
            this.id = id;
            return this;
        }

        public AnswerBuilder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Answer build() {
            return new Answer(this);
        }
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer.getId());
    }

    public void toQuestion(Question question) {
        this.questionId = question.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWriterId() {
        return writerId;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writerId +
                ", questionId=" + questionId +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
