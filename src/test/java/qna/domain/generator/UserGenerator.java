package qna.domain.generator;

import qna.domain.user.User;
import qna.domain.user.UserRepository;

public class UserGenerator {

	private final UserRepository userRepository;

	public UserGenerator(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public static User questionWriter() {
		return new User("hahoho87", "password", "질문자", "hahoho87@email.com");
	}

	public static User answerWriter() {
		return new User("hahaha90", "password", "답변자", "hahaha90@email.com");
	}

	private static User of(String userId, String password, String name, String email) {
		return new User(userId, password, name, email);
	}

	public User savedUser() {
		return userRepository.saveAndFlush(questionWriter());
	}

	public User savedUser(User user) {
		return userRepository.saveAndFlush(user);
	}
}
