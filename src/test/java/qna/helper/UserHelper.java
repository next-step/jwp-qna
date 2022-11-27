package qna.helper;

import qna.domain.User;
import qna.domain.UserRepository;

public class UserHelper {
    private final UserRepository userRepository;

    public UserHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String userId, String password, String name, String email) {
        final User user = new User(userId, password, name, email);
        return userRepository.save(user);
    }
}
