package qna.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoriesTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("로그인 유저 기준으로 삭제 내역을 추가할 수 있다.")
    public void addDeleteHistoryTest() {
        //given
        User user1 = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));

        DeleteHistory firstHistory = new DeleteHistory(ContentType.ANSWER, 1L, user1, LocalDateTime.now());
        DeleteHistory secondHistory = new DeleteHistory(ContentType.ANSWER, 2L, user1, LocalDateTime.now());

        DeleteHistories givenFirstDeleteHistories = new DeleteHistories(Lists.newArrayList(firstHistory));
        DeleteHistories givenSecondDeleteHistories = new DeleteHistories(Lists.newArrayList(secondHistory));

        //when & then
        assertThat(givenFirstDeleteHistories.concat(givenSecondDeleteHistories))
                .isEqualTo(new DeleteHistories(Lists.newArrayList(firstHistory)).concat(new DeleteHistories(Lists.newArrayList(secondHistory))));
    }

}