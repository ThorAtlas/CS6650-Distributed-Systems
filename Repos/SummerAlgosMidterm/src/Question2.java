import java.lang.reflect.Array;
import java.util.Arrays;

public class Question2 {

  public static class QuickSort {

    /* This function takes last element as pivot,
   places the pivot element at its correct
   position in sorted array, and places all
   smaller (smaller than pivot) to left of
   pivot and all greater elements to right
   of pivot */
    private int[] arr;

    public int[] getArr() {
      return arr;
    }

    //A constructor class that'll create an Array of random numbers based on input
    public QuickSort(int numbersInArray) {
      this.arr = new int[numbersInArray];
      for (int i = 0; i < arr.length; i++) {
        arr[i] = (int) Math.floor(Math.random() * (100 - 1 + 1) + 1);
      }
    }

    //swapper method
    public void swap(int i, int j) {
      int temp = arr[i];
      arr[i] = arr[j];
      arr[j] = temp;
    }

    //partition code will be the same as the recursive method of quicksort
    public int partition(int low, int high) {
      //we select the higher index as the pivot point (chosen for ease)
      int pivot = arr[high];

      // index of smaller element we start one less than low because of the for loop.
      int i = (low - 1);

      for (int j = low; j <= high - 1; j++) {
        // If current element is smaller than or equal to pivot we swap
        if (arr[j] <= pivot) {
          i++;
          swap(i, j);
        }
      }
      //swap pivot and the next element after the swapped one
      swap(i+1, high);
      return i+1;
    }

    public void quickSortIterative(int startIndex, int endIndex) {
      // Create a temporary stack that is the size of the amount of items between start/end indexes
      int[] stack = new int[endIndex - startIndex + 1];

      //initialize the stack
      int stackPointer = 0;
      stack[stackPointer] = startIndex;
      stackPointer++;
      stack[stackPointer] = endIndex;

      // while stack is not empty we continue to partition and swap
      while (stackPointer >= 0) {
        // Get the ending index and the starting indexes
        endIndex = stack[stackPointer];
        stackPointer--;
        startIndex = stack[stackPointer];
        stackPointer--;

        // Set pivot element at its correct position
        int p = partition(startIndex, endIndex);

        // If elements on left of pivot then add them to the left of the stack
        if (p - 1 > startIndex) {
          stackPointer++;
          stack[stackPointer] = startIndex;
          stackPointer++;
          stack[stackPointer] = p - 1;
        }

        // If elements on right of pivot then add them to the right of the stack
        if (p + 1 < endIndex) {
          stackPointer++;
          stack[stackPointer] = p + 1;
          stackPointer++;
          stack[stackPointer] = endIndex;
        }
      }
    }

    public static void main(String args[]) {

      QuickSort test = new QuickSort(10);
      System.out.println(Arrays.toString(test.getArr()));

      test.quickSortIterative(0, 9);
      System.out.println(Arrays.toString(test.getArr()));

    }
  }
}





