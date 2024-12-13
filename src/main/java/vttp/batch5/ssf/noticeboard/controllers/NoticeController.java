package vttp.batch5.ssf.noticeboard.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.models.NoticeResponse;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers

@Controller
@RequestMapping
public class NoticeController {

    private final NoticeService noticeService;
    
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/")
    public String showLandingPage(Model model) {
        Notice notice = new Notice();
        model.addAttribute("notice", notice);
        return "notice";
    }

    @PostMapping("/notice")
    public String postNoticeForm(@Valid @ModelAttribute("notice") Notice entity, BindingResult results, Model model) {
        if (results.hasErrors()) {
            return "notice";
        }

        Notice newNotice = new Notice(entity.getTitle(), entity.getPoster(), entity.getPostDate(), entity.getCategories(), entity.getText());

         // send the valid notice details to service -> service will call the REST end point using rest template
        // need to create a page for successful notice
        try {

            NoticeResponse noticeResponse = noticeService.postToNoticeServer(newNotice);
            model.addAttribute("noticeResponse", noticeResponse);
            
            return "successNotice";


        } catch (Exception e) {
            
            // add error message to the model
            model.addAttribute("error",e.getMessage());
            return "errorNotice";
        }
       

        // i need to get a response back -> return the NoticeResponse so i can show the id and time stamp?
        // if exception -> another DTO?

    }

    

}
