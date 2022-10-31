package audioSteganography;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.io.FileOutputStream;

public class AudioSteganography {
	private static final int START = 50; // set as constant since we always start at 50
	
	public static String decodeMessage(File inputFile) 
			throws SecretMessageException
	{
		RandomAccessFile stream = null;
		String secretMessage = "";
		
		try 
		{
			 stream= new RandomAccessFile(inputFile, "rw");
			 stream.seek(START);
			 // read message size encoded at byte 50
			 int messageLength = stream.readInt();
			 if (messageLength <= 0 || messageLength >= (Byte.MAX_VALUE)) 
				 // to prevent bad index initialization and Java.lang OutOfMemory exception
			 {
				 stream.close();
				 throw new SecretMessageException("SecretMessageException: Secret Message is not found");
			 }
			
			 char[] charArray = new char[messageLength];
			 
			 long nextPosition;
			 for (int i = 0; i < messageLength; i++)
			 {
				charArray[i] = stream.readChar();
				nextPosition = stream.readLong();
				
				if (i < messageLength -1)
				{
					//System.out.println(nextPosition); // for debugging purposes
					stream.seek(nextPosition);	
				}
			 }
			 secretMessage = new String(charArray);	
			
		}
		catch (IOException e)
		{
			System.out.println("IOException: " + e.getMessage());
		}
		 return secretMessage;
	}
	
	public static void encodeMessage(File inputFile, File outputFile, String message) 
			throws NotEnoughSpaceException, SecretMessageException 
	{
		
		try
		{
			// Copy input file to a separate output file
			// Encryption will be done on the copy output file and leave the input file untouched
			FileOutputStream outputStream = new FileOutputStream (outputFile);
			Files.copy(inputFile.toPath(), outputStream);
			
			int messageLength = message.getBytes().length; // instead of message.length, takes other user's systems into consideration.
//			int messageLength = message.length(); // use this line if previous line doesn't work on some systems.

			if (messageLength <= 0 || messageLength >= (Byte.MAX_VALUE)) 
				 // to prevent bad index initialization and Java.lang.OutOfMemory exception
			 {
				 outputStream.close();
				 throw new NotEnoughSpaceException(messageLength, inputFile.length());
			 }
			
			RandomAccessFile out = new RandomAccessFile(outputFile, "rw");
			long[] positionArray = getDataLocations(START + 4, out.length(), messageLength); 
			// size of integer is 4 bytes
			// at byte 50, the int length of the message was written down
			// starting position of the first long is START + 4 
			
			char[] charArray = new char[messageLength];
			out.seek(START); 
			// write length of message at byte 50
			out.writeInt(messageLength);
		
			for (int i = 0; i < messageLength; i++) 
			{
				charArray[i] = message.charAt(i); // fills up charArray with characters from message
				out.seek(positionArray[i]); //move pointer to location in positionArray[i]
				out.writeChar(charArray[i]); // write the char at that position
				//out.writeLong(positionArray[i+1]);
				if (i+1 < messageLength) 
				{

					out.writeLong(positionArray[i+1]);
				}
			}
			out.close();	
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public static long[] getDataLocations(long start, long stop, int numLocations) 
			throws NotEnoughSpaceException
	{
		long[] dataLocation = new long[numLocations]; // numLocations == messageLength
		
		long space = stop - start; // amount of bytes we can fit the message in;
		//System.out.println("start " + start + " & space: " + space + " & stop: " + stop);
		
		if (numLocations > space)
		{
			throw new NotEnoughSpaceException(numLocations, space);
		}
		dataLocation[0] = start; // first Location is the start 
		
		for (int i = 1; i < numLocations; i++) 
		{
			 // use random locations to store characters
			dataLocation[i] = Math.abs(start + ((long) (space *  Math.random()))); // ensure no byte is negative
			
			for (int j = 0; j < i; j++) // uses second for-loop to ensure the array contains only unique long-values
			{
				if (dataLocation[i] == dataLocation[j]) // if current slot contains the same long value as ANY previous slot, 
														//reinitialize current slot with new long
				{
					dataLocation[i] = Math.abs(start + ((long) (space *  Math.random()))); // ensure no byte is negative
				}
			}
		}
//		for (int i = 0; i < dataLocation.length; i++) // prints out the long positions for debugging purposes
//			{
//				System.out.println(dataLocation[i]);
//			}
		return dataLocation;
	}

}
