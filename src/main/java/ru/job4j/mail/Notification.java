package ru.job4j.mail;

public class Notification {
    private final String subjectTemplate = "Notification {username} to email {email}";
    private final String bodyTemplate = "Add a new event to {username}";
    private final String subject;
    private final String body;
    private final String email;

    public Notification(User user) {
        this.subject = createNotification(user, subjectTemplate);
        this.body = createNotification(user, bodyTemplate);
        this.email = user.getEmail();
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getEmail() {
        return email;
    }

    private String createNotification(User user, String template) {
        return template.replaceAll("\\{username}", user.getUsername()).replaceAll("\\{email}", user.getEmail());
    }
}
