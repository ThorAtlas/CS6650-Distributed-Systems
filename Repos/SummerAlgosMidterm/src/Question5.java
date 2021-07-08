public class Question5 {
  private int N; // number of key-value pairs in the table
  private int M = 16; // size of linear-probing table

  public void delete(Key key)
  {
    //if the code does not contain the key we terminate the method with return
    if (!contains(key)) return;


    //we hash the key given which will return some hashcode
    int i = hash(key);

    //if the key != the key at the hashed out value i, we modular hash the
    //previous i with +1 its previous value due to linear probing. looking for the closest
    //value on the table near the current i's hash
    while (!key.equals(keys[i])) {
      //this is modular hashing
      i = (i + 1) % M;
    }

    //once we find the proper hash value we delete the key and value stored at i (the modular keyhash)
    keys[i] = null;
    vals[i] = null;

    //now we have to go through the process of reinserting all key-value pairs that is the
    //in the cluster to the right of the hashcode i. This is done because if we just set it to null
    //it will mess up other searches for values that are further down the linear probing chain
    //i.e. we delete the keypair value at the keyprobe depth of 4, if we try to search for the same
    //key any values further than 4 down the probe won't be found due to the null creating a "wall"
    //per se during the search

    //finds the cluster next to it
    i = (i + 1) % M;

    //finds the values that are in the cluster. Once we reach a null value that will be the end of
    //the cluster.
    while (keys[i] != null)
    {
      //setting the keya nd value pairs to redo as temporary storages
      Key keyToRedo = keys[i];
      Value valToRedo = vals[i];

      //setting those values to null
      keys[i] = null;
      vals[i] = null;

      N--;
      put(keyToRedo, valToRedo);
      i = (i + 1) % M;
    }
    N--;
    if (N > 0 N == M/8) resize(M/2);
  }
}
