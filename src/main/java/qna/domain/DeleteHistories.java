package qna.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeleteHistories {
    List<DeleteHistory> histories;

    private DeleteHistories(List<DeleteHistory> histories) {
        this.histories = histories;
    }

    public static DeleteHistories valueOf(DeleteHistory ... histories) {
        return new DeleteHistories(new ArrayList<>(Arrays.asList(histories)));
    }

    public boolean add(DeleteHistory history) {
        return this.histories.add(history);
    }
    
    public List<DeleteHistory> toList() {
        return Collections.unmodifiableList(this.histories);
    }
}
