package view;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * This view displays all the agents and updates when the update method is called by the Agent Observables. This class keeps tracks of all the images on the screen and maps them to an Agent object.
 * @author Melissa Zhang
 *
 */
public class ViewAgents extends View{
	private static final int CONSOLEX = NARROW_WIDTH;
	private static final int CONSOLEY = MENU_OFFSET;
	private static final Color DEFAULT_COLOR = Color.WHITE;
	private static final String UPDATE_PROPERTIES = "updateObserver";
	private static final String WINDOW_PROPERTIES = "windowProperties";
	private static final double MAX_PREFERENCE_HEIGHT = 40;

	private Drawer drawer;
	private Color backgroundColor;
	private StringProperty backgroundColorString = new SimpleStringProperty();
	private ResourceBundle updateResources;
	private ResourceBundle windowResources;
	private ResourceBundle cssResources = ResourceBundle.getBundle("CSSClasses");
	private ResourceBundle preferenceResources = ResourceBundle.getBundle("preferencesdefault");
	private HBox agentViewPreferences;
	private Pane agentPane;
	private Boolean isSelectedAgentToggle;
	private HashMap<ImageView,Agent> imageAgentMap;
	private StringProperty currentAgentNameProperty;
	protected HashMap<String, Agent> agentMap;
	private CustomImagePalette imagePalette;
	private CustomColorPalette colorPalette;
	private Map<String,List<Object>> savedPreferences;

	
	public ViewAgents(ViewType ID, Map<String,List<Object>> savedPreferences) {
		super(ID, savedPreferences);
		setX(CONSOLEX);
		setY(CONSOLEY);
		isSelectedAgentToggle = false;
		currentAgentNameProperty = new SimpleStringProperty();
		agentMap = new HashMap<String,Agent>();
		imageAgentMap = new HashMap<ImageView,Agent>();

		updateResources = ResourceBundle.getBundle(UPDATE_PROPERTIES);    
		windowResources = ResourceBundle.getBundle(WINDOW_PROPERTIES);
		
		agentPane = getPane();
		drawer = new Drawer(agentPane);

		agentPane.setPrefSize(WIDE_WIDTH, WIDE_WIDTH);

		
		agentViewPreferences = new HBox();
		agentViewPreferences.getStyleClass().add(cssResources.getString("DISPLAYVIEW"));
		agentViewPreferences.setMaxHeight(MAX_PREFERENCE_HEIGHT);
		agentViewPreferences.setLayoutY(WIDE_WIDTH-agentViewPreferences.getMaxHeight());
		setPane(agentViewPreferences);
		
		this.savedPreferences = savedPreferences;
		//initPreferences(savedPreferences);
		setBackgroundColor(Color.valueOf(preferenceResources.getString("BACKGROUNDCOLOR")));


	}
	private void initPreferences(Map<String, List<Object>> savedPreferences) {
		savedPreferences.put("background", Arrays.asList(new StringProperty[]{backgroundColorString}));
	}
	public void setBackgroundColor(Color color){
		agentPane.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
		backgroundColor = color;
		//backgroundColorString.setValue(backgroundColor.toString());
	}
	public Color getBackgroundColor(){
		return backgroundColor;
	}
	
	public void setUpColorPicker(){
		ColorPicker colorPicker = new ColorPicker(backgroundColor);
        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                setBackgroundColor(colorPicker.getValue());      
            }
        });

        agentViewPreferences.getChildren().add(colorPicker);

	}

	@Override
	public void update(Observable agent, Object updateType) {
		
		if(((Agent) agent).isVisible()){
			if (updateType == updateResources.getString("STAMP")){
				drawer.stampImage(getImageCopy((Agent) agent), ((Agent) agent).getXPosition(), ((Agent) agent).getYPosition());
			
			}else if (updateType == updateResources.getString("MOVE")){
				drawer.moveImage(((Agent) agent).getImageView(), ((Agent) agent).getXPosition(), ((Agent) agent).getYPosition());
				if(!((Agent) agent).isPenUp()){
					drawer.drawLine(((Agent) agent).getOldXPosition(), ((Agent) agent).getOldYPosition(), ((Agent) agent).getXPosition(), ((Agent) agent).getYPosition(),((Agent) agent).getPenThickness(),((Agent) agent).getPenColor(),Integer.parseInt(updateResources.getString(((Agent) agent).getPenStyle()+"DASH")));
				}

			}else if (updateType == updateResources.getString("INITIAL")){ 
				ImageView agentImageView = createNewImageViewWithHandler(agent);
				drawer.moveImage(agentImageView, ((Agent) agent).getXPosition(), ((Agent) agent).getYPosition());
			
			}else if (updateType == updateResources.getString("IMAGEVIEW")){
				imageAgentMap.remove(((Agent) agent).getOldImageView());
				ImageView newAgentImageView = createNewImageViewWithHandler(agent);
				drawer.setNewImage(((Agent) agent).getOldImageView(),newAgentImageView,((Agent) agent).getXPosition(), ((Agent) agent).getYPosition());
			
			}else if (updateType == updateResources.getString("CURRENT")){
				currentAgentNameProperty.setValue(((Agent) agent).getName());
				if(isSelectedAgentToggle){
					drawer.addSelectEffect(((Agent) agent).getImageView());
					drawer.removeSelectEffectForNonSelectedTurtles(((Agent) agent).getImageView());
				}else{
					drawer.removeSelectEffect(((Agent) agent).getImageView());
				}
			}
		}else if(updateType == updateResources.getString("VISIBLE")){
			drawer.removeImage(((Agent) agent).getImageView());
			
		}

			
	}
	private ImageView getImageCopy(Agent agent) {
		ImageView imageCopy = (ImageView) imagePalette.getPaletteObject(((Agent) agent).getCurrentImageIndex());
		return imageCopy;
	}
	private ImageView createNewImageViewWithHandler(Observable agent) {
		ImageView newAgentImageView;
		if (((Agent) agent).getCurrentImageIndex()<0){ //use Default imageview
			newAgentImageView = ((Agent) agent).getImageView();
		}else{
			newAgentImageView = (ImageView) imagePalette.getPaletteObject(((Agent) agent).getCurrentImageIndex());
		}
		addImageHandler(newAgentImageView);
		imageAgentMap.put(newAgentImageView, (Agent) agent);
		return newAgentImageView;
	}
			
	@Override
	public Pane getView() {
		setUpColorPicker();
		setUpClearButton();
		setUpSelectAgentToggle();
		//return agentPane;
		return super.getView();
	}
	private void addImageHandler(ImageView img){
		img.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

		     @Override
		     public void handle(MouseEvent event) {
		         update(imageAgentMap.get(img),updateResources.getString("CURRENT"));
		     }


		});
	}
	

	private void setUpClearButton() {
		Button clearButton = new Button(windowResources.getString("CLEARBUTTON"));
		clearButton.setOnAction(new EventHandler() {
            public void handle(Event t) {
                drawer.clearAllLines();  
                drawer.clearAllStamps();
            }
        });
		agentViewPreferences.getChildren().add(clearButton);
		
	}
	
	private void setUpSelectAgentToggle(){
		CheckBox agentToggle = new CheckBox();
		agentToggle = new CheckBox(updateResources.getString("SELECTAGENTLABEL"));
		agentToggle.setSelected(isSelectedAgentToggle);
	    agentToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
	    
			public void changed(ObservableValue<? extends Boolean> ov,
	            Boolean old_val, Boolean new_val) {
	        		isSelectedAgentToggle = new_val;
	        		updateCurrentAgentView();
	        }
	    });
	    agentViewPreferences.getChildren().add(agentToggle);
	}
	public double getWidth() {
		return WIDE_WIDTH;
	}
	public double getHeight() {
		return WIDE_WIDTH;
	}
	public StringProperty getCurrentAgentNameProperty() {
		return currentAgentNameProperty;
	}
	public void updateCurrentAgentView(){
		update(agentMap.get(currentAgentNameProperty.getValue()),updateResources.getString("CURRENT"));

	}
	public void updateAgentMap(HashMap<String, Agent> newAgentMap) {
		agentMap = newAgentMap;
		
	}
	public void setColorPalette(CustomColorPalette customColorPalette) {
		colorPalette = customColorPalette;
	}
	public void setImagePalette(CustomImagePalette customImagePalette) {
		imagePalette = customImagePalette;
	}

	
}