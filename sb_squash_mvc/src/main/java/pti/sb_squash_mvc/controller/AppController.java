package pti.sb_squash_mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pti.sb_squash_mvc.db.Database;

@Controller
public class AppController {

    
    @GetMapping("/")
    public String loadLoginPage(){
        
        return "login.html";
    }
    
    
}
