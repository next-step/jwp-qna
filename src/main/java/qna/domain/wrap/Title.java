package qna.domain.wrap;

import java.util.Objects;

import static java.lang.String.format;

public class Title {
    private static final int MAXIMUM_LENGTH = 100;

    private String title;

    public Title(String title) {
        validate(title);

        this.title = title;
    }

    private void validate(String title) {
        if (Objects.isNull(title)) {
            throw new IllegalArgumentException("제목은 null일 수 없습니다.");
        } else if(title.length() > MAXIMUM_LENGTH) {
            throw new IllegalArgumentException(format("제목의 최대 길이는 %d자 입니다.", MAXIMUM_LENGTH));
        }
    }

    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title1 = (Title) o;
        return Objects.equals(title, title1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
