public class Question3 {

  public class Node implements Comparable<Node>{
    Comparable key;
    int minHeapIndex;
    int maxHeapIndex;

    @Override
    public int compareTo(Node other) {

      return key.compareTo(other.key);
    }

  }

//  private class Key extends Comparable<Key> {
//
//    @Override
//    public int compareTo(Key o) {
//      return 0;
//    }
//  }

  public class MinMaxPriorityQueue<Key extends Comparable<Key>>{
    private Node[] minQueue;
    private Node[] maxQueue;
    private int size = 0;

    MinMaxPriorityQueue(){
      minQueue = new Node[2];
      maxQueue = new Node[2];
    }

    public boolean isEmpty(){
      return size == 0;
    }

    public int getSize(){
      return size;
    }

    public void insert(Key key){

    }

  }

}
