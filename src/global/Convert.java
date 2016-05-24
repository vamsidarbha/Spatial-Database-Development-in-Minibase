/* file Convert.java */

package global;

import heap.Tuple;

import java.io.*;
import java.lang.*;

import org.apache.commons.lang3.SerializationUtils;




public class Convert{
 
 /**
 * read 4 bytes from given byte array at the specified position
 * convert it to an integer
 * @param  	data 		a byte array 
 * @param       position  	in data[]
 * @exception   java.io.IOException I/O errors
 * @return      the integer 
 */
  public static int getIntValue (int position, byte []data)
   throws java.io.IOException
    {
      InputStream in;
      DataInputStream instr;
      int value;
      byte tmp[] = new byte[4];
      
      // copy the value from data array out to a tmp byte array
      System.arraycopy (data, position, tmp, 0, 4);
      
      /* creates a new data input stream to read data from the
       * specified input stream
       */
      in = new ByteArrayInputStream(tmp);
      instr = new DataInputStream(in);
      value = instr.readInt();  
      
      return value;
    }
  
  /**
   * read 4 bytes from given byte array at the specified position
   * convert it to a float value
   * @param  	data 		a byte array 
   * @param       position  	in data[]
   * @exception   java.io.IOException I/O errors
   * @return      the float value
   */
  public static float getFloValue (int position, byte []data)
    throws java.io.IOException
    {
      InputStream in;
      DataInputStream instr;
      float value;
      byte tmp[] = new byte[4];
      
      // copy the value from data array out to a tmp byte array
      System.arraycopy (data, position, tmp, 0, 4);
      
      /* creates a new data input stream to read data from the
       * specified input stream
       */
      in = new ByteArrayInputStream(tmp);
      instr = new DataInputStream(in);
      value = instr.readFloat();  
      
      return value;
    }
  
  
  /**
   * read 2 bytes from given byte array at the specified position
   * convert it to a short integer
   * @param  	data 		a byte array
   * @param       position  	the position in data[]
   * @exception   java.io.IOException I/O errors
   * @return      the short integer
   */
  public static short getShortValue (int position, byte []data)
    throws java.io.IOException
    {
      InputStream in;
      DataInputStream instr;
      short value;
      byte tmp[] = new byte[2];
      
      // copy the value from data array out to a tmp byte array
      System.arraycopy (data, position, tmp, 0, 2);
      
      /* creates a new data input stream to read data from the
       * specified input stream
       */
      in = new ByteArrayInputStream(tmp);
      instr = new DataInputStream(in);
      value = instr.readShort();
      
      return value;
    }
  
  /**
   * reads a string that has been encoded using a modified UTF-8 format from
   * the given byte array at the specified position
   * @param       data            a byte array
   * @param       position        the position in data[]
   * @param 	length 		the length of the string in bytes
   *			         (=strlength +2)
   * @exception   java.io.IOException I/O errors
   * @return      the string
   */
  public static String getStrValue (int position, byte []data, int length)
    throws java.io.IOException
    {
      InputStream in;
      DataInputStream instr;
      String value;
      byte tmp[] = new byte[length];  
      
      // copy the value from data array out to a tmp byte array
      System.arraycopy (data, position, tmp, 0, length);
      
      /* creates a new data input stream to read data from the
       * specified input stream
       */
      in = new ByteArrayInputStream(tmp);
      instr = new DataInputStream(in);
      value = instr.readUTF();
      return value;
    }
  
  /**
   * reads 2 bytes from the given byte array at the specified position
   * convert it to a character
   * @param       data            a byte array
   * @param       position        the position in data[]
   * @exception   java.io.IOException I/O errors
   * @return      the character
   */
  public static char getCharValue (int position, byte []data)
    throws java.io.IOException
    {
      InputStream in;
      DataInputStream instr;
      char value;
      byte tmp[] = new byte[2];
      // copy the value from data array out to a tmp byte array  
      System.arraycopy (data, position, tmp, 0, 2);
      
      /* creates a new data input stream to read data from the
       * specified input stream
       */
      in = new ByteArrayInputStream(tmp);
      instr = new DataInputStream(in);
      value = instr.readChar();
      return value;
    }
  
  public static Shape_Geometry getShape_Geometry(int position,byte []data)
  throws java.io.IOException, ClassNotFoundException
  {
//	  ByteArrayInputStream in;
//	  ObjectInputStream instr;
//	  Shape_Geometry value;
//	  byte tmp[]=new byte[32];
//	  System.arraycopy (data, position, tmp, 0, i);
//	  //System.out.println(tmp);
//	  for(int j=0;j<tmp.length;j++){
//		  System.out.println(tmp[j]);
//	  }
//	  in = new ByteArrayInputStream(tmp);
//	  instr = new ObjectInputStream(in);
//      value=(Shape_Geometry) instr.readObject();
//      //Shape_Geometry yourObject = (Shape_Geometry) SerializationUtils.deserialize(tmp);
	  int[] n=new int[4];
	  InputStream in;
      DataInputStream instr;
      byte tmp[] = new byte[16];
      System.arraycopy (data, position, tmp, 0, 16);
      in = new ByteArrayInputStream(tmp);
	  instr = new DataInputStream(in);
	  n[0]=instr.readInt();
	  n[1]=instr.readInt();
	  n[2]=instr.readInt();
	  n[3]=instr.readInt();
      
	  int a=Math.abs(n[0]-n[1]);
	  int b=Math.abs(n[2]-n[3]);
	  int m=a*b;
	  Shape_Geometry s=new Shape_Geometry(n[0],n[1],n[2],n[3]);
      return s;
  }
  
 
  /**
   * update an integer value in the given byte array at the specified position
   * @param  	data 		a byte array
   * @param	value   	the value to be copied into the data[]
   * @param	position  	the position of tht value in data[]
   * @exception   java.io.IOException I/O errors
   */
  public static void setIntValue (int value, int position, byte []data) 
    throws java.io.IOException
    {
      /* creates a new data output stream to write data to 
       * underlying output stream
       */
      
      OutputStream out = new ByteArrayOutputStream();
      DataOutputStream outstr = new DataOutputStream (out);
      
      // write the value to the output stream
      
      outstr.writeInt(value);
      
      // creates a byte array with this output stream size and the
      // valid contents of the buffer have been copied into it
      byte []B = ((ByteArrayOutputStream) out).toByteArray();
      
      // copies the first 4 bytes of this byte array into data[] 
      System.arraycopy (B, 0, data, position, 4);
      
    }
  
  public static void setShapeGeoValue (Shape_Geometry value, int position, byte []data) 
		    throws java.io.IOException
		    {
		      /* creates a new data output stream to write data to 
		       * underlying output stream
		       */
		      
//	          ByteArrayOutputStream  out = new ByteArrayOutputStream();
//		      ObjectOutputStream outstr = new ObjectOutputStream (out);
//		      
//		      // write the value to the output stream
//		      
//		      outstr.writeObject(value);
//		      
//		      // creates a byte array with this output stream size and the
//		      // valid contents of the buffer have been copied into it
//		      byte []B = ((ByteArrayOutputStream) out).toByteArray();
//		      
//		      // copies the first 4 bytes of this byte array into data[] 
//		      System.arraycopy (B, 0, data, position, 4);
	  		int[] n=new int[4];
	  		n[0]= value.getX1();
	  		n[1]=value.getX2();
	  		n[2]=value.getY1();
	  		n[3]=value.getY2();
	  		OutputStream out = new ByteArrayOutputStream();
	  		DataOutputStream outstr = new DataOutputStream (out);
	  
	  		for(int i=0;i<n.length;i++){
	  			
	  			
      
      // write the value to the output stream
      
	  			outstr.writeInt(n[i]);
      
      // creates a byte array with this output stream size and the
      // valid contents of the buffer have been copied into it
	  			
      
      // copies the first 4 bytes of this byte array into data[] 
	  			
	  		}
	  		byte []B = ((ByteArrayOutputStream) out).toByteArray();
	  		int n1=outstr.size();
	  		System.arraycopy (B, 0, data, position, n1);
		      
		    }
  
  
  
  /**
   * update a float value in the given byte array at the specified position
   * @param  	data 		a byte array
   * @param	value   	the value to be copied into the data[]
   * @param	position  	the position of tht value in data[]
   * @exception   java.io.IOException I/O errors
   */
  public static void setFloValue (float value, int position, byte []data) 
    throws java.io.IOException
    {
      /* creates a new data output stream to write data to 
       * underlying output stream
       */
      
      OutputStream out = new ByteArrayOutputStream();
      DataOutputStream outstr = new DataOutputStream (out);
      
      // write the value to the output stream
      
      outstr.writeFloat(value);
      
      // creates a byte array with this output stream size and the
      // valid contents of the buffer have been copied into it
      byte []B = ((ByteArrayOutputStream) out).toByteArray();
      
      // copies the first 4 bytes of this byte array into data[] 
      System.arraycopy (B, 0, data, position, 4);
      
    }
  
  /**
   * update a short integer in the given byte array at the specified position
   * @param  	data 		a byte array
   * @param	value   	the value to be copied into data[]
   * @param	position  	the position of tht value in data[]
   * @exception   java.io.IOException I/O errors
   */
  public static void setShortValue (short value, int position, byte []data) 
    throws java.io.IOException
    {
      /* creates a new data output stream to write data to 
       * underlying output stream
       */
      
      OutputStream out = new ByteArrayOutputStream();
      DataOutputStream outstr = new DataOutputStream (out);
      
      // write the value to the output stream
      
      outstr.writeShort(value);
      
      // creates a byte array with this output stream size and the
      // valid contents of the buffer have been copied into it
      byte []B = ((ByteArrayOutputStream) out).toByteArray();
      
      // copies the first 2 bytes of this byte array into data[] 
      System.arraycopy (B, 0, data, position, 2);
      
    }
  
  /**
   * Insert or update a string in the given byte array at the specified 
   * position.
   * @param       data            a byte array
   * @param       value           the value to be copied into data[]
   * @param       position        the position of tht value in data[]
   * @exception   java.io.IOException I/O errors
   */
 public static void setStrValue (String value, int position, byte []data)
        throws java.io.IOException
 {
  /* creates a new data output stream to write data to
   * underlying output stream
   */
 
   OutputStream out = new ByteArrayOutputStream();
   DataOutputStream outstr = new DataOutputStream (out);
   
   // write the value to the output stream
   
   outstr.writeUTF(value);
   // creates a byte array with this output stream size and the 
   // valid contents of the buffer have been copied into it
   byte []B = ((ByteArrayOutputStream) out).toByteArray();
   
   int sz =outstr.size();  
   // copies the contents of this byte array into data[]
   System.arraycopy (B, 0, data, position, sz);
   
 }
  
  /**
   * Update a character in the given byte array at the specified position.
   * @param       data            a byte array
   * @param       value           the value to be copied into data[]
   * @param       position        the position of tht value in data[]
   * @exception   java.io.IOException I/O errors
   */
  public static void setCharValue (char value, int position, byte []data)
    throws java.io.IOException
    {
      /* creates a new data output stream to write data to
       * underlying output stream
       */
      
      OutputStream out = new ByteArrayOutputStream();
      DataOutputStream outstr = new DataOutputStream (out);
      
      // write the value to the output stream
      outstr.writeChar(value);  
      
      // creates a byte array with this output stream size and the
      // valid contents of the buffer have been copied into it
      byte []B = ((ByteArrayOutputStream) out).toByteArray();
      
      // copies contents of this byte array into data[]
      System.arraycopy (B, 0, data, position, 2);
      
    }
}
