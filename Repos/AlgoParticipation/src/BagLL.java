public class Bag<Item>{
  // reference to start of collection
  private Item head;
  private int size;

  public Bag(Item item){
    //set the head of the bag to intialize
    this.head = item;
    this.size = 0;
  }

  public boolean isEmpty(){
    if(size == 0){
      return true;
    }
    return false;
  }

  public void add(Item item){
    //if bag is empty we make the new item the head
    if (isEmpty()){
      this.head = item;
      this.size++;
    }

    //loop through the bag until we get tot he end, then add new item
    Item current = this.head;
    while(current != null){
       this.head = current.next;
    }
    current.next = item;
    this.size++;
  }
  //returns the size of the bag
  public int size(){
    return this.size;
  }
}
