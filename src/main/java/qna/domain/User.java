package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import qna.UnAuthorizedException;

@Entity
@Table(name = "user")
public class User {
	public static final GuestUser GUEST_USER = new GuestUser();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createAt = LocalDateTime.now();

	@Column(name = "email", length = 50)
	private String email;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "password", nullable = false, length = 20)
	private String password;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();

	@Column(name = "user_id", nullable = false, length = 20, unique = true)
	private String userId;

	protected User() {
	}

	public User(String userId, String password, String name, String email) {
		this(null, userId, password, name, email);
	}

	public User(Long id, String userId, String password, String name, String email) {
		this.id = id;
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.email = email;
	}

	public void update(User loginUser, User target) {
		if (!matchUserId(loginUser.userId)) {
			throw new UnAuthorizedException();
		}

		if (!matchPassword(target.password)) {
			throw new UnAuthorizedException();
		}

		this.name = target.name;
		this.email = target.email;
	}

	private boolean matchUserId(String userId) {
		return this.userId.equals(userId);
	}

	public boolean matchPassword(String targetPassword) {
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

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
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
		return Objects.hash(id, createAt, email, name, password, updatedAt, userId);
	}

	private static class GuestUser extends User {
		@Override
		public boolean isGuestUser() {
			return true;
		}
	}
}
