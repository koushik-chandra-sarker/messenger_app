package bd.edu.seu.messengerapp.Firebase.Entity;

public class Message {
    String userId, message, messageId;
    Long timestamp;

    public Message() {
    }

    public Message(String userId, String message, Long timestamp) {
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Message(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Message(String userId, String message, String messageId, Long timestamp) {
        this.userId = userId;
        this.message = message;
        this.messageId = messageId;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
