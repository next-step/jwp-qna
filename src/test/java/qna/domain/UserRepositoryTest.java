package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

class UserRepositoryTest extends JpaTest {

    @DisplayName("유저를 생성해서 저장한다.")
    @Test
    void save() {
        //given
        User javajigi = JAVAJIGI;

        //when
        User actual = getUsers().save(javajigi);

        //then
        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(javajigi.getId());
            assertThat(actual.getUserId()).isEqualTo(javajigi.getUserId());
            assertThat(actual.getPassword()).isEqualTo(javajigi.getPassword());
            assertThat(actual.getName()).isEqualTo(javajigi.getName());
            assertThat(actual.getEmail()).isEqualTo(javajigi.getEmail());
        });
    }

    @DisplayName("유저아이디에 해당하는 유저를 리턴한다.")
    @Test
    void findByUserId() {
        //given
        User user = getUsers().save(JAVAJIGI);

        //when
        User actual = getUsers().findByUserId(user.getUserId())
                .orElseThrow(EntityNotFoundException::new);

        //then
        assertThat(actual).isSameAs(user);
    }

    @DisplayName("유저아이디에 해당하는 유저가 없다면 예외를 발생시킨다.")
    @Test
    void findByUserIdException() {
        //given
        User javajigi = JAVAJIGI;

        //when
        assertThatThrownBy(() -> getUsers().findByUserId(javajigi.getUserId())
                .orElseThrow(EntityNotFoundException::new))
                .isInstanceOf(EntityNotFoundException.class); //then
    }
}