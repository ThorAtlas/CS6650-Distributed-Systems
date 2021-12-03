package Server.Paxos;

import Server.Commands.Command;
import java.util.ArrayList;
import java.util.List;

public class ProposalObject {

  public ProposalObject(int machineID, int sequenceID, Command command){
    this.machineID = machineID;
    this.sequenceID = sequenceID;
    this.command = command;
  }
  //ID of the machine proposing
  int machineID;
  //Sequence value of the proposal
  int sequenceID;
  //The actual command being proposed
  Command command;

}
