package edu.unsw.comp9321;


import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.sql.DataSource;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.DerbyDAOImpl;
import edu.unsw.comp9321Ass2.jdbc.UserDTO;

public class CastDAOImplTest extends TestCase{

	public  CastDAO cast;
	
	public CastDAOImplTest(){
		super();
	}
	
	@Before
	public void setUp() throws Exception {
		// To test JNDI outside Tomcat, we need to set these
		 // values manually ... (just for testing purposes)
		 System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
            "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, 
            "org.apache.naming");            

        // Create InitialContext with java:/comp/env/jdbc
        InitialContext ic = new InitialContext();

        ic.createSubcontext("java:comp");
        ic.createSubcontext("java:comp/env");
        ic.createSubcontext("java:comp/env/jdbc");
        
        Reference ref = new Reference("javax.sql.DataSource", "org.apache.commons.dbcp.BasicDataSourceFactory", null);
        ref.add(new StringRefAddr("driverClassName", "org.apache.derby.jdbc.ClientDriver"));
        //TODO: change the following to your own setting
        ref.add(new StringRefAddr("url", "jdbc:derby://localhost:1527/ass2"));
        ref.add(new StringRefAddr("username", "user"));
        ref.add(new StringRefAddr("password", "123"));
        ic.bind("java:comp/env/jdbc/cs9321", ref);
		cast = new DerbyDAOImpl();
		
	}
	
	public void testFindAll(){
		List<UserDTO> castList = cast.findAllUser();
		assertNotNull(castList);
		assertTrue(castList.size()>0);
	}
	
    public void tearDown() throws Exception {
        // To test JNDI outside Tomcat, we need to set these
        // values manually ... (just for testing purposes)
      System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
          "org.apache.naming.java.javaURLContextFactory");
      System.setProperty(Context.URL_PKG_PREFIXES, 
          "org.apache.naming");            

      // Create InitialContext with java:/comp/env/jdbc
      InitialContext ic = new InitialContext();

      ic.unbind("java:comp/env/jdbc/cs9321");
      
      ic.destroySubcontext("java:comp/env/jdbc");
      ic.destroySubcontext("java:comp/env");
      ic.destroySubcontext("java:comp");
       
   }

}
