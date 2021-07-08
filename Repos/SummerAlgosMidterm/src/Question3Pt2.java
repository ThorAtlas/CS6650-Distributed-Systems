public class Question3Pt2 {
  public class Node{
    private int value;
    private int minIndex;
    private int maxIndex;

    public Node(){
    }

    public Node(int value){
      this.value = value;
    }

    public Node(int value, int minIndex, int maxIndex){
      this.value = value;
      this.minIndex = minIndex;
      this.maxIndex = maxIndex;
    }

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

    public int getMinIndex() {
      return minIndex;
    }

    public void setMinIndex(int minIndex) {
      this.minIndex = minIndex;
    }

    public int getMaxIndex() {
      return maxIndex;
    }

    public void setMaxIndex(int maxIndex) {
      this.maxIndex = maxIndex;
    }
  }

  public class Heap {

    //initializes a heap with size 0, and two arrays/heaps of nodes that are both null
    private int heapSize = 0;
    private Node[] maxHeap = null;
    private Node[] minHeap = null;

    public Heap() {
    }

    //because we are directly accessing the min/max heaps value via an array it is constant time
    //to access them.
    public int findMax() {
      return maxHeap[0].getValue();
    }

    public int findMin() {
      return minHeap[0].getValue();
    }

    /**
     * Insert a new value to the heap.
     *
     * @param x the value to add into the heaps.
     */
    public void insert(int x) {
      //created a new Node for the given value
      Node newNode = new Node(x);

      //insert to max heap
      insertMaxHeap(newNode);
      //insert to min heap
      insertMinHeap(newNode);

      //increase size of heap since we inserted something to it.
      heapSize++;
    }

    /**
     * Exract Max. This action extracts the max of this heap and update the references to the min
     * heap and change the min heap accordingly. Time Complexity is: O(lgn).
     */
    public void deleteMax() {
      if (heapSize > 0) {

        //gets the index of the minimum value in the maxHeap
        int minIndexOfMaxHeap = maxHeap[0].getMinIndex();

        //create a new node consists of the last element info in the maxHeap
        Node newNode = new Node(maxHeap[heapSize].getValue(),
            maxHeap[heapSize].getMinIndex(), maxHeap[heapSize].getMaxIndex());

        //replace the max node with the new node
        maxHeap[0] = newNode;
        minHeap[newNode.getMinIndex()].setMaxIndex(0);

        //delete the old max
        maxHeap[heapSize] = null;

//        //in case that this is the last element, just update references and heapify
//        if(minIndexOfMaxHeap == heapSize) {
//          minHeap[heapSize] = null;
//          heapSize = heapSize -1;
//          maxHeapify(maxHeap, 0);
//          minHeapify(minHeap, 0);
//        } else {
//          newNode = new Node();
//          newNode.setValue(minHeap[heapSize].getValue());
//          newNode.setMaxIndex(minHeap[heapSize].getMaxIndex());
//          newNode.setMinIndex(minHeap[heapSize].getMinIndex());
//
//          minHeap[minIndexOfMaxHeap] = newNode;
//          maxHeap[newNode.getMaxIndex()].setMinIndex(minIndexOfMaxHeap);
//
//          minHeap[heapSize] = null;
//
//          heapSize = heapSize -1;
//
//          maxHeapify(maxHeap, 0);
//          minHeapify(minHeap, minIndexOfMaxHeap);
//        }

      }
    }

    /**
     * Exract Min. This action extracts the min of this heap and update the references to the max
     * heap and change the max heap accordingly. Time Complexity is: O(lgn).
     *
     * @return int the min number extracted
     */
    public void deleteMin() {
      if (heapSize > 1) {
        //set the min value
        int maxIndexOfMinHeap = minHeap[0].getMaxIndex();

        //create a new node with all last element min heap details
        Node newNode = new Node(minHeap[heapSize].getValue(),
            minHeap[heapSize].getMaxIndex(), minHeap[heapSize].getMinIndex());

        minHeap[0] = newNode;
        maxHeap[newNode.getMaxIndex()].setMinIndex(0);

        minHeap[heapSize] = null;
//        // in case that this is the last element, clear references and heapify the heaps
//        if(maxIndexOfMinHeap==heapSize) {
//          maxHeap[heapSize] = null;
//
//          heapSize = heapSize -1;
//
//          minHeapify(minHeap, 0);
//          maxHeapify(maxHeap, 0);
//        } else {
//          newNode = new Node();
//          newNode.setValue(maxHeap[heapSize].getValue());
//          newNode.setMaxIndex(maxHeap[heapSize].getMaxIndex());
//          newNode.setMinIndex(maxHeap[heapSize].getMinIndex());
//
//          maxHeap[maxIndexOfMinHeap] = newNode;
//          minHeap[newNode.getMinIndex()].setMaxIndex(maxIndexOfMinHeap);
//
//          maxHeap[heapSize] = null;
//
//          heapSize = heapSize -1;
//
//          minHeapify(minHeap, 0);
//          maxHeapify(maxHeap, maxIndexOfMinHeap);
//        }
      }
    }

    /**
     * Insert a new node to the Max heap.
     *
     * @param newNode the node to insert
     */
    private void insertMaxHeap(Node newNode) {

      //set i as the heap size and then we move down
      int i = heapSize;

      //insert to Max heap
      while (i > 0 && (maxHeap[(i - 1) / 2].getValue() < newNode.getValue())) {
        Node node = new Node(maxHeap[(i - 1) / 2].getValue(),
            maxHeap[(i - 1) / 2].getMinIndex(), maxHeap[(i - 1) / 2].getMaxIndex());

        maxHeap[i] = node;
        // update reference in Min heap
        minHeap[maxHeap[i].getMinIndex()].setMaxIndex(i);
        //change to parent(i)
        i = (i - 1) / 2;
      }
      newNode.setMaxIndex(i);
      maxHeap[i] = newNode;
    }

    /**
     * Insert a new node to the min heap.
     *
     * @param newNode the new node to insert.
     */
    private void insertMinHeap(Node newNode) {
      int i = heapSize;

      //insert to Max heap
      while (i > 0 && (minHeap[(i - 1) / 2].getValue() > newNode.getValue())) {
        Node node = new Node();
        node.setValue(minHeap[(i - 1) / 2].getValue());
        node.setMinIndex(minHeap[(i - 1) / 2].getMinIndex());
        node.setMaxIndex(minHeap[(i - 1) / 2].getMaxIndex());

        minHeap[i] = node;
        // update reference in Min heap
        maxHeap[minHeap[i].getMaxIndex()].setMinIndex(i);
        //set to parent(i)
        i = (i - 1) / 2;
      }
      newNode.setMinIndex(i);
      minHeap[i] = newNode;
    }

//    /**
//     * Max Heapify
//     * @param heap the heap to perform this action
//     * @param index to perform this action
//     */
//    private void maxHeapify(Node[] heap, int index) {
//      int largest;
//      int left = (2 * index) + 1;
//      int right = (2 * index) + 2;
//
//      if (left <= heapSize && heap[left].getValue() > heap[index].getValue()) {
//        largest = left;
//      } else {
//        largest = index;
//      }
//
//      if (right <= heapSize
//          && heap[right].getValue() > heap[largest].getValue()) {
//        largest = right;
//      }
//      if (largest != index) {
//        maxExchange(heap, index, largest);
//        maxHeapify(heap, largest);
//      }
//    }
//
//    /**
//     * Swap the given heap indexes
//     * @param heap the heap to perform this action at.
//     * @param index the index of first element
//     * @param largest the index of the second element
//     */
//    private void maxExchange(Node[] heap, int index, int largest) {
//      int tempval = heap[index].getValue();
//      int tempMinIndex = heap[index].getMinIndex();
//
//      // swap values A[i]<->A[largest]
//      heap[index].setValue(heap[largest].getValue());
//      heap[index].setMinIndex(heap[largest].getMinIndex());
//
//      heap[largest].setValue(tempval);
//      heap[largest].setMinIndex(tempMinIndex);
//
//      // swap max array reference in Minimum heap
//      this.minHeap[heap[largest].getMinIndex()].setMaxIndex(largest);
//      this.minHeap[heap[index].getMinIndex()].setMaxIndex(index);
//    }

//    /**
//     * Min Heapify
//     * @param heap the heap to perform this action at
//     * @param index the element index
//     */
//    private void minHeapify(Node[] heap, int index) {
//      int smallest;
//      int left = (2 * index) + 1;
//      int right = (2 * index) + 2;
//
//      if (left <= heapSize && heap[left].getValue() < heap[index].getValue()) {
//        smallest = left;
//      } else {
//        smallest = index;
//      }
//
//      if (right <= heapSize
//          && heap[right].getValue() < heap[smallest].getValue()) {
//        smallest = right;
//      }
//      if (smallest != index) {
//        minExchange(heap, index, smallest);
//        minHeapify(heap, smallest);
//      }
//    }

//    /**
//     * Swap the given heap elements
//     * @param heap
//     * @param index
//     * @param smallest
//     */
//    private void minExchange(Node[] heap, int index, int smallest) {
//      int tempval = heap[index].getValue();
//      int tempMaxIndex = heap[index].getMaxIndex();
//
//      // swap values A[i]<->A[smallest]
//      heap[index].setValue(heap[smallest].getValue());
//      heap[index].setMaxIndex(heap[smallest].getMaxIndex());
//
//      heap[smallest].setValue(tempval);
//      heap[smallest].setMaxIndex(tempMaxIndex);
//
//      // swap max array reference in Minimum heap
//      this.maxHeap[heap[smallest].getMaxIndex()].setMinIndex(smallest);
//      this.maxHeap[heap[index].getMaxIndex()].setMinIndex(index);
//    }
//
//    public Node[] getMaxHeap() {
//      return maxHeap;
//    }
//
//    public Node[] getMinHeap() {
//      return minHeap;
//    }
//  }

  }
}


