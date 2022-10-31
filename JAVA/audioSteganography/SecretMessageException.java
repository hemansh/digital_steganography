package audioSteganography;

public class SecretMessageException extends Exception{

	public SecretMessageException()
	{
		super("SecretMessageException");
	}
	
	public SecretMessageException(String message)
	{
		super(message);
	}
}
