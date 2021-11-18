package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void 삭제이력_저장() {
        //given
        User writer = userRepository.save(TestUserFactory.create("donkey"));
        DeleteHistory actual = deleteHistoryRepository.save(TestDeleteHistoryFactory.create(ContentType.ANSWER, 1L, writer));
        Long savedId = actual.getId();

        //when
        DeleteHistory expected = deleteHistoryRepository.findById(savedId).get();

        //then
        assertThat(actual).isSameAs(expected);
    }

    @Test
    public void 사용자_삭제이력_조회() {
        //given
        User writer = userRepository.save(TestUserFactory.create("donkey"));
        deleteHistoryRepository.save(TestDeleteHistoryFactory.create(ContentType.ANSWER, 1L, writer));
        deleteHistoryRepository.save(TestDeleteHistoryFactory.create(ContentType.ANSWER, 2L, writer));

        //when
        List<DeleteHistory> expected = deleteHistoryRepository.findAll();

        //then
        assertThat(expected).hasSize(2);
    }
}
