package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import qna.CannotDeleteException;

@Entity
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean deleted = false;

    @Column(length = 100, nullable = false)
    private String title;

    @Embedded
    private Contents contents;

    @Embedded
    private final QuestionAnswers answers = new QuestionAnswers();

    protected Question() {
    }

    public Question(final User writer, final String title, final String contents) {
        this(null, writer, title, contents);
    }

    public Question(final Long id, final User writer, final String title, final String contents) {
        this.id = id;
        this.contents = new Contents(contents, writer);
        this.title = title;
    }

    public List<DeleteHistory> deleteBy(final User loginUser) {
        final List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(deleteQuestionBy(loginUser));
        deleteHistories.addAll(answers.deleteBy(loginUser));
        return deleteHistories;
    }

    private DeleteHistory deleteQuestionBy(final User loginUser) {
        checkAuthority(loginUser);
        this.deleted = true;
        return new DeleteHistory(ContentType.QUESTION, id, loginUser);
    }

    private void checkAuthority(User loginUser) {
        if (!contents.isWrittenBy(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public void addAnswer(final Answer answer) {
        answers.add(answer);
    }

    public Long getId() {
        return id;
    }

    public Contents getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getTitle() {
        return title;
    }

    public QuestionAnswers getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", contents='" + contents + '\'' +
            ", deleted=" + deleted +
            '}';
    }
}
