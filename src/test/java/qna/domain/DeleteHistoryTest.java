package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    private DeleteHistory answer = new DeleteHistory(ContentType.ANSWER, 1L,
        UserTest.JAVAJIGI);
    private DeleteHistory question = new DeleteHistory(ContentType.QUESTION, 2L,
        UserTest.SANJIGI);

    @BeforeEach
    public void init(){
        UserTest.JAVAJIGI.setId(null);
        UserTest.SANJIGI.setId(null);
    }


    @Test
    public void saveEntity(){
        ArrayList<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(deleteHistoryRepository.save(answer));
        deleteHistories.add(deleteHistoryRepository.save(question));

        assertThat(deleteHistories).extracting("id").isNotNull();
        assertThat(deleteHistories).extracting("contentType").containsExactly(ContentType.ANSWER,ContentType.QUESTION);
    }

    @Test
    public void equalTest(){
        DeleteHistory answerSaved = deleteHistoryRepository.save(answer);
        assertThat(answerSaved.hashCode()).isEqualTo(answer.hashCode());
    }


    @Test
    public void annotingTest(){
        DeleteHistory answerSaved = deleteHistoryRepository.save(answer);
        assertThat(answerSaved.getCreateDate()).isNotNull();
    }

    @Test
    public void cascadePersist(){
        assertThat(answer.getDeletedById().getId()).isNull();
        DeleteHistory answerSaved = deleteHistoryRepository.save(answer);
        assertThat(answerSaved.getDeletedById().getId()).isNotNull();
    }
}
