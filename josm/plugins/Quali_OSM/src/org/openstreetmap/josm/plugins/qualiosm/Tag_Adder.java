/***************************************************
* File: Tag_Adder.java
* Author: Gabriel Franklin Braz de Medeiros
* Programa de Pos-Graduacao em Informatica
* University of Brasilia
* Professor Maristela Terto de Holanda
*****************************************************/

package org.openstreetmap.josm.plugins.qualiosm;

import java.awt.Font;
import java.awt.Label;
import static org.openstreetmap.josm.tools.I18n.tr;
import static org.openstreetmap.josm.tools.I18n.trn;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.command.ChangeCommand;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.command.SequenceCommand;
import org.openstreetmap.josm.data.UndoRedoHandler;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.coor.conversion.DecimalDegreesCoordinateFormat;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.tools.HttpClient;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.Shortcut;

/******************************************************************
* The class Tag_Adder is derived from the class JosmAction,
* which is base class helper for all actions in JOSM.
* This class uses Nominatim tool for capturate address information
* from the objects in OpenStreetMap.
********************************************************************/

public class Tag_Adder extends JosmAction {  
    
    static final String baseUrl = "https://nominatim.openstreetmap.org/reverse?format=json";

    protected static String[] objectTypesToCheckforDuplicates = {"way", "node", "relation"};
    protected static String streetTypeTagPlaceholder = "___street_type_tag___";
    protected static String overpassBaseUrl = "https://overpass-api.de/api/interpreter";

    public Tag_Adder() {
          
         super(tr("Add Address Tags"), new ImageProvider("quality_icon.png"), tr("Add address tags to selected objects."),
               Shortcut.registerShortcut("Add Address Tags", tr("Add Address Tags"),
                        KeyEvent.VK_A, Shortcut.CTRL_SHIFT), false, "AddEndereco",
                true);
       
    }
    @Override
    public void actionPerformed(ActionEvent event) {
        // Returns selected objects
       
 JFrame theFrame = new JFrame("Tag Adder");
 Label label = new Label("Please wait for the addition of address tags...");
 label.setFont(new Font("Tahoma", Font.PLAIN, 16));
 
 theFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 theFrame.add(label);
 theFrame.setSize(400, 100);
 theFrame.setVisible(true);
 long start = System.currentTimeMillis();
        
 final Collection<OsmPrimitive> sel = MainApplication.getLayerManager().getEditDataSet().getAllSelected();

 new Notification(
                            "<strong>" + tr("Quali OSM Plugin") + "</strong><br />" +
                                    tr("Address tags successfully inserted!")) 
                            .setDuration(12500)
                            .show();
        
      
        final List<Command> commands = new ArrayList<>();
        sel.stream().forEach((selectedObject) -> {
            OsmPrimitive newObject = loadAddress(selectedObject);
            if (newObject != null) {
                commands.add(new ChangeCommand(selectedObject, newObject));
            }
        });
        if (!commands.isEmpty()) {
            UndoRedoHandler.getInstance().add(new SequenceCommand(trn("Insert address", "Insert address", commands.size()), commands));
        }
        long finish = System.currentTimeMillis();
        long tag_adder_time = finish - start;
        System.out.println("Time to execute = " + tag_adder_time +" ms.");
        theFrame.setVisible(false);
       theFrame.dispose();  
    }
    
    public static OsmPrimitive loadAddress(OsmPrimitive selectedObject){
        boolean noExceptionThrown = false;
        Exception exception = null;

        LatLon center = selectedObject.getBBox().getCenter();

        try {
            URL url = new URL(baseUrl
                    + "&lat=" + URLEncoder.encode(DecimalDegreesCoordinateFormat.INSTANCE.latToString(center), "UTF-8")
                    + "&lon=" + URLEncoder.encode(DecimalDegreesCoordinateFormat.INSTANCE.lonToString(center), "UTF-8")
  
            ); 
			
		
            final JsonObject json;
            try (BufferedReader in = HttpClient.create(url)
                    .setReasonForRequest("Quali_OSM Plugin")
                    .setHeader("User-Agent", "Quali_OSM Plugin")
                    .connect()
                    .getContentReader();
                 JsonReader reader = Json.createReader(in)) {
                json = reader.readObject();
            }

            final JsonObject addressItems = json.getJsonObject("address");
          
            /*Selection of just one object
            //if (addressItems.size() > 0) {
          
          //String building = addressItems.getString("building","");
           //if (!"".equals(building)){
             // if (selectedObject.get("addr:building") == null){
	      //selectedObject.put("addr:building", building); 
            //  }
           }*/
            
            
             String street = addressItems.getString("street","");
          if (!"".equals(street)){
              if (selectedObject.get("addr:street") == null){
	      selectedObject.put("addr:street", street); 
              }
              
          }	
         
          String city = addressItems.getString("city","");
          if (!"".equals(city)){
              if (selectedObject.get("addr:city") == null){
	      selectedObject.put("addr:city", city); 
              }
              
          }
          
          String suburb = addressItems.getString("suburb","");
          if (!"".equals(suburb)){
              if (selectedObject.get("addr:suburb") == null){
	      selectedObject.put("addr:suburb", suburb); 
              }
              
          }
				

	  String postcode = addressItems.getString("postcode","");
           if (!"".equals(postcode)){
              if (selectedObject.get("addr:postcode") == null){
	      selectedObject.put("addr:postcode", postcode); 
               
              
              }
           }			
				
	 			
	 String neighborhood = addressItems.getString("neighborhood","");
          if (!"".equals(neighborhood)){
              if (selectedObject.get("addr:neighborhood") == null){
	      selectedObject.put("addr:neighborhood", neighborhood); 
              }
          }	
          
	  String building = addressItems.getString("building","");
           if (!"".equals(building)){
              if (selectedObject.get("addr:building") == null){
	      selectedObject.put("addr:building", building); 
              }
           }			

          String housenumber = addressItems.getString("housenumber","");
          if (!"".equals(housenumber)){
              if (selectedObject.get("addr:housenumber") == null){
	      selectedObject.put("addr:housenumber", housenumber); 
              }
          }    
             
                
            noExceptionThrown = true;
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            
            exception = e;
        } catch (IOException | NullPointerException e) {
            exception = e;
        } finally {
            if (!noExceptionThrown) {
                new Notification(
                        tr("Notification") +
                                "</strong>" + tr("Falha na VerificaÃƒÂ§ÃƒÂ£o do EndereÃƒÂ§o") + "<strong>" + exception.toString()
                )
                        .setIcon(JOptionPane.ERROR_MESSAGE)
                        .show();
            }
        }
        
    
        return null;
    }

    protected static ArrayList<String> getUrlsOfObjectsWithThatAddress(OsmPrimitive newObject, LatLon position) {
          
        ArrayList<String> urls = new ArrayList<>();

        final String header = "[out:json][timeout:10]";

        // Definition of the Bounding Box
        String bbox ="[bbox:" +
                (position.getY() - 0.075) + "," +
                (position.getX() - 0.1  ) + "," +
                (position.getY() + 0.075) + "," +
                (position.getX() + 0.1  ) + "]";

        // Verify tags from objects
        newObject.keySet().stream().forEach((key) -> {
        });

        StringBuilder filterBuilder = new StringBuilder();
        String filter = filterBuilder.toString();
        final String footer = "out body;";
        // Overpass API 
        String query = header + bbox + ";" + "(" + filter + ");" + footer;

        boolean noExceptionThrown = false;
        Exception exception = null;

        try {
            URL url = new URL(overpassBaseUrl
                    + "?data=" + URLEncoder.encode(query, "UTF-8")
            );

            final JsonObject json;

            try (BufferedReader in = HttpClient.create(url)
                    .setReasonForRequest("Quali_OSM Plugin")
                    .setHeader("User-Agent", "Quali_OSM Plugin")
                    .connect()
                    .getContentReader();
                JsonReader reader = Json.createReader(in)) {
                json = reader.readObject();
            }

            final JsonArray items = json.getJsonArray("elements");

            if (items.size() > 0) {
                for (JsonValue item: items) {
                    JsonObject itemObject = item.asJsonObject();

                    try {
                        String type = itemObject.getString("type");
                        int osmId = itemObject.getInt("id");

                        urls.add("https://www.openstreetmap.org/" + URLEncoder.encode(type, "UTF-8") + "/" + URLEncoder.encode(Integer.toString(osmId), "UTF-8"));
                    } catch (NullPointerException e) {
                        urls.add("<URL nÃƒÂ£o encontrada>");
                    }
                }
            }

            noExceptionThrown = true;
        } catch (MalformedURLException | UnsupportedEncodingException | NullPointerException e) {
            exception = e;
        } catch (IOException e) {
            exception = e;
        } finally {
            if (!noExceptionThrown) {
                new Notification(
                        tr("Falha na busca por endereÃƒÂ§os duplicados.") +
                                "<strong>" + tr("Quali_OSM Plugin") + "</strong>" + exception.toString()
                )
                .setIcon(JOptionPane.ERROR_MESSAGE)
                .show();

                urls = null;
            }
        }

        return urls;
    }
    
    @Override
    protected void updateEnabledState() {
        if (getLayerManager().getEditDataSet() == null) {
            setEnabled(false);
        } else {
            updateEnabledState(getLayerManager().getEditDataSet().getSelected());
        }
    }

    @Override
    protected void updateEnabledState(final Collection<? extends OsmPrimitive> selection) {
        // Verify if just one object is selected
        setEnabled(selection != null && selection.size() >= 1);
    }  
}
    

  

  
