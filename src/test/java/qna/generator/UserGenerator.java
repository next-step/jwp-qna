package qna.generator;

import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.domain.User;
import qna.domain.UserRepository;

@TestConstructor(autowireMode = AutowireMode.ALL)
public class UserGenerator {

    private final UserRepository userRepository;

    public UserGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static User generateUser(String userId, String password, String name, String email) {
        return new User(userId, password, name, email);
    }

    public static User generateLoginUser() {
        return generateUser("login-user", "password", "인증된 사용자", "login-user@gmail.com");
    }

    public static User generateQuestionWriter() {
        return generateUser("choi-ys", "password", "최용석", "project.log.062@gmail.com");
    }

    public static User generateAnswerWriter() {
        return generateUser("nextstep", "password", "넥스트스텝", "nextstep@nextstep.camp");
    }

    public User savedUser() {
        return userRepository.saveAndFlush(generateQuestionWriter());
    }

    public User savedUser(User user) {
        return userRepository.saveAndFlush(user);
    }
}
