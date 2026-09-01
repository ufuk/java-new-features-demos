package io.github.ufuk.java22.examples;

import java.util.Objects;

public class UserAccount {

    private final String username;
    private final String email;

    public UserAccount(String username, String email) {
        this.username = Objects.requireNonNull(username, "username cannot be null");
        this.email = Objects.requireNonNull(email, "email cannot be null");
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

}
