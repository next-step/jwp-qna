package qna.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class DeleteHistoryRepositoryTest {

  @Autowired
  private DeleteHistoryRepository deleteHistoryRepository;

  @Test
  void save() {
    DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());

    DeleteHistory actual = deleteHistoryRepository.save(expected);

    assertAll(
        () -> assertNotNull(actual.getId()),
        () -> assertEquals(expected.getId(), actual.getId())
    );
  }
}