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
import java.io.FileReader;
import java.io.IOException;


import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import static jdk.nashorn.internal.objects.NativeMath.min;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


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

import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.Shortcut;


/******************************************************************
* The class Tag_Adder is derived from the class JosmAction,
* which is base class helper for all actions in JOSM.
* This class uses Nominatim tool for capturate address information
* from the objects in OpenStreetMap.
********************************************************************/

public class Tags_Correios extends JosmAction {  
    


    protected static String[] objectTypesToCheckforDuplicates = {"way", "node", "relation"};
    protected static String streetTypeTagPlaceholder = "___street_type_tag___";


    public Tags_Correios() {
          
         super(tr("Add postcode tag - Correios"), new ImageProvider("postcode_icon.png"), tr("Add postcode information to selected objects."),
               Shortcut.registerShortcut("Add postcode tag - Correios", tr("Add postcode tag - Correios"),
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
        
        
        
        
           JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) parser.parse(new FileReader(
                    "files/ceps_DF.json"));
                    //"files/ceps_rio2.json"));
        } catch (IOException ex) {
            Logger.getLogger(Tags_Correios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Tags_Correios.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    

 
           String latitudeObj = URLEncoder.encode(DecimalDegreesCoordinateFormat.INSTANCE.latToString(center));
           String longitudeObj = URLEncoder.encode(DecimalDegreesCoordinateFormat.INSTANCE.lonToString(center));
            
            double latObj = Double.parseDouble(latitudeObj); 
             double lonObj = Double.parseDouble(longitudeObj); 
          //  System.out.println("Latitude::::" + latitudeObj);
         //   System.out.println("Longitude::::" + longitudeObj);
             double smallest = 1; 
    

        for (Object o : jsonArray) {
            
            
            
            
            
            
            JSONObject objeto = (JSONObject) o;
            
              ArrayList<JSONObject> list = new ArrayList<>();

         Double latitude = (Double) objeto.get("latitude");
         
          
           //String iniciolat = latitude.substring(1,3);
          // String fimlat = latitude.substring(3,latitude.length());
          // String latitudecompleta =  iniciolat + "." + fimlat;
           
          // double latnum = Double.parseDouble(latitude); 
           
           
          Double longitude = (Double) objeto.get("longitude");
           //String iniciolon = longitude.substring(1,3);
          // String fimlon = longitude.substring(3,9);
          // String longitudecompleta = iniciolon + "." + fimlon;
          
           //double lonnum = Double.parseDouble(longitude); 
           
           
        // String logr_nome = (String) objeto.get("logr_nome");
         
       // Long cep = (Long) objeto.get("cep");
        Long cep = (Long) objeto.get("cep");
        
        String cepstr = Long.toString(cep);
        
        // Long bairro = (Long) objeto.get("bairro");
      // String bairro_str = Long.toString(bairro);
        
        
     
          
           double distancia = sqrt(pow(latObj - latitude,2) + pow(lonObj - longitude,2));
       
           
     
       
       
        if(distancia < 0.00008){
             
             System.out.println("Latitude = " + latitude);
             System.out.println("Longitude = " + longitude);
             System.out.println("distancia = " + distancia);
    
             
         
               if (selectedObject.get("addr:postcode") == null){
	      selectedObject.put("addr:postcode", cepstr); 
              }
               
          
   
               
               
               
               
            // System.out.println("Logradouro = " + logr_nome);
           } 
        
        
       
        
        }


        


     
    
        return null;
    }
    
    

    
    
    
     
    protected static ArrayList<String> getUrlsOfObjectsWithThatAddress(OsmPrimitive newObject, LatLon position) {
          
        ArrayList<String> urls = new ArrayList<>();

        final String header = "[out:json][timeout:10]";

       
        
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
    

  

  
