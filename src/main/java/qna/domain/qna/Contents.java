package qna.domain.qna;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import qna.common.exception.InvalidParamException;

@Embeddable
public class Contents {

    @Lob
    @Column(name = "contents")
    private String contents;

    protected Contents() {
    }

    private Contents(String contents) {
        valid(contents);

        this.contents = contents;
    }

    public static Contents of(String contents) {
        return new Contents(contents);
    }

    public String getContents() {
        return contents;
    }

    private void valid(String contents) {
        if (Objects.isNull(contents) || contents.isEmpty()) {
            throw new InvalidParamException("컨텐츠 내용을 입력해주세요.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contents contents1 = (Contents) o;
        return Objects.equals(contents, contents1.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }
}
