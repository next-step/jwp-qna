package qna.fixture;

import qna.domain.User;

public class UserFixture {
	public static User Y2O2U2N() {
		return User.of("y2o2u2n@gmail.com", "youn", "password", "y2o2u2n");
	}

	public static User Y2O2U2N(long id) {
		return User.of(id, "y2o2u2n@gmail.com", "youn", "password", "y2o2u2n");
	}

	public static User SEMISTONE222() {
		return User.of("semistone222@gmail.com", "junseok", "password", "semistone222");
	}

	public static User SEMISTONE222(long id) {
		return User.of(id, "semistone222@gmail.com", "junseok", "password", "semistone222");
	}
}
