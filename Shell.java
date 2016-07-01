/**
 * @author Oscar Garcia-Telles
 *
 * There's a separate folder with the ThreadOS .class files
 *
 */

public class Shell extends Thread 
{
	// Private fields
	private int promptNumber;			// When promting Shell[n]%
	private StringBuffer lineOfInput;	// Entire line of input
	private int activeThreads;
	private int greatestThread;
	

	// Default constructor
    public Shell()  
    {
    	// Initialize fields
    	promptNumber = 0;
    	lineOfInput = new StringBuffer();
    	activeThreads = 0;
    	greatestThread = 0;
    	
    	
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
    		//SysLib.cin(lineOfInput);
    		getLineToProcess();
    		buffLength = getInputLength();
    		
    		// Storing string buffer as a string
    		// to break up into commands and arguments.
    		lineToProcess = strBuff.toString();
    		
    		// Process string and execute commands
    		//processLineOfInput(strBuff);
    		processLineOfInput();
    		
    		if(verbose)
    		{
    			SysLib.cout("line of input:  " + lineToProcess + "\n");
    		}
    		
    		// Emptying buffer for next line of input
    		clearInput();
    		SysLib.cout("BEFORE new shell prompt, greatest thread:  ");
    		SysLib.cout(getGreatestThread() + "\n");
    		shellPrompt();
    		
    		
    	}
    	
    	

    }
    
    // Processes entire line of input. Line of input 
    // can have numerous commands/args separated by 
    // & (run concurrently) or ; (run sequentially)
    public void processLineOfInput()
    {
    	boolean verbose = false;
    	int currTid = 0;
    	// Used to see if next command will be run sequentially
    	boolean runSequential = false;
    	
    	// String that stores data for one command
    	// and its arguments
    	String currentCommand = "";
    	int inputLength = getInputLength();
    	
    	// Current and child thread id number
    	int threadID = 1;
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
    		currentChar = lineOfInput.charAt(i);
    		
    		// Checking for delimeters "&", ";"
   
    		// Checking if current and next
    		// commands must be run sequentially
    		if(currentChar == ';' || currentChar == '&' || i == lastIndex)
    		{
    			// First checking if we're at the end of the 
    			// input string buffer
    			if(currentChar == ';' || currentChar == '&')
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
    			currentCommand = lineOfInput.substring(startOfCmd, endOfCmd);
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
    				incrThreadNum();
    				SysLib.cout("*** Seq exec() call. Id/command: " + threadID + "/" + currentCommand + "\n");
    				activeThreads = activeThreads + 1;
    				
    				
    			}
    			// Else not running sequentially.
    			else
    			{
    				threadID = SysLib.exec(cmdArgs);
    				incrThreadNum();
    				SysLib.cout("*** Concurrent exec() call. Id/command: " + threadID + "/" + currentCommand + "\n");
    				activeThreads = activeThreads + 1;
    				
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
    		
    		if(verbose == true)
    		{
    			SysLib.cout("Current threadID:  " + threadID 
    					+ ", for command:  " + currentCommand
    					+ ", with childID:  " + childThread);
    			SysLib.cout("\n");
    		}
    		
    
    	}// End of for loop


    	int currThread = SysLib.join();
    	SysLib.cout("join() == " + currThread + ", threadID == " + threadID + "\n");
    	//while(currThread != getGreatestThread())
    	while(currThread != threadID)
    	{
    		currThread = SysLib.join();
    		
    		SysLib.cout("waiting for termination, join() == " + currThread 
    					+ ", threadID == " + threadID + "\n");
    	}
    	
    	
    	
    }
    
    // Gets entire line of input from command line
    private void getLineToProcess()
    {
    	// Receiving an entire line of input
		SysLib.cin(lineOfInput);
		
    }
    
    // Returns the length of input
    private int getInputLength()
    {
    	return lineOfInput.length();
    }
    
    // Clears contents of buffer to process
    // next line of input
    private void clearInput()
    {
    	int len = getInputLength();
    	lineOfInput.delete(0, len);
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
    
    private void incrThreadNum()
    {
    	greatestThread = greatestThread + 1;
    }
    
    private int getGreatestThread()
    {
    	return greatestThread;
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