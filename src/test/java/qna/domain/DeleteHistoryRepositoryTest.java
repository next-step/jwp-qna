package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaSystemException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Test
    public void save() {
        DeleteHistory h1 = deleteHistoryRepository.save(DeleteHistoryTest.history1);
        DeleteHistory h2 = deleteHistoryRepository.save(DeleteHistoryTest.history2);

        assertThat(h1.getId()).isNotNull();
        assertThat(h1.getId()).isEqualTo(1L);
        assertThat(h2.getId()).isNotNull();
        assertThat(h2.getId()).isEqualTo(2L);


        // db에 반영이 안될때는 이상이 없다.
        h1.setId(null);
        // db에 null setting이 반영되면 JpaSystemException 발생
        // 트랜잭션 내 lazy sql 확인가능
        assertThatThrownBy(()->{
            h2.setId(null);
            deleteHistoryRepository.findAll();
        }).isInstanceOf(JpaSystemException.class);
    }
}