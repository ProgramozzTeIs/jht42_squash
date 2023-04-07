package pti.sb_squash_mvc.db;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Database {

	private SessionFactory sessionFactory;

	public Database() {

		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures settings from
																							// hibernate.cfg.xml
				.build();

		sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}
	
	
	public boolean userExists(String newName) {
		
		boolean result = false;
		
		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		
		Query query = session.createNativeQuery("SELECT * FROM users");
		List<Object[]> rows = query.getResultList();
		
		for(int i = 0; i < rows.size(); i++) {
			
			String existingUsername = rows.get(i)[1].toString();
			if(newName.equals(existingUsername)) {
				result = true;
				
			}
	
		}
		
		tr.commit();
		session.close();
		
		return result;
	}


	
	public void closeDb() {
		
		sessionFactory.close();
	}
}