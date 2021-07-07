public class Array<Item>{

  //initializes the array with an item
  private Item[] bag = (Item[]) new Object[1];
  private int elements;

  public Array(int bagSize){
    //sets the size of the array
    bag = new Item[bagSize];
    this.elements = 0;
  }

  public boolean isEmpty(){
    if(this.elements == 0){
      return true;
    }
    return false;
  }

  public void add(Item item){
    //if aray is empty we set the 0th item as the item
    if (isEmpty()){
      bag[0] = item;
    }
    // if array is not full with valid elements we add it to the back
    if(this.elements < this.bag.length){
      this.bag[this.elements] = item;
    }
    if(this.elements >= this.bag.length){
      //loop through and copy all elelemnt,s then addd new one
      //create new array double the size
      Item[] newBag = (Item[]) new Object[this.size * 2];
      for(int i = 0; i < this.bag.length; i++){
        newBag[i] = this.bag[i];
      }
      newBag[this.elements] = item;

      this.bag = newBag;
    }

  }
  //returns the size of the bag
  public int size(){
    return this.elements;
  }
}