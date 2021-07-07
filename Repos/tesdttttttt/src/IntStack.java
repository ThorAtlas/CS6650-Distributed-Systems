import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class IntStack<T>{
  private List<T> stack ;

  public IntStack () {
    stack = new ArrayList<>();
  }

  public boolean empty () {
    return stack.size() == 0 ;
  }

  // push and pop operations
  public void push ( T obj ) {
    stack.add(obj);
  }

  public T pop () {
    if (stack.size() <= 0) {
      throw new EmptyStackException();
    }
    return stack.remove(stack.size() - 1);
  }

  public T peek () {
    if (stack.size() == 0) {
      return null;
    }
    return stack.get(stack.size() - 1);
  }
  public boolean hasNext() {
    return ( stack.size() > 0 );
  }

  @Override
  public String toString() { return stack.toString(); }

  public static void main(String [] args) {
    IntStack<String> istack = new IntStack<>();
    istack.push("3");
    istack.push("12");
    System.out.println(istack);
    System.out.println(istack.pop());
    System.out.println(istack);
  }
}