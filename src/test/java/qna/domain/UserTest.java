package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
    @BeforeEach
    void setUp() {

    }

    @Test
    void 유저_질문_추가_테스트() {
        // given
        // when
        User user = 질문_추가_유저_생성();
        // then
        assertThat(user.getQuestion()).contains(QuestionTest.Q1);
    }

    @Test
    void 유저_질문_포함_여부_확인() {
        // given
        User user = 질문_추가_유저_생성();
        // when
        // then
        assertThat(user.containQuestion(QuestionTest.Q1)).isTrue();
    }

    @Test
    void 유저_이름_이메일_변경_테스트() {
        // given
        final User loginUser = JAVAJIGI;
        final User target = 변경_유저_생성();
        // when
        JAVAJIGI.updateNameAndEmail(loginUser, target);
        // then
        assertThat(loginUser.equalsNameAndEmail(target)).isTrue();
    }

    @Test
    void 유저_이름_이메일_변경_아이디_오류_테스트() {
        // given
        final User loginUser = SANJIGI;
        final User target = 변경_유저_생성();
        // when
        // then
        assertThatThrownBy(() -> JAVAJIGI.updateNameAndEmail(loginUser, target))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 유저_이름_이메일_변경_비밀번호_오류_테스트() {
        // given
        final User loginUser = JAVAJIGI;
        final User target = new ChangeNameAndEmailDto("error", "이찬준", "lcjltj@gmail.com").toEntity();
        // when
        // then
        assertThatThrownBy(() -> JAVAJIGI.updateNameAndEmail(loginUser, target))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 게스트_유저_확인() {
        // given
        User user = JAVAJIGI;
        User guest = User.GUEST_USER;
        // when
        // then
        assertThat(user.isGuestUser()).isFalse();
        assertThat(guest.isGuestUser()).isTrue();
    }

    private User 질문_추가_유저_생성() {
        User loginUser = JAVAJIGI;
        loginUser.addQuestion(QuestionTest.Q1);
        return loginUser;
    }

    private User 변경_유저_생성() {
        final User target = new ChangeNameAndEmailDto("password", "이찬준", "lcjltj@gmail.com").toEntity();
        return target;
    }

}
