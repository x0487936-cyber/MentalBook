package android.app.src.main.java.com.mentalbook.app;

/**
 * Message model for chat messages
 */
public class Message {
    public static final int TYPE_USER = 0;
    public static final int TYPE_BOT = 1;

    private String text;
    private int type;
    private String time;

    public Message(String text, int type, String time) {
        this.text = text;
        this.type = type;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

