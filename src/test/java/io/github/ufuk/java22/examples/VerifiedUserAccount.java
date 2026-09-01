package io.github.ufuk.java22.examples;

public class VerifiedUserAccount extends UserAccount {

    private final String nationalId;

    public VerifiedUserAccount(String rawUsername, String rawEmail, String rawNationalId) {
        // Statements executed BEFORE super(...) (introduced as preview in Java 22, JEP 447)
        if (rawUsername == null || rawUsername.isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (rawEmail == null || !rawEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid email address: " + rawEmail);
        }
        if (rawNationalId == null || rawNationalId.length() != 11) {
            throw new IllegalArgumentException("National ID must be exactly 11 digits");
        }

        String sanitizedUsername = rawUsername.trim().toLowerCase();
        String sanitizedEmail = rawEmail.trim().toLowerCase();

        super(sanitizedUsername, sanitizedEmail);

        this.nationalId = rawNationalId.trim();
    }

    public String getNationalId() {
        return nationalId;
    }

}
