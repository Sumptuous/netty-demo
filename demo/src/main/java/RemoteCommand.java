import java.io.Serializable;
import java.util.List;

/**
 * Created by lotus on 2017/5/5.
 */
public class RemoteCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    private String clientId;
    private String message;
    private RemoteCommandType type;
    private List<MessageType> messageTypeList;

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

    public List<MessageType> getMessageTypeList() {
        return messageTypeList;
    }

    public void setMessageTypeList(List<MessageType> messageTypeList) {
        this.messageTypeList = messageTypeList;
    }
}
