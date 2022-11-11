package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class DeleteHistories {

    @OneToMany(mappedBy = "deletedBy", cascade = CascadeType.ALL)
    private final List<DeleteHistory> deleteHistories = new ArrayList<>();

    protected DeleteHistories() {

    }


    public List<DeleteHistory> getList() {
        return Collections.unmodifiableList(this.deleteHistories);
    }

    public void add(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
    }

    public void addAll(DeleteHistories deleteHistories) {
        this.deleteHistories.addAll(deleteHistories.getList());
    }

    public boolean contains(DeleteHistory deleteHistory) {
        return this.deleteHistories.contains(deleteHistory);
    }

}
