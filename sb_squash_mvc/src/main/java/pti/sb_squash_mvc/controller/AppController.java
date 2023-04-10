package pti.sb_squash_mvc.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pti.sb_squash_mvc.db.Database;
import pti.sb_squash_mvc.model.Game;
import pti.sb_squash_mvc.model.User;


@Controller
public class AppController {

    
    @GetMapping("/")
    public String loadLoginPage(){
        
        return "login.html";
    }
    
    @PostMapping("/login")
    public String checkLogin(Model model,
            @RequestParam(name = "name") String uName,
            @RequestParam(name = "password") String password){
        
        String returnPage = "";
        
        Database db = new Database();
        User user = db.userExists(uName, password);
        
        
        if(user == null){
            returnPage = "login.html";
            model.addAttribute("error", "User doesn't exist!");
        } else {
            if(user.isNewuser() == true){
                returnPage = "changepassword.html";
                model.addAttribute("user", user);
            } else {
                List<Game> matchList= db.getAllMatches();
                
                returnPage = "index.html";
                model.addAttribute("matchList", matchList);
            }
        }
        
        db.closeDb();
        
        return returnPage;
    }
    
    
}
