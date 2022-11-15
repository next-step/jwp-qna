package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("수정 테스트")
    void updateTest() {
        User newJavajigi = new User(JAVAJIGI.getId(), JAVAJIGI.getUserId(), JAVAJIGI.getPassword(), "new_name", "new_javajigi@slipp.net");
        
        assertThatNoException().isThrownBy(() -> JAVAJIGI.update(newJavajigi, JAVAJIGI));
    }
    
    @Test
    @DisplayName("오류 핸들링 테스트")
    void ExceptionTest() {
        User newJavajigi = new User(JAVAJIGI.getId(), JAVAJIGI.getUserId(), "wrong_password", "new_name", "new_javajigi@slipp.net");
        
        assertAll(
            () -> assertThrows(UnAuthorizedException.class, () -> {
                JAVAJIGI.update(SANJIGI, JAVAJIGI);
            }),
            () -> assertThrows(UnAuthorizedException.class, () -> {
                JAVAJIGI.update(JAVAJIGI, newJavajigi);
            })
        );
    }
    
    @Test
    @DisplayName("equalsNameAndEmail 메소드 테스트")
    void toQuestionTest() {
        SANJIGI.setEmail(JAVAJIGI.getEmail());
        
        assertThat(JAVAJIGI.equalsNameAndEmail(SANJIGI)).isTrue();
    }
}
