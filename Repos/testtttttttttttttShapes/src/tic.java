public class tic {

//  public int ticMovement(SetCenter animation){
//    int time;
//
//
//    animation.getStartTime() + animation.getEndTime();
//    return 0;
//  }
//
//  public int ticColor(SetColor animation){
//    int a = animation.getR;
//    int b = animation.getB;
//    int c = animation.getG;
//    return 0;
//  }
//
//  public int ticSize(SetCenter animation){
//    return 0;
//  }
  public double ticAtTime(double ticTime, double initialValue, double endValue, double initialTime,
      double endtime) throws IllegalArgumentException{
    if(ticTime < initialTime || ticTime > endtime){
      throw new IllegalArgumentException("ticTime must be inbetween the start and endtimes");
    }
    return ((initialValue * ((endtime - ticTime) / (endtime - initialTime)))
        + (endValue * ((ticTime - initialTime) / (endtime - initialTime))));

  }


}
