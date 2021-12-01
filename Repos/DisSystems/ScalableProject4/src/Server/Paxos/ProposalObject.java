package Server.Paxos;

import Server.Commands.Command;
import java.util.ArrayList;
import java.util.List;

public class ProposalObject {
  //ID of the machine proposing
  int machineID;
  //Sequence value of the proposal
  int sequenceID;
  //The actual command being proposed
  Command command;

}
