package pti.sb_squash_mvc.controller;

import java.util.ArrayList;
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
    public String loadLoginPage() {

        return "login.html";
    }

    @PostMapping("/login")
    public String checkLogin(Model model,
            @RequestParam(name = "name") String uName,
            @RequestParam(name = "password") String password) {

        String returnPage = "";

        Database db = new Database();
        User user = db.userExists(uName, password);

        if (user == null) {

            returnPage = "login.html";
            model.addAttribute("error", "User doesn't exist!");

        } else {
            if (user.isNewuser() == true) {

                returnPage = "changepassword.html";
                model.addAttribute("user", user);

            } else {
                List<Game> matchList = db.getAllMatches();

                user.setLoggedin(true);
                db.updateUser(user);

                returnPage = "index.html";

                model.addAttribute("user", user);
                model.addAttribute("matchList", matchList);
            }
        }

        db.closeDb();

        return returnPage;
    }

    @PostMapping("/changepwd")
    public String changepwd(
            Model model,
            @RequestParam(name = "uid") int uId,
            @RequestParam(name = "password") String pwd
    ) {
        String msg = "";
        Database db = new Database();

        db.pwdChange(uId, pwd);
        msg = "Password was changed.";
        db.closeDb();

        model.addAttribute("uid", uId);
        model.addAttribute("message", msg);

        return "index.html";
    }
    
    @GetMapping("/searchbycourt")
    public String searchByCourt(
    		Model model,
    		@RequestParam(name = "uid") int uId,
    		@RequestParam(name = "cid") int cId
    		) {
    	List<Game> gameList = new ArrayList<>();
    	Database db = new Database();
    	gameList = db.getGamesByCourt(cId);
    	db.closeDb();
    	
    	model.addAttribute("uid", uId);
    	model.addAttribute("gamelist", gameList);
    	
    	return "index.html";
    }
    
    
    
    
    
    

}
