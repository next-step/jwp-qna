package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, length = 20)
	private String userId;

	@Column(length = 20, nullable = false)
	private String password;

	@Column(length = 20, nullable = false)
	private String name;

	@Column(length = 50)
	private String email;

	protected User() {
	}

	public User(String userId, String password, String name, String email) {
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
	public String toString() {
		return "User{" +
				"id=" + id +
				", userId='" + userId + '\'' +
				", password='" + password + '\'' +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
