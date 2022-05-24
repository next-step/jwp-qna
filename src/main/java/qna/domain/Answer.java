package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseWriterEntity {
    @Lob
    private String contents;

    private boolean deleted = false;

    private Long questionId;

    public Answer() {
    }

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

        this.writerId = writer.getId();
        this.questionId = question.getId();
        this.contents = contents;
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

    public Long getQuestionId() {
        return questionId;
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
                "contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", questionId=" + questionId +
                ", id=" + id +
                ", writerId=" + writerId +
                '}';
    }
}
