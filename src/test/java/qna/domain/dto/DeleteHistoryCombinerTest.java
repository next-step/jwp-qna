package qna.domain.dto;

import static qna.domain.ContentType.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;

class DeleteHistoryCombinerTest {
    @Test
    void 단건_다건_저장시_데이터가_누락되는게_없는지_확인() {
        // given
        DeleteHistoryCombiner deleteHistoryCombiner = new DeleteHistoryCombiner();
        Question question = new Question("title1", "contents");
        User user = new User("seunghoona", "password", "이름", "이메일");

        // when
        deleteHistoryCombiner.add(new DeleteHistory(QUESTION, question, user, LocalDateTime.now()));
        deleteHistoryCombiner.add(Arrays.asList(new DeleteHistory(QUESTION, question, user, LocalDateTime.now())));

        // then
        List<DeleteHistory> combiner = deleteHistoryCombiner.getCombiner();
        Assertions.assertThat(combiner.size()).isEqualTo(2);
    }
}