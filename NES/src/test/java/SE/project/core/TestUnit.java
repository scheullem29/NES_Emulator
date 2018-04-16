package SE.project.core;

import org.junit.runner.*;
import org.junit.runner.notification.*;
/**
 *
 * @author Tony
 */
public class TestUnit
{

    public static void main(String[] args) 
    {
        Result r = JUnitCore.runClasses(CPUTest.class);
        
        for (Failure failure : r.getFailures()) 
        {
            System.out.println(failure.toString());
        }
		
        System.out.println(r.wasSuccessful());
    }
    
}
