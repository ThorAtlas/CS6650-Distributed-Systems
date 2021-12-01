package Server.Commands;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand implements Command{
  List<String> commandList = new ArrayList();
  String wordToDelete;
  public DeleteCommand(String wordToDelete){
    this.wordToDelete = wordToDelete;
    commandList.add("DELETE");
    commandList.add(wordToDelete);
  }

  @Override
  public List getCommand() {
    return commandList;
  }
}
