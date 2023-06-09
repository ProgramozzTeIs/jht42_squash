package pti.sb_squash_mvc.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pti.sb_squash_mvc.db.Database;
import pti.sb_squash_mvc.model.Court;
import pti.sb_squash_mvc.model.Game;
import pti.sb_squash_mvc.model.Roles;
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
                List<Game> gameList = db.getAllGames();
                List<User> userList = db.getAllUsers();
                List<Court> courtList = db.getAllCourts();

                user.setLoggedin(true);
                db.updateUser(user);

                if (user.getRole().equals(Roles.ADMIN)) {
                    returnPage = "admin.html";
                    
                    model.addAttribute("user", user);
                    model.addAttribute("userList", userList);
                    model.addAttribute("courtList", courtList);

                } else {
                    returnPage = "index.html";

                    model.addAttribute("user", user);
                    model.addAttribute("gameList", gameList);
                }

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

        User user = db.getUserById(uId);
        List<Game> gameList = db.getAllGames();
        List<User> userList = db.getAllUsers();
        List<Court> courtList = db.getAllCourts();

        db.closeDb();

        model.addAttribute("user", user);
        model.addAttribute("gameList", gameList);
        model.addAttribute("userList", userList);
        model.addAttribute("courtList", courtList);
        model.addAttribute("message", msg);

        return "index.html";
    }

    @GetMapping("/searchbycourt")
    public String searchByCourt(
            Model model,
            @RequestParam(name = "userId") int uId,
            @RequestParam(name = "courtId") int cId
    ) {
        List<Game> gameList = new ArrayList<>();
        Database db = new Database();
        User user = db.getUserById(uId);
        String site = "";
        if (user.isLoggedin()) {

            gameList = db.getGamesByCourt(cId);
            List<User> userList = db.getAllUsers();
            List<Court> courtList = db.getAllCourts();
            db.closeDb();

            model.addAttribute("user", user);
            model.addAttribute("gameList", gameList);
            model.addAttribute("userList", userList);
            model.addAttribute("courtList", courtList);

            site = "index.html";
        } else {
            site = "login.html";
        }

        return site;
    }

    @PostMapping("/logout")
    public String logout(
            Model model,
            @RequestParam(name = "uid") int uId) {

        String msg = "";
        Database db = new Database();
        User user = db.getUserById(uId);
        user.setLoggedin(false);
        db.updateUser(user);
        msg = "successful logout";
        db.closeDb();

        model.addAttribute("message", msg);

        return "login.html";
    }

    @GetMapping("/searchbyuser")
    public String searchByUser(
            Model model,
            @RequestParam(name = "searched_userid") int searchedUserId,
            @RequestParam(name = "loggedin_userid") int loggedInUserId
    ) {

        Database db = new Database();
        List<Game> filteredList = db.getGamesByUserId(searchedUserId);
        User loggedInUser = db.getUserById(loggedInUserId);
        String site = "";

        if (loggedInUser.isLoggedin()) {

            List<User> userList = db.getAllUsers();
            List<Court> courtList = db.getAllCourts();

            db.closeDb();

            model.addAttribute("user", loggedInUser);
            model.addAttribute("gameList", filteredList);
            model.addAttribute("userList", userList);
            model.addAttribute("courtList", courtList);
            site = "index.html";
        } else {
            site = "login.html";
        }

        return site;
    }

    @GetMapping("/admin")
    public String createGame(
            Model model,
            @RequestParam(name = "loggedin_userid") int loggedInUserId
    ) {

        Database db = new Database();

        List<User> userList = db.getAllUsers();
        List<Court> courtList = db.getAllCourts();

        db.closeDb();

        model.addAttribute("userList", userList);
        model.addAttribute("courtList", courtList);

        return "admin.html";
    }

    @PostMapping("/addcourt")
    public String createCourt(
            Model model,
            @RequestParam(name = "courtname") String name,
            @RequestParam(name = "courtaddress") String address,
            @RequestParam(name = "courtrent") int rent,
            @RequestParam(name = "loggedin_userid") int adminId
    ) {
        Court newCourt = new Court();
        String msg = "";

        Database db = new Database();
        User admin = db.getUserById(adminId);

        if (admin.isLoggedin()) {
            newCourt.setName(name);
            newCourt.setAddress(address);
            newCourt.setRent(rent);
            db.insertCourt(newCourt);
            msg = "Success insert court.";

        } else {
            msg = "Admin isn't logged in!";
        }

        List<User> userList = db.getAllUsers();
        List<Court> courtList = db.getAllCourts();
        db.closeDb();

        model.addAttribute("msg", msg);
        model.addAttribute("userList", userList);
        model.addAttribute("courtList", courtList);
        model.addAttribute("user", admin);

        return "admin.html";
    }

    @PostMapping("/addgame")
    public String createGame(
            Model model,
            @RequestParam(name = "loggedin_adminid") int adminId,
            @RequestParam(name = "player1Id") int player1Id,
            @RequestParam(name = "player2Id") int player2Id,
            @RequestParam(name = "courtId") int courtId,
            @RequestParam(name = "scoreplayer1") int sP1,
            @RequestParam(name = "scoreplayer2") int sP2,
            @RequestParam(name = "gamedate") String gamedate
    ) {

    	String targetPage = "admin.html";
    	Database db = new Database();
    	User user = db.getUserById(adminId);
    	
    	/** LOGGED IN? */
    	if(user.isLoggedin() == true) {
    		
	        Game newGame = new Game();
	        String msg = "";
	
	        newGame.setPlayer1Id(player1Id);
	        newGame.setPlayer2Id(player2Id);
	        newGame.setCourtId(courtId);
	        newGame.setScorePlayer1(sP1);
	        newGame.setScorePlayer2(sP2);
	        
	        gamedate += ":00";
	        newGame.setGamedate( LocalDateTime.parse(gamedate) );
	
	        
	        if (player1Id != player2Id) {
	
	            db.createNewGame(newGame);
	        } else {
	
	            msg = "Nem lehet ugyan az a két játékos";
	
	        }
	
	        model.addAttribute("msg", msg);
	        
	        
	        
	        List<Game> gameList = db.getAllGames();
	        List<User> userList = db.getAllUsers();
	        List<Court> courtList = db.getAllCourts();
	        model.addAttribute("user", user);
	        model.addAttribute("userList", userList);
	        model.addAttribute("courtList", courtList);
	        model.addAttribute("gameList", gameList);
    	}
    	else {
    		
    		targetPage = "login.html";
    	}
        
        db.closeDb();

        return targetPage;
    }

    @GetMapping("/showgames")
    public String showgames(
            Model model,
            @RequestParam(name = "uid") int uId
    ) {
        Database db = new Database();
        User user = db.getUserById(uId);
        String targetPage = "index.html";
        
        /** LOGGED IN? */
        if(user.isLoggedin() == true) {
	        List<Game> gameList = db.getAllGames();
	        List<User> userList = db.getAllUsers();
	        List<Court> courtList = db.getAllCourts();
	
	        
	        model.addAttribute("user", user);
	
	        model.addAttribute("userList", userList);
	        model.addAttribute("courtList", courtList);
	        model.addAttribute("gameList", gameList);
        }
        else {
        	
        	targetPage = "login.html";
        }

        
        return targetPage;
    }

    @PostMapping("/adduser")
    public String addUser(
            Model model,
            @RequestParam(name = "username") String name,
            @RequestParam(name = "loggedin_adminid") int adminId
    ) {
        User newUser = new User();
        String msg = "";
        Random random = new Random();
        int pwd = random.nextInt(10000) + 1001;
        String s = String.valueOf(pwd);
        Database db = new Database();
        User admin = db.getUserById(adminId);

        if (admin.isLoggedin()) {

            newUser.setName(name);
            newUser.setPwd(s);
            newUser.setRole(Roles.PLAYER);
            newUser.setNewuser(true);
            db.addUser(newUser);
            msg = "Add new user";

        } else {
            msg = "logged in!";
        }

        List<User> userList = db.getAllUsers();
        List<Court> courtList = db.getAllCourts();
        db.closeDb();

        model.addAttribute("user", admin);
        model.addAttribute("msg", msg);
        model.addAttribute("userList", userList);
        model.addAttribute("courtList", courtList);

        return "admin.html";
    }

}
