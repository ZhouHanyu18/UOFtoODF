package object_set;

public class Base64Decoder {

	public static byte[] base64Dec(String base64Str) {
		base64Str.replace(" ","");   //for EIOffice. Still some problems.
		base64Str.replace("\n",""); 
		
		int length = base64Str.length();
		if (length%4 != 0) {
			System.out.print("Error: Bad base64 String!");
		}
		int chunkNum = length/4;
		byte[] base64Bytes = base64Str.getBytes();
		byte[] resultBytes = new byte[chunkNum * 3];
		byte[] chunk = new byte[4];  //base64 4-bytes
		int baseIndex = 0;
		int resultIndex = 0;	
		while(baseIndex + 4 < length)
		{
			chunk[0] = getBase64Value(base64Bytes[baseIndex++]); 
			chunk[1] = getBase64Value(base64Bytes[baseIndex++]); 
			chunk[2] = getBase64Value(base64Bytes[baseIndex++]); 
			chunk[3] = getBase64Value(base64Bytes[baseIndex++]); 
			
			resultBytes[resultIndex++] = (byte)(((chunk[0] << 2)) | (chunk[1] >> 4)); 
			resultBytes[resultIndex++] = (byte)((chunk[1] << 4) | (chunk[2] >> 2)); 
			resultBytes[resultIndex++] = (byte)((chunk[2] << 6) | (chunk[3]));
		}
		return resultBytes;
	}
	
	private static byte getBase64Value(byte ch)
	{
		if ((ch >= 'A') && (ch <= 'Z')) 
			return (byte)(ch - 'A'); 
		if ((ch >= 'a') && (ch <= 'z')) 
			return (byte)(ch - 'a' + 26); 
		if ((ch >= '0') && (ch <= '9')) 
			return (byte)(ch - '0' + 52); 
		if (ch == '+')
			return 62; 
		if (ch == '/') 
			return 63; 
		if (ch == '=')
			return 0;
		return 0;
	}
	
	
}
