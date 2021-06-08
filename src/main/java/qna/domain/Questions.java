package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Questions {
    @OneToMany(mappedBy = "writer")
    private final List<Question> questions;

    public Questions(List<Question> questions) {
        this.questions = questions;
    }
    protected Questions(){this(new ArrayList<>());}
}
