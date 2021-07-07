

public class SlideEncryption implements EncryptionStrategy {
  private int valueSlide;

  public SlideEncryption(int valueToSlide){
    valueSlide = valueToSlide;
  }

  @Override
  public String encode(String msg) {
    StringBuilder message = new StringBuilder();

    if(valueSlide > 0){
      //char slidLetter = msg.charAt(msg.length()-1);
      message.append(msg.charAt(msg.length()-1));

      for (int i = 1; i < msg.length(); i++) {
        char slidLetter = msg.charAt(i - 1);
        message.append(slidLetter);
      }
      return message.toString();
    }

    else if(valueSlide < 0) {
      for (int i = 0; i < msg.length(); i++) {
        if (i == msg.length() - 1) {
          char slidLetter = msg.charAt(0);
          message.append(slidLetter);
          break;
        }
        char slidLetter = msg.charAt(i + 1);
        message.append(slidLetter);
      }
      return message.toString();
    }

    else{
        return msg;
    }
  }
}
