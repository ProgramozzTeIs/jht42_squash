package pti.sb_squash_mvc.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import pti.sb_squash_mvc.model.Court;
import pti.sb_squash_mvc.model.User;

public class Database {
	
	private SessionFactory sessionFactory; 
	
	public Database() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
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
	
	
	
	
	public void closeDb() {
		sessionFactory.close();
	}

	
}