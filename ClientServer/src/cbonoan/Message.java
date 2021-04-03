package cbonoan;

import java.io.Serializable;

public class Message implements Serializable {
    private MessageType type;
    private String name;
    private String msg;

    public Message(MessageType type, String name, String msg) {
        this.type = type;
        this.name = name;
        this.msg = msg;
    }

    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return String.format("{Type: %-15s|Name: %-20s|Message: %-100s", this.getType(), this.getName(), this.getMsg());
    }
}
