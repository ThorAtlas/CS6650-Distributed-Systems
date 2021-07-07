public class StrategyMain {
  public static void main(String [] args) {
    SpyKid kid = new SpyKid("Carmen", "Hello Shark Boy!",
        new SlideEncryption(-1)); // slide right
    System.out.println(kid);
    kid.changeEncryptionStrategy(new ShiftEncryption(5)); // shift right
    System.out.println(kid);


    String shiftedEncrption = kid.toString();
    SpyKid testing = new SpyKid("testing", shiftedEncrption,
        new ShiftEncryption(-5)); // slide right
    System.out.println(testing);


    SpyKid otherKid = new SpyKid("Juni", "Ifmmp!Tibsl!Cpz",
        new ShiftEncryption(-1)); // shift left by 1
    System.out.println(otherKid);

    SpyKid test = new SpyKid("test", "az",
        new ShiftEncryption(10));
    System.out.println(test);
  }
}