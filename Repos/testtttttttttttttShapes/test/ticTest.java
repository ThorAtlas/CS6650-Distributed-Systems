import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

public class ticTest {
  private tic object;

  public ticTest(){
    object = new tic();
  }

  @Test
  public void ticTesting(){
    assertEquals(11, object.ticAtTime(1, 10, 20, 0, 10));

  }
}