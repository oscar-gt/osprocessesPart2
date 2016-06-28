/**
 * @author Oscar Garcia-Telles
 *
 * There's a separate folder with the ThreadOS .class files
 *
 */

public class Shell extends Thread 
{
	// Private fields
	int promptNumber;		// When promting Shell[n]%
	

	// Default constructor
    public Shell()  
    {
    	// Initialize fields
    	promptNumber = 5;
    	
    	
    }
    
    // Run method 
    public void run()
    {
    	boolean terminate = false;
    	shellPrompt(promptNumber);
    	
    	

    	if(terminate)
    	{
    		SysLib.exit();		// Exiting
        	return;
    	}
    }
    
    // Displays shell prompt: Shell[n]%
    // where n is the nth command line
    public void shellPrompt(int n)
    {
    	SysLib.cout( "Shell[" + n + "]% ");  // using SysLib vs System.out
//    	SysLib.exit();                    // (see SysLib.java)
//    	return;
    }

    /**
     * @param args
     */
//    public static void main(String[] args) 
//    {
//        // TODO Auto-generated method stub
//        Shell shell = new Shell();
//        shell.run();
//    }
}