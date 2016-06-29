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
    	promptNumber = 0;
    	
    	
    }
    
    // Run method 
    public void run()
    {  	
    	boolean terminate = false;
    	boolean verbose = false;
    	
    	// Line of input stored in a str buffer
    	StringBuffer strBuff = new StringBuffer();
    	int buffLength = 0;
    	
    	String lineToProcess = "";
    	
    	// Initial shell prompt
    	shellPrompt();
    	
    	// Loop to accept user input
    	while(true)
    	{
    		// Receiving an entire line of input
    		SysLib.cin(strBuff);
    		buffLength = strBuff.length();
    		
    		// Storing string buffer as a string
    		// to break up into commands and arguments.
    		lineToProcess = strBuff.toString();
    		
    		// Process string and execute commands
    		processLineOfInput(strBuff);
    		
    		if(verbose)
    		{
    			SysLib.cout("line of input:  " + lineToProcess + "\n");
    		}
    		
    		// Emptying buffer for next line of input
    		strBuff.delete(0, buffLength);
    		shellPrompt();
    		
    		
    	}
    	
    	

    }
    
    // Processes entire line of input. Line of input 
    // can have numerous commands/args separated by 
    // & (run concurrently) or ; (run sequentially)
    public void processLineOfInput(StringBuffer input)
    {
    	// Used to see if next command will be run sequentially
    	boolean runSequential = false;
    	
    	// String that stores data for one command
    	// and its arguments
    	String currentCommand = "";
    	int inputLength = input.length();
    	
    	// Current and child thread id number
    	int threadID = 0;
    	int childThread = 0;
    	
    	// Index of first, last char in current command
    	int startOfCmd = 0;
    	int endOfCmd = 0;	
    	char currentChar;
    	
    	int lastIndex = inputLength - 1;
    	
    	// Read buffer up until "&" or ";" is 
    	// encountered. Process this command.
    	// Done when all chars have been looked at.
    	for(int i = 0; i < inputLength; i++)
    	{
    		currentChar = input.charAt(i);
    		
    		// Checking for delimeters "&", ";"
   
    		// Checking if current and next
    		// commands must be run sequentially
    		if(currentChar == ';' || currentChar == '&' || i == lastIndex)
    		{
    			// First checking if we're at the end of the 
    			// input string buffer
    			if(currentChar == ';' || currChar == '&')
    			{
    				endOfCmd = i - 1;
    			}
    			else
    			{
    				endOfCmd = i;
    			}
    
    			
    			// Make sure endOfCmd is not negative
    			if(endOfCmd < 0)
    			{
    				SysLib.cout("endOfCmd is negative \n");
    				break;
    			}
    			
    			// Store string command up to current index
    			currentCommand = input.substring(startOfCmd, endOfCmd);
    			// Updating start of next command, should be next index
    			startOfCmd = i + 1;
    			
    			// Creating argument array and executing
    			String[] cmdArgs = SysLib.stringToArgs(currentCommand);
    			
    			// First need to check if we need 
    			// to wait for the previous execution to terminate
    			if(runSequential)
    			{
    				childThread = SysLib.join();
    				// Waiting for previous child to terminate...
    				while(childThread != threadID)
    				{
    					childThread = SysLib.join();
    				}
    				// At this point, previous child process
    				// has terminated. Can execute next command
    				threadID = SysLib.exec(cmdArgs);
    			}
    			// Else not running sequentially.
    			else
    			{
    				threadID = SysLib.exec(cmdArgs);
    			}
    			
    			// Checking if next call will be
    			// sequentially run.
    			if(currentChar == ';')
    			{
    				runSequential = true;
    			}
    			else
    			{
    				runSequential = false;
    			}
    			
    			
    		} // End of if( "&" || ";" || endOfBuffer)
    		
    	}// End of for loop
    	
    	
    	
    }
    
    // Displays shell prompt: Shell[n]%, where n is the 
    // nth command line. Since n increases everytime, 
    // we increment the prompt number everytime shellPrompt()
    // is called (initialized as 0)
    public void shellPrompt()
    {
    	incrPromptNumber();
    	int pnum = getPromptNumber();
    	SysLib.cout( "Shell[" + pnum + "]% ");  // using SysLib vs System.out
//    	SysLib.exit();                    // (see SysLib.java)
//    	return;
    }
    
    private void incrPromptNumber()
    {
    	promptNumber = promptNumber + 1;
    }
    
    public void displayStrArr(String[] input)
    {
    	int len = input.length;
    	for(int i = 0; i < len; i++)
    	{
    		SysLib.cout(input[i] + " ");
    	}
    	SysLib.cout("\n");
    }
    
    
    public int getPromptNumber()
    {
    	return promptNumber;
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