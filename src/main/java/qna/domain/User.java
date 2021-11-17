package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import qna.UnAuthorizedException;

@Entity
public class User extends AuditEntity {

	public static final GuestUser GUEST_USER = new GuestUser();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	@Column(length = Email.MAX_LENGTH)
	private Email email;

	@Embedded
	@Column(length = UserName.MAX_LENGTH, nullable = false)
	private UserName name;

	@Embedded
	@Column(length = Password.MAX_LENGTH, nullable = false)
	private Password password;

	@Embedded
	@Column(length = UserId.MAX_LENGTH, unique = true, nullable = false)
	private UserId userId;

	protected User() {
	}

	public User(String userId, String password, String name, String email) {
		this(null, userId, password, name, email);
	}

	public User(Long id, String userId, String password, String name, String email) {
		this.id = id;
		this.userId = UserId.of(userId);
		this.password = Password.of(password);
		this.name = UserName.of(name);
		this.email = Email.of(email);
	}

	public void update(User loginUser, User target) {
		validateUpdate(loginUser, target);
		this.name = target.name;
		this.email = target.email;
	}

	private void validateUpdate(User loginUser, User target) {
		if (!matchUserId(loginUser.userId)) {
			throw new UnAuthorizedException();
		}

		if (!matchPassword(target.password)) {
			throw new UnAuthorizedException();
		}
	}

	private boolean matchUserId(UserId userId) {
		return this.userId.equals(userId);
	}

	public boolean matchPassword(Password targetPassword) {
		return this.password.equals(targetPassword);
	}

	public boolean equalsNameAndEmail(User target) {
		if (Objects.isNull(target)) {
			return false;
		}

		return name.equals(target.name) &&
			email.equals(target.email);
	}

	public boolean isGuestUser() {
		return false;
	}

	public Long getId() {
		return id;
	}

	public UserId getUserId() {
		return this.userId;
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", userId='" + userId + '\'' +
			", password='" + password + '\'' +
			", name='" + name + '\'' +
			", email='" + email + '\'' +
			'}';
	}

	private static class GuestUser extends User {
		@Override
		public boolean isGuestUser() {
			return true;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		User user = (User)o;

		return Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
