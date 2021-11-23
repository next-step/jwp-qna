package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    void 질문_삭제_이력_저장() {
        final User javajigi = userRepository.save(UserFixture.javajigi());
        final Question question = questionRepository.save(QuestionFixture.질문().writeBy(javajigi));
        final DeleteHistory history = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, question.getId(), javajigi));
        deleteHistoryRepository.flush();
        assertThat(history.getDeletedBy()).isEqualTo(javajigi);
    }
}
