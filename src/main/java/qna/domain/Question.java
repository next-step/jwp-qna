package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "question")
public class Question extends BaseTime {

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "longtext")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Question() {

    }

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

    private boolean canDelete(User writer) {
        return isOwner(writer);
    }

    public List<DeleteHistory> delete(User writer) throws CannotDeleteException {
        if(!canDelete(writer)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        this.deleted = true;
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(recordDeleteHistory());
        deleteHistories.addAll(deleteAnswer(writer));
        return deleteHistories;
    }

    private DeleteHistory recordDeleteHistory() {
        return new DeleteHistory(ContentType.QUESTION, this.id, this.writer, LocalDateTime.now());
    }

    private List<Answer> notDeletedAnswers() {
        return this.answers.stream()
                            .filter(a -> a.isNotDeleted())
                            .collect(toList());
    }

    public List<DeleteHistory> deleteAnswer(User writer) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        List<Answer> answers = notDeletedAnswers();
        for(Answer answer : answers)
        {
            deleteHistories.add(answer.delete(writer));
        }
        return deleteHistories;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
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
