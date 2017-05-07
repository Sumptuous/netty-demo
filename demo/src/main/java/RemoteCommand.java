import java.io.Serializable;

/**
 * Created by lotus on 2017/5/5.
 */
public class RemoteCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    private String clientId;
    private String message;
    private RemoteCommandType type;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RemoteCommandType getType() {
        return type;
    }

    public void setType(RemoteCommandType type) {
        this.type = type;
    }
}
