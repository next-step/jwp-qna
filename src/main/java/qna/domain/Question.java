package qna.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Question extends BaseEntity{
    @Column(length = 100, nullable = false)
    private String title;
    @Column
    @Lob
    private String contents;
    @ManyToOne
    @JoinColumn(name="writer_id")
    private User writer;
    @Column(nullable = false)
    private boolean deleted = false;
    @Column
    private LocalDateTime updatedAt= LocalDateTime.now();

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

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

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }


    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer +
                ", deleted=" + deleted +
                '}';
    }

    public void setWriter(User writer) {
        this.writer = writer;
        this.writer.addQuestion(this);
    }

    public void delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        addHistory(loginUser);
        List<Answer> answers = filterDeleteFalse();
        deleteAnswers(answers,loginUser);
        deleted = true;
    }

    private void addHistory(User loginUser) {
        DeleteHistory.addHistory(ContentType.QUESTION,id,loginUser);
    }

    private void deleteAnswers(List<Answer> answers, User loginUser) {
        for( Answer answer : answers) {
            answer.delete(loginUser);
        }
    }

    private List<Answer> filterDeleteFalse() {
        List<Answer> filteredAnswer = new ArrayList<>();
        for ( Answer answer: getAnswers()) {
            addAnswerIfNotDeleted(filteredAnswer, answer);
        }
        return filteredAnswer;
    }

    private void addAnswerIfNotDeleted(List<Answer> filteredAnswer, Answer answer) {
        if( !answer.isDeleted()) {
            filteredAnswer.add(answer);
        }
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
