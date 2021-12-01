package Server.Commands;

import java.util.ArrayList;
import java.util.List;

public class PutCommand implements Command{
  String key;
  String value;
  List<String> commandList = new ArrayList<String>();
  public PutCommand(String key, String value){

    this.key = key;
    this.value = value;
    commandList.add("PUT");
    commandList.add(key);
    commandList.add(value);
  }

  @Override
  public List getCommand() {
    return commandList;
  }
}
