package view;
import java.util.Observable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Interpreter;

public class ConsoleView extends View {
	private HistoryView historyView;
	private Interpreter interpreter;
	
	public ConsoleView(String id, HistoryView history) {
		super(id);
		historyView = history;
	}
	
	@Override
	public void update(Observable o, Object arg) {

	}
	
	public void setInterpreter(Interpreter ip) { 
		interpreter = ip; 
	}

	@Override
	public Group getView() {
		Group group = new Group();
		VBox vb = new VBox();
		TextArea console = new TextArea();
		console.getStyleClass().add("code");
		Button btn = new Button("Run");
		btn.setOnMouseClicked(e->{
			HistoryElem hist = new HistoryElem(console.getText(), historyView);
			interpreter.run(console.getText());
//			//if error, create error and add to history
//			
//			//else if variable, create variable and add to saved vars
//			if(console.getText().contains("make '")){
//				String[] textList = console.getText().split(" ");
//				controller.addVariable(textList[1].substring(1),textList[2]);
//			}//else if method, create method and add to saved methods
//			else if(console.getText().contains("save")){
//				String[] textList = console.getText().split(" ");
//				methodcontroller.addMethod(textList[1]);
//			}
//			console.clear();
		});
		vb.getChildren().addAll(console,btn);
		vb.setPrefSize(View.WIDE_WIDTH, View.SHORT_HEIGHT);
		group.getChildren().add(vb);
		return group;
	}

}