package pti.sb_squash_mvc.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pti.sb_squash_mvc.db.Database;
import pti.sb_squash_mvc.model.Match;
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
                List<Match> matchList= db.getAllMatches();
                
                for (int index = 0; index < matchList.size(); index++) {
                    
                    int player1Id = matchList.get(index).getPlayer1Id();
                    int player2Id = matchList.get(index).getPlayer2Id();
                    int courtId = matchList.get(index).getCourtId();
                    
                    Match currentMatch = matchList.get(index);
                    
                    currentMatch.setPlayer1(db.getUserById(player1Id));
                    currentMatch.setPlayer2(db.getUserById(player2Id));
                    currentMatch.setCourt(db.getCourtById(courtId));
                    
                }
                
                returnPage = "index.html";
                model.addAttribute("matchList", matchList);
            }
        }
        
        return returnPage;
    }
    
    
}
