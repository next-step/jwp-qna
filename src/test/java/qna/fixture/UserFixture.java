package qna.fixture;

import qna.domain.User;

public class UserFixture {
	public static User Y2O2U2N() {
		return User.of(1L, "y2o2u2n@gmail.com", "youn", "password", "y2o2u2n");
	}

	public static User SEMISTONE222() {
		return User.of(2L, "semistone222@gmail.com", "junseok", "password", "semistone222");
	}

	public static User JUN222() {
		return User.of(3L, "jun222@gmail.com", "jun", "password", "jun222");
	}
}
