package vttp.batch5.ssf.noticeboard.models;

public class NoticeResponse {

    private String id;
    private String message;
    private Long timestamp;

    

    public NoticeResponse() {
    }

    
    public NoticeResponse(String id, Long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }


    public NoticeResponse(String message) {
        this.message = message;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    

    
    
}
