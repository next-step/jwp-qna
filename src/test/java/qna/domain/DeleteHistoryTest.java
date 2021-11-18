package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistorys;

    @Test
    void init() {
        DeleteHistory d = new DeleteHistory();
        deleteHistorys.save(d);
    }
}
