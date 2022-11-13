package qna.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Title {

    private String name;

    protected Title() {
    }

    public Title(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
