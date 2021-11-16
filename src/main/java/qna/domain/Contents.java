package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Embeddable
public class Contents {
    @Column(columnDefinition = "clob")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    protected Contents() {
    }

    public Contents(final String contents, final User writer) {
        this.contents = contents;
        this.writer = writer;
    }

    public boolean isWrittenBy(final User loginUser) {
        return writer.equalsAccount(loginUser);
    }

    public String getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    @Override
    public String toString() {
        return "Contents{" +
            "contents='" + contents + '\'' +
            ", writerId=" + writer.getId() +
            '}';
    }
}
