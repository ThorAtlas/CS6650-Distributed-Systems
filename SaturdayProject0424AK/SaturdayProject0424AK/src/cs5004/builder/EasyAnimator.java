package cs5004.builder;

import static cs5004.builder.AnimationReader.parseFile;

import java.io.FileReader;
import cs5004.builder.AnimationBuilder;
import cs5004.builder.AnimationModel;
import cs5004.builder.Builder;
import cs5004.builder.IAnimationModel;
import cs5004.views.IView;
import cs5004.views.SVGView;
import cs5004.views.TextView;
import cs5004.views.ViewFactory;
import cs5004.views.VisualView;

public class EasyAnimator {
  /**
   * Method that builds and shows us the animation we have built.
   *
   * @param args string arguments
   */
  public static void main(String[] args) throws Exception {
    String inputName = null;
    String viewType = null;
    String output = null;
    int tps = 1;
    IView view = null;

    Appendable out = new StringBuffer();
    int i;

    for (i = 0; i < args.length; i++) {
      String str = args[i].replace("<", "".replace(">", ""));
      String id = str.split(" ")[0];

      if (args[i].equalsIgnoreCase("-in")) {
        inputName = args[i + 1];
        //if doesn't contain .txt in its name then adds it
        if (!inputName.contains(".txt")) {
          inputName = inputName + ".txt";
        }
      } else if (args[i].equalsIgnoreCase("-view")) {
        viewType = args[i + 1];
        if (!viewType.equalsIgnoreCase("svg")
            && !viewType.equalsIgnoreCase("text")
            && !viewType.equalsIgnoreCase("visual")) {
          throw new IllegalArgumentException("only txt, svg, or visual views allowed.");
        }
      } else if (args[i].equalsIgnoreCase("-out")) {
        output = args[i + 1];
      } else if (args[i].equalsIgnoreCase("-speed")) {
        tps = Integer.parseInt(args[i + 1]);
      }
    }
    if (inputName == null || viewType == null) {
      throw new IllegalArgumentException("input and viewType cannot be null");
    }
    if (output == null && viewType.equalsIgnoreCase("svg")) {
      output = System.out.toString();
    }
    AnimationBuilder<AnimationModel> build = new Builder();

    Readable infile = new FileReader(inputName);
    IAnimationModel model = parseFile(infile, build);
    System.out.println(model.toString());

//    System.out.println(model.getShapeList().toString());
//    System.out.println(model.getAnimationList().toString());
    view = ViewFactory.createView(viewType);
    if(view instanceof TextView){
      System.out.println("Inside instance of TextView");
      view.render(model);//.getShapeList());
//     view.render(model.getShapeList()).toString();
//      System.out.println(output);
    }
    if(view instanceof SVGView){
      view.render(model/*).getShapeList()*/, new StringBuilder(), tps);
    }
    if(view instanceof VisualView){
      int count = 0;
      while(count < 1000){
        view.render(model/*.getShapeList()*/, new StringBuilder(), count);
        count++;
        try{
          Thread.sleep(1000);
        }
        catch(Exception e){

        }
      }

    }


  }

}
