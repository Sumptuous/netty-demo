/**
 * Created by Administrator on 2017/5/5.
 */
public class Response extends BaseMsg {
    private String content;

    public Response() {
        super();
        setType(MsgType.PING);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
