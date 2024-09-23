package guru.qa.niffler.utils.templates;

public enum Messages {
    BAD_CREDENTIALS("Bad credentials"),
    SUCCES_REGISTER("Congratulations! You've registered!"),
    ALREADY_EXIST("` already exists"),
    USERNAME("Username `");
    private String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}