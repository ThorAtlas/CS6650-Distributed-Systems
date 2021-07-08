public class Question5 {
  private int N; // number of key-value pairs in the table
  private int M = 16; // size of linear-probing table

///**
// * Prior to deletion of C
// *Hash table
// *
// * 0 P10       8 L11
// * 1 M9        9 null
// * 2 null     10 E12
// * 3 null     11 null
// * 4 A8       12 null
// * 5 C4       13 null
// * 6 S0       14 R3
// * 7 H5       15 X7
// *
// * C4 is deleted from Hash 5, and then S0, H5, and L11 are all reinserted
// * S0 hash is 6 and so it'll stay where it's at
// * H5 hash is 4. So it checks 4 which is occupied by A8, and move to the next availble hash which is
// * Hash 5 (the spot C was deleted from)
// * Then L11 will be reinserted it's hash is 6 and now that 7 is null after H5's reinsertion the next
// * null spot after Hash 6 is 7 and will be reinserted at Hash 7.
// *
// * Result of Delete C
// * 0 P10       8 null
// * 1 M9        9 null
// * 2 null     10 E12
// * 3 null     11 null
// * 4 A8       12 null
// * 5 H5       13 null
// * 6 S0       14 R3
// * 7 L11      15 X7
// */
//
//
//  /**
//   * The delete code is deleting a key from the table. All of the values associated with that key
//   * must be moved down to another cluster, which will be the next closest key to that
//   * key-to-be-deleted, more specifically the next closest key in incrementing order (modular M).
//   * The key is deleted and all the data associated with that key is moved to another cluster of
//   * key-pair data in the table.
//   *
//   */
//  public void delete(Key key)
//  {
//    //if the code does not contain the key we terminate the method with return
//    if (!contains(key)) return;
//
//
//    //we hash the key given which will return some hashcode
//    int i = hash(key);
//
//    //if the key != the key at the hashed out value i, we modular hash the
//    //previous i with +1 its previous value due to linear probing. looking for the closest
//    //value on the table near the current i's hash
//    while (!key.equals(keys[i])) {
//      //this is modular hashing
//      i = (i + 1) % M;
//    }
//
//    //once we find the proper hash value we delete the key and value stored at i (the modular keyhash)
//    keys[i] = null;
//    vals[i] = null;
//
//    //now we have to go through the process of reinserting all key-value pairs that is the
//    //in the cluster to the right of the hashcode i. This is done because if we just set it to null
//    //it will mess up other searches for values that are further down the linear probing chain
//    //i.e. we delete the keypair value at the keyprobe depth of 4, if we try to search for the same
//    //key any values further than 4 down the probe won't be found due to the null creating a "wall"
//    //per se during the search
//
//    //finds the cluster next to it
//    i = (i + 1) % M;
//
//    //finds the values that are in the cluster. Once we reach a null value that will be the end of
//    //the cluster. (that is if the cluster is not empty)
//    while (keys[i] != null)
//    {
//      //setting the keya nd value pairs to redo as temporary storages
//      Key keyToRedo = keys[i];
//      Value valToRedo = vals[i];
//
//      //setting those values to null/deleting them so they can be reinserted
//      keys[i] = null;
//      vals[i] = null;
//
//      //since we deleted a keyvalue pair by deleting a key we have to lower the number of
//      // keyvalue pairs, N
//      N--;
//
//      //we then re-add the keyToRedoValToRedo pair to the next closest cluster
//      put(keyToRedo, valToRedo);
//      //we then move onto the next cluster and continue
//      i = (i + 1) % M;
//    }
//
//    //since we deleted a key-value pair we must lower the count
//    N--;
//
//    //here we resize the arrays due to there being one less key-value pair now. This will ensure
//    //the table is at least 1/8th full ensuring the amount of memoryu used is always within a
//    //constant factor of the number of key-value pairs int he table.
//    if (N > 0 && N == M/8) resize(M/2);
//  }
}
