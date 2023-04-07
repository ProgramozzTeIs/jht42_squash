package pti.sb_squash_mvc.db;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

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


	
	public void closeDb() {
		
		sessionFactory.close();
	}
}