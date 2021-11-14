package qna.domain;

import java.util.List;

public class DeleteHistories {

    private List<DeleteHistory> values;

    private DeleteHistories(List<DeleteHistory> values) {
        this.values = values;
    }

    public static DeleteHistories from(List<DeleteHistory> values) {
        return new DeleteHistories(values);
    }

    public List<DeleteHistory> getValues() {
        return values;
    }
}
