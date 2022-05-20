package qna.domain;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
public class Title {

    @Column(length = 100, nullable = false)
    private String title;

    protected Title() {
    }

    public Title(String title) {
        this.title = title;
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
