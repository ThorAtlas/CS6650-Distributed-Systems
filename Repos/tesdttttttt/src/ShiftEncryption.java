/**
 * In order to have combo strategies that would combine slide and shift, you would call
 * slide and shift at the appropriate times within that combostrategy. I.e. lets say you want to
 * slide than shift insinde combo(). You would first call the slide encyrption inside combo, then
 * you would call the shift encyrption.
 */
public class ShiftEncryption implements EncryptionStrategy {

  //should only take -10-10 values
  private int valueShift;

  public ShiftEncryption(int valueToShift) throws IllegalArgumentException {
    if (valueToShift < -10 || valueToShift > 10) {
      throw new IllegalArgumentException("nonono can only shift by a magnitude of 10!");
    }
    valueShift = valueToShift;
  }

  @Override
  public String encode(String msg) {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < msg.length(); i++) {
      char ch = ((char) (((int) msg.charAt(i) + valueShift) % 128));
      result.append(ch);
    }
    return result.toString();
  }
}

/**
 * all this commented out stuff below was to account for the chnace of a non-alpha character
 * being converted into a alpha character and then bein stuck in that loop.
 * However, I ran into some trouble for when trying to account for those non-alpha characters
 * between 91-96 and I decided to just leave it as is.
 * In order to check for them I would check if the value added makes a nonalpha a alpha
 * if it did I would add 26 and see if it was greater than 96 (because then it would still be a
 * nonalpha). If it is then I would instead add 62 to it and mod 128.
 *
 * I would do the same logic for the reverse when checking if adding a value (i.e. subtracting) made
 * a nonalpha alpha and follow the same logic.
 */
//      for (int i = 0; i < msg.length(); i++) {
//    if (Character.isUpperCase(msg.charAt(i))) {
//        //add the valueshift wanted fist because we are now shifting it, leaving it as this leaves
//        //a very basic shift cipher that won't cycle letters
//        //second subtract by 65 because it is the start of uppercase letters in ASCII
//        // mod that whole thang by 26 because there are only 26 letters in the alphabet
//        //and if you mod a negative number it'll give you the proper "cycled" value of those 26
//        //letters
//        //lastly add 65 back to it because that is the begining of the uppercase letters
//        //you won't get any values past the uppercase letters because the min/max you can get in
//        //this method is 65-90
//        char ch = (char)(((int)msg.charAt(i) +
//            valueShift - 65) % 26 + 65);
//        result.append(ch);
//      }
//      //if the cahracter is lower case we adjust the above logic for the lowercase ASCII values
//      else if( Character.isLowerCase(msg.charAt(i))) {
//        char ch = (char)(((int)msg.charAt(i) +
//            valueShift - 97) % 26 + 97);
//        result.append(ch);
//      }
//      else if((((int)msg.charAt(i)) + valueShift > 64)){
//        //check to see if adding valueshift turns a nonAlpha character into alpha character
//        //takes steps to avoid that and turn it into another non-character
//        if ((int)msg.charAt(i) + valueShift + 24 > 96){
//          int ch = ((int)msg.charAt(i) + 50 + valueShift);
//          if(ch > 127){
//            ch = ch % 128;
//            result.append(((char)ch);
//          }else{
//          result.append(((char)ch);
//          }
//        }else{
//          char ch = (char)((int)msg.charAt(i) + 24 + valueShift);
//          result.append(ch);
//        }
//      }
//      //check to see if subtract valueshift turns a nonAlpha character into alpha character
//      //takes steps to avoid that and turn it into another non-character
//      else if((((int)msg.charAt(i)) + valueShift < 123)){
//        if((int)msg.charAt(i) + valueShift - 26 > 90) {
//          char ch = (char) ((int) msg.charAt(i) );
//          result.append(ch);
//        }else {
//          char ch = (char) ((int) msg.charAt(i) + 60);
//          result.append(ch);
//        }

//      }

//      //if the character is none of the above we just shift it
//      else{
//        char ch = (char)((int)msg.charAt(i) + valueShift);
//        result.append(ch);
//      }
//    }
//    return result.toString();
//  }
//}
