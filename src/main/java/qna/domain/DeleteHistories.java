package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class DeleteHistories {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    protected DeleteHistories() {
    }

    public void add(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
    }

    public List<DeleteHistory> delete(User loginUser) {
        return null;
    }
}
