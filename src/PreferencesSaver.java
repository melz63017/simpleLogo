import java.io.File;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.collections.ObservableList;
import javafx.stage.Stage;
import view.Preferences;

public class PreferencesSaver extends Saver {
	private static final String EXTENSION = "*.xml";
	private static final String EXTENSION_DESCRIPTION = "XML Files";
	private Document doc;	
	private Element rootElem;
	private Preferences preferences;
	
	public PreferencesSaver(Stage stage, Preferences preferences){
		super(stage,EXTENSION_DESCRIPTION,EXTENSION);
		this.preferences = preferences;
	}
	
	private boolean isSingleElem(Object obj){
		return obj.getClass()==String.class;
	}
	
	private void saveSingleElem(String tagName, Object value){
		Element elem = doc.createElement(tagName);
		elem.appendChild(doc.createTextNode(value.toString()));
		rootElem.appendChild(elem);
	}
	
	private void saveListElem(String tagName, ObservableList<Object> values){
		Element elem = doc.createElement(tagName);
		for(Object val: values){
			Element row = doc.createElement("row");
			row.appendChild(doc.createTextNode(val.toString()));
			elem.appendChild(row);
		}
		rootElem.appendChild(elem);
	}

	@SuppressWarnings("unchecked")
	public void saveInfo(Document doc) {
		this.doc = doc;
        rootElem = doc.createElement("preferences");
        doc.appendChild(rootElem);
        
		Map<String,Object> prefMap = preferences.getPreferenceMap();
		for(String prefName: prefMap.keySet()){
			if(isSingleElem(preferences.getPreferenceMap().get(prefName))){
				saveSingleElem(prefName,prefMap.get(prefName));
			}
			else{
				saveListElem(prefName,(ObservableList<Object>)prefMap.get(prefName));
			}
		}
	}
	
	public void saveFile(String filePath){
		try{
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        dbFactory.setIgnoringElementContentWhitespace(true);
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();	        
	        doc = dBuilder.newDocument();  
	        
	        saveInfo(doc);
	        
	        TransformerFactory transformerFactory =
	                TransformerFactory.newInstance();
            Transformer transformer =
            transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
		} catch(Exception e){	
		}			
	}
	
}
