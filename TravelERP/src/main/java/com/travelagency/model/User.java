package com.travelagency.model;

/**
 * User model representing authentication and authorization
 */
public class User extends BaseEntity {
    private String username;
    private String password;
    private String email;
    private UserRole role;
    private boolean isActive;

    public enum UserRole {
        ADMIN("ADMIN"),
        EMPLOYEE("EMPLOYEE"),
        MANAGER("MANAGER");

        private final String value;

        UserRole(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static UserRole fromString(String value) {
            for (UserRole role : UserRole.values()) {
                if (role.value.equalsIgnoreCase(value)) {
                    return role;
                }
            }
            return EMPLOYEE;
        }
    }

    public User() {
        super();
        this.isActive = true;
    }

    public User(int id, String username, String email, UserRole role) {
        super(id);
        this.username = username;
        this.email = email;
        this.role = role;
        this.isActive = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return id * 31 + username.hashCode();
    }
}
