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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

public class Tags_Cepaberto extends JosmAction {  
    
    static final String baseUrl = "https://www.cepaberto.com/api/v3/nearest?";

    protected static String[] objectTypesToCheckforDuplicates = {"way", "node", "relation"};
    protected static String streetTypeTagPlaceholder = "___street_type_tag___";
    protected static String overpassBaseUrl = "https://overpass-api.de/api/interpreter";

    public Tags_Cepaberto() {
          
         super(tr("Add postcode tag - Cep Aberto"), new ImageProvider("postcode_icon.png"), tr("Add postcode information to selected objects."),
               Shortcut.registerShortcut("Add postcode tag - Cep Aberto", tr("Add postcode tag - Cep Aberto"),
                        KeyEvent.VK_A, Shortcut.CTRL_SHIFT), false, "AddPostcode",
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
                    + "lat=" + URLEncoder.encode(DecimalDegreesCoordinateFormat.INSTANCE.latToString(center), "UTF-8")
                    + "&lng=" + URLEncoder.encode(DecimalDegreesCoordinateFormat.INSTANCE.lonToString(center), "UTF-8")
  
            ); 
           
           
            URLConnection urlCon = url.openConnection();
            
             urlCon.setRequestProperty("Authorization", "Token token=66acd61c3fce9cdf2cfde02ccdb71a2e");
            urlCon.setRequestProperty("User-Agent", 
   "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
           urlCon.connect();
     
			
		
            final JsonObject json;
            try (
                
                InputStream inputStream = urlCon.getInputStream();
            BufferedInputStream reader = new BufferedInputStream(inputStream);
                    
                    
                
                
                    
                    JsonReader jreader = Json.createReader(reader)) {
                json = jreader.readObject();
              //  json = "teste1.json";
            }
            
         

           
           
         
          String postcode = json.getString("cep","");
          if (!"".equals(postcode)){
              if (selectedObject.get("addr:postcode") == null){
	      selectedObject.put("addr:postcode", postcode); 
              }
              
          }
          
          
            
          String cidade = json.getString("cidade","");
          if (!"".equals(postcode)){
              if (selectedObject.get("addr:city") == null){
	      selectedObject.put("addr:city", cidade); 
              }
              
          }
          
          
             String bairro = json.getString("bairro","");
          if (!"".equals(bairro)){
              if (selectedObject.get("addr:suburb") == null){
	      selectedObject.put("addr:suburb", bairro); 
              }
              
          }
          
            String logradouro = json.getString("logradouro","");
          if (!"".equals(logradouro)){
              if (selectedObject.get("addr:neighbourhood") == null){
	      selectedObject.put("addr:neighbourhood", logradouro); 
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
                               "</strong>" + tr("Falha na VerificaÃ§Ã£o do EndereÃ§o") + "<strong>" + exception.toString()
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
        /*String bbox ="[bbox:" +
                (position.getY() - 0.075) + "," +
                (position.getX() - 0.1  ) + "," +
                (position.getY() + 0.075) + "," +
                (position.getX() + 0.1  ) + "]";*/
        
         String bbox ="[bbox:" +
                (position.getY()) + "," +
                (position.getX()) + "," +
                (position.getY()) + "," +
                (position.getX()) + "]";

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
                        urls.add("<URL nÃ£o encontrada>");
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
                        tr("Falha na busca por endereÃ§os duplicados.") +
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
    

  

  
