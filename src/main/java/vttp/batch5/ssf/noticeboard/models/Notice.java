package vttp.batch5.ssf.noticeboard.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Notice {

    private String noticeId;

    @NotEmpty(message="Notice title cannot be empty")
    @Size(min = 3, max = 128, message = "Notice title's length must be between 3 and 128 characters.")
    private String title;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid Email format")
    private String poster;

    @NotNull(message = "Cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Must be a future date")
    private Date postDate;

    @NotEmpty(message = "Cannot be empty")
    @Size(min = 1, message = "Must pick at least one category")
    private List<String> categories;

    @NotEmpty(message = "Contents of the notice cannot be empty.")
    private String text;

    
    public Notice() {
    }

    

    public Notice(
            @NotEmpty(message = "Notice title cannot be empty") @Size(min = 3, max = 128, message = "Notice title's length must be between 3 and 128 characters.") String title,
            @NotEmpty(message = "Email cannot be empty") @Email(message = "Invalid Email format") String poster,
            @Future(message = "Must be a future date") Date postDate,
            @NotEmpty(message = "Must picked at least one category") @Size(min = 1) List<String> categories,
            @NotEmpty(message = "Contents of the notice cannot be empty.") String text) {
        
        this.noticeId = UUID.randomUUID().toString();
        this.title = title;
        this.poster = poster;
        this.postDate = postDate;
        this.categories = categories;
        this.text = text;
    }



    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPoster() {
        return poster;
    }
    public void setPoster(String poster) {
        this.poster = poster;
    }
    public Date getPostDate() {
        return postDate;
    }
    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
    public List<String> getCategories() {
        return categories;
    }
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getNoticeId() {
        return noticeId;
    }
    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    


    
    
    
}
