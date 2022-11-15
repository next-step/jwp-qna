package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy ="question")
    List<Answer> answers = new ArrayList<>();

    protected Answers() {

    }
}
