package audioSteganography;

public class NotEnoughSpaceException extends Exception{
	public NotEnoughSpaceException() // constructor to create instance of this exception
	{
		super("NotEnoughSpaceException"); 
		// calls original constructor in the Exception class to print the message
	}
	
	public NotEnoughSpaceException(String message) {
		super(message); // Allows custom exception handling message
	}

	public NotEnoughSpaceException(long spaceNeeded, long spaceAvailable )
	{ 
		super("NotEnoughSpaceException: " + spaceNeeded + " bytes is needed and you only have" + spaceAvailable + " bytes available ");
	}
}
