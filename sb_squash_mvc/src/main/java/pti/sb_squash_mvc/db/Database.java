package pti.sb_squash_mvc.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import pti.sb_squash_mvc.model.Court;
import pti.sb_squash_mvc.model.Game;
import pti.sb_squash_mvc.model.User;

public class Database {


	private SessionFactory sessionFactory;

	public Database() {

		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures settings from
																							// hibernate.cfg.xml
				.build();

		sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}
	
	
	public User userExists(String name, String pw) {
		
		User currentUser = null;
		
		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		
		Query query = session.createQuery("SELECT u FROM User u");
		List<User> users = query.getResultList();
		

		for(int i = 0; i < users.size(); i++) {
		
			
			
			if( (name.equals(users.get(i).getName())) && (pw.equals(users.get(i).getPwd())) ) {
				
				currentUser = users.get(i);
			}
		}
	
		
		tr.commit();
		session.close();
		
		return currentUser;
	}
	
	public User getUserById(int id) {
		User user = null;
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		user = session.get(User.class, id);
				
		tx.commit();
		session.close();
				
		return user;
	}
	
	public Court getCourtById(int id) {
		Court court = null;
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		court = session.get(Court.class, id);
				
		tx.commit();
		session.close();
				
		return court;
	}
	
	public void pwdChange(int uId, String pwd) {
		
		User user = getUserById(uId);
		user.setPwd(pwd);
		user.setNewuser(false);
		user.setLoggedin(true);
		
		updateUser(user);
		
		
	}
	
	public List<Game> getGamesByCourt(int cId) {
		List<Game> gameList = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		Query query = session.createQuery("SELECT g FROM Game g WHERE g.courtId = ?1");
		query.setParameter(1, cId);
		gameList = query.getResultList();
		
		gameList = loadUsersAndCourtForGames(gameList);
		
		
		tx.commit();
		session.close();
		
		return gameList;
	}

	
	public void closeDb() {
		
		sessionFactory.close();
	}

    public List<Game> getAllGames() {
       
    	List<Game> games = null;
    	
    	Session session = sessionFactory.openSession();
    	Transaction tx = session.beginTransaction();
    	
    	Query query = session.createQuery("SELECT g FROM Game g ORDER BY g.gamedate");
    	games = query.getResultList();
    	
    	games = loadUsersAndCourtForGames(games);    	
    	
    	tx.commit();
    	session.close();
    	
        
        return games;
    }
    
    
    public List<User> getAllUsers() {
    	
    	List<User> users = null;
    	
    	Session session = sessionFactory.openSession();
    	Transaction tx = session.beginTransaction();
    	
    	Query query = session.createQuery("SELECT u FROM User u");
    	users = query.getResultList();
    	
    	
    	tx.commit();
    	session.close();
    	
    	return users;
    }
    
    
    public List<Court> getAllCourts() {
    	
    	List<Court> courts = null;
    	
    	Session session = sessionFactory.openSession();
    	Transaction tx = session.beginTransaction();
    	
    	Query query = session.createQuery("SELECT c FROM Court c");
    	courts = query.getResultList();
    	
    	
    	tx.commit();
    	session.close();
    	
    	return courts;
    }
    

    public void updateUser(User user) {
    	
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		session.update(user);
		
		tx.commit();
		session.close();
    	
    }
    
    public void insertCourt(Court court) {
    	
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		session.save(court);
		
		tx.commit();
		session.close();
    	
    }

    public List<Game> getGamesByUserId(int userId) {
        
    	List<Game> games = null;
    	
    	Session session = sessionFactory.openSession();
    	Transaction tx = session.beginTransaction();
    	
    	Query query = session.createQuery("SELECT g FROM Game g WHERE g.player1Id = ?1 OR g.player2Id = ?2");
    	query.setParameter(1, userId);
    	query.setParameter(2, userId);
    	games = query.getResultList();
    	
    	games = loadUsersAndCourtForGames(games);
    	
    	
    	tx.commit();
    	session.close();
    	
    	
        
        return games;
    }


	private List<Game> loadUsersAndCourtForGames(List<Game> gameList) {
		
		
		for (int index = 0; index < gameList.size(); index++) {
    		
    		Game currentGame = gameList.get(index);
    		
    		
    		/** USER1 */
    		User user1 = getUserById(currentGame.getPlayer1Id());
    		currentGame.setPlayer1(user1);
    		
    		/** USER2 */
    		User user2 = getUserById(currentGame.getPlayer2Id());
    		currentGame.setPlayer2(user2);
    		
    		/** COURT */
    		Court court =getCourtById(currentGame.getCourtId());
    		currentGame.setCourt(court);
    	}
		
		
		return gameList;
	}
	
	public void createNewGame(Game newGame) {
		
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
	
		session.save(newGame);

		transaction.commit();
		session.close();

	}
    
	public void addUser (User newUser) {
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		session.save(newUser);
		
		tx.commit();
		session.close();
				
		
	}

}