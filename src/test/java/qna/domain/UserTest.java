package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.generator.UserGenerator.generateQuestionWriter;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import qna.UnAuthorizedException;

@DisplayName("Domain:User")
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    @DisplayName("회원 생성")
    public void createUserEntityTest() {
        // given
        final String userId = "choi-ys";
        final String password = "password";
        final String name = "최용석";
        final String email = "project.log.062@gmail.com";

        // When
        User given = new User(userId, password, name, email);

        // Then
        assertAll(
            () -> assertThat(given.getId()).as("IDENTITY 전략에 의해 DB에서 생성되는 PK값의 Null 여부").isNull(),
            () -> assertThat(given.getUserId()).isEqualTo(userId),
            () -> assertThat(given.getPassword()).isEqualTo(password),
            () -> assertThat(given.getName()).isEqualTo(name),
            () -> assertThat(given.getEmail()).isEqualTo(email),
            () -> assertThat(given.getCreatedAt()).as("JPA Audit에 의해 할당되는 생성일시 정보의 Null 여부").isNull(),
            () -> assertThat(given.getUpdatedAt()).as("JPA Audit에 의해 할당되는 수정일시 정보의 Null 여부").isNull()
        );
    }

    @Test
    @DisplayName("회원 수정")
    public void updateTest() {
        // Given
        final User given = generateQuestionWriter();
        final String userId = "choi-ys";
        final String password = "password";
        final String name = "용석";
        final String email = "rcn115@naver.com";
        final User target = new User(userId, password, name, email);

        // When
        given.update(given, target);

        // Then
        assertAll(
            () -> assertThat(given.getUserId()).isEqualTo(target.getUserId()),
            () -> assertThat(given.getPassword()).isEqualTo(target.getPassword()),
            () -> assertThat(given.getName()).isEqualTo(target.getName()),
            () -> assertThat(given.getEmail()).isEqualTo(target.getEmail())
        );
    }

    @Test
    @DisplayName("[설계 오류] 회원 정보 수정 : 일치하지 않는 아이디 입력 시 예외")
    public void throwException_WhenInvalidUserId() {
        // Given
        final User given = generateQuestionWriter();
        final User target = new User("invalid user id", given.getPassword(), "new Name", "new Email");

        // When : AS-IS
        given.update(given, target);

        // Then : AS-IS
        assertThat(given.getName()).isEqualTo("new Name");
        assertThat(given.getEmail()).isEqualTo("new Email");

        // TODO [설계 오류] 회원 아이디가 일치하지 않는 수정 요청의 경우 UnAuthorizedException 발생
        // When & Then : TO-BE
//        assertThatExceptionOfType(UnAuthorizedException.class)
//            .isThrownBy(() -> given.update(given, target));
    }

    @Test
    @DisplayName("User Entity 수정 : 일치하지 않는 비밀번호 입력 시 예외")
    public void throwException_WhenInvalidPassword() {
        // Given
        final User given = generateQuestionWriter();

        // When
        User target = new User(given.getUserId(), "invalid password", given.getName(), given.getEmail());

        // When & Then
        assertThatExceptionOfType(UnAuthorizedException.class)
            .isThrownBy(() -> given.update(given, target));
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("이름과 이메일 일치 여부")
    public void equalsNameAndEmailTest(final User target, final boolean expected) {
        // Given
        User given = generateQuestionWriter();

        // When & Then
        assertThat(given.equalsNameAndEmail(target)).isEqualTo(expected);
    }

    private static Stream equalsNameAndEmailTest() {
        final User questionWriter = generateQuestionWriter();
        return Stream.of(
            Arguments
                .of(new User(questionWriter.getUserId(), questionWriter.getPassword(), null, null), false),
            Arguments.of(new User(null, null, questionWriter.getName(), questionWriter.getEmail()), true)
        );
    }
}
