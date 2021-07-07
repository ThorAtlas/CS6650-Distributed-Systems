import java.util.ArrayList;

public class Question1 {
  //Want to first find floor F in log(N) throws, where N is the building height and F is the floor
  //that is breaks on.
  public static class Building{
    private int floors;
    private int brokenEggFloor;
    private ArrayList listOfFloors;
    private int BREAKABLE_FLOOR = 1;
    private int SAFE_FLOOR = 0;

    public Building(int floors, int brokenEggFloor){
      this.floors = floors;
      this.brokenEggFloor = brokenEggFloor;
      listOfFloors = new ArrayList<Integer>();
      int count = 0;
      while(count < floors){
        if(count < brokenEggFloor - 1){
          listOfFloors.add(0);
        }
        else{
          listOfFloors.add(1);
        }
        count++;
      }
    }

    public int getFloors() {
      return floors;
    }

    public int getBrokenEggFloor() {
      return brokenEggFloor;
    }

    public ArrayList getListOfFloors(){

      return listOfFloors;
    }

    public int findFloorLgN(){
      return findFloorLgN(0, this.getFloors());
    }

    public int findFloorLgN(int low, int high){
      if(low <= high){
        int middle = (low + high) / 2;

        //prints the current index of the floor we are at
        System.out.println("Current index: " + middle);

        //middle is an unbreakble floor
        if(BREAKABLE_FLOOR > (Integer) listOfFloors.get(middle)){
          return findFloorLgN(middle + 1, high);
        }
        else{ //middle is breakable floor
          int floorBelowBreak = findFloorLgN(low, middle - 1);

          //if the floorbelowBreak is -1 it means the low > middle - 1
          //meaning we found the proper middle
          if (floorBelowBreak == -1){
            //it goes by index so the floor is +1 of the index
            System.out.println("The breakable egg floor is: " + (middle + 1));
            return middle;
          }
          else{
            //otherwise we just continue the loop
            return floorBelowBreak;
          }
        }
      }
      else{
        return -1;
      }
    }

    public int findFloor2LgF() {
      //log(f^2) = 2lg(f) so first lets focus on the f^2 portion
      //To do so we will be using a loop to find a breakable floor faster than if we were to just
      //test the middle starting from the lowest floor
      int lowestFloor = 0;

      //A normal forloop by adding one over and over again would be of N speed, but one to the power
      //reduces the speed logarithmically. This also depends on the instance of breakable floors
      //rather than the height of the building

      //first we check if the first two floors are breakable if it is we do a normal floor checker
      if((Integer) listOfFloors.get(0) == BREAKABLE_FLOOR
          || (Integer) listOfFloors.get(1) == BREAKABLE_FLOOR ){
        return findFloorLgN(0,1);
      }

      //create a powerSearchStore so we can use it later to find the previous unbreakable floor.
      int powerSearchStore = 2;
      for(int powerSearch = 2; BREAKABLE_FLOOR != (Integer) listOfFloors.get(lowestFloor);
          powerSearch++){
        powerSearchStore = powerSearch;
        System.out.println("Current Index: " + lowestFloor);
        lowestFloor = (int) Math.pow(2, powerSearch);
      }

      //now we found a more suitable value to use the previous

      int previousUnbreakableFloor = (int) Math.pow(1, powerSearchStore - 1);
      int highestFloor = Math.min(this.getFloors() - 1, lowestFloor);
      int floorSearch = findFloorLgN(previousUnbreakableFloor + 1, highestFloor);

      if(floorSearch == -1){
        return highestFloor;
      }
      else{
        return floorSearch;
        //asd
      }
    }

  }

  public static void main(String[] args){
    //shows that the algorithm works in lg(n) time.
//    Building testBuilding = new Building(12,12);
//    System.out.println(testBuilding.findFloorLgN(0, testBuilding.getFloors()));

    Building testBuilding2 = new Building(10000, 50);
    Building testBuilding01 = new Building(30000, 1);
    testBuilding2.findFloor2LgF();
    testBuilding01.findFloor2LgF();

  }
}

