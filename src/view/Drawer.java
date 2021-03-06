
package view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;


public class Drawer {
	private static final String NO_SELECT_EFFECT = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 0, 0, 0, 0)";
	private static final String SELECT_EFFECT = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)";
	private static final double DASH_OFFSET = 8;
	private Pane agentGroup;
	private List<ImageView> stampList;
	private List<Node> lineList;
	private List<ImageView> agentViewList;

	
	public Drawer(Pane agentPane){
		agentGroup = agentPane;
		stampList = new ArrayList<ImageView>();
		lineList = new ArrayList<Node>();
		agentViewList = new ArrayList<ImageView>();
		

	}
	public void drawLine(double oldX,double oldY,double newX, double newY, double thickness, Color color, double dash){
		Line line = new Line();
		line.setStartX(oldX);
		line.setStartY(oldY);
		line.setEndX(newX);
		line.setEndY(newY);
		line.setStrokeWidth(thickness);
		line.setStroke(color);
		line.getStrokeDashArray().removeAll();
		line.getStrokeDashArray().add(dash);
		line.setStrokeDashOffset(DASH_OFFSET);
		lineList.add(line);
		agentGroup.getChildren().add(line);
	}
	/**
	 * 
	 * Need to make a copy of ImageView to stamp it otherwise run into duplicate children issue
	 */
	public void stampImage(ImageView img, double xPosition, double yPosition){
		setLocation(img, xPosition, yPosition);
		stampList.add(img);
		agentGroup.getChildren().add(img);
	}
	private void setLocation(ImageView img, double xPosition, double yPosition) {
		img.setLayoutX(xPosition -  img.getBoundsInParent().getWidth()/2);
		img.setLayoutY(yPosition - img.getBoundsInParent().getHeight()/2);

	}
	
	public void moveImage(ImageView img, double xPosition, double yPosition){
		agentGroup.getChildren().remove(img);
		setLocation(img, xPosition, yPosition);
		agentGroup.getChildren().add(img);
		if (!agentViewList.contains(img)){
			agentViewList.add(img);
		}
	}

	public void addSelectEffect(ImageView img){
		img.setStyle(SELECT_EFFECT);
	}
	public void removeSelectEffect(ImageView imageView) {
		imageView.setStyle(NO_SELECT_EFFECT);
	}
	public void removeImage(ImageView agentView) {
		agentGroup.getChildren().remove(agentView);
		agentViewList.remove(agentView);
	}
	public void setNewImage(ImageView oldView,ImageView newView, double xPosition,double yPosition) {
		agentGroup.getChildren().remove(oldView);
		agentViewList.remove(oldView);
		setLocation(newView, xPosition, yPosition);
		agentGroup.getChildren().add(newView);
		agentViewList.add(newView);
	}

	public void clearAllLines() {
		for (Node line: lineList){
			agentGroup.getChildren().remove(line);
		}
	}
	public int clearAllStamps(){
		if (!stampList.isEmpty()) {
			
			for (Node stamp: stampList){
				agentGroup.getChildren().remove(stamp);
			}
			stampList.clear();
			return 1;
		}
		return 0;
	}
	public void removeSelectEffectForNonSelectedTurtles(ImageView selectedImageView) {
		for (ImageView img: agentViewList){
			if (!img.equals(selectedImageView)){
				removeSelectEffect(img);
			}
		}
		
	}

}