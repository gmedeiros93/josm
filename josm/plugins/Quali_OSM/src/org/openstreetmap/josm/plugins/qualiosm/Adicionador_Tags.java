package org.openstreetmap.josm.plugins.qualiosm;

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
import javax.swing.JOptionPane;


import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.command.ChangeCommand;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.command.SequenceCommand;
import org.openstreetmap.josm.data.UndoRedoHandler;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.coor.conversion.DecimalDegreesCoordinateFormat;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.tools.HttpClient;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.Shortcut;



public class Adicionador_Tags extends JosmAction {
    static final String baseUrl = "https://nominatim.openstreetmap.org/reverse?format=json";

    protected static String[] objectTypesToCheckforDuplicates = {"way", "node", "relation"};
    protected static String streetTypeTagPlaceholder = "___street_type_tag___";
    protected static String overpassBaseUrl = "https://overpass-api.de/api/interpreter";

    public Adicionador_Tags() {
         

        
         super(tr("Add Address Tags"), new ImageProvider("quality_icon.png"), tr("Adicionar tags de endereÃ§o"),
               Shortcut.registerShortcut("Add Address Tags", tr("Add Address Tags"),
                        KeyEvent.VK_A, Shortcut.CTRL_SHIFT), false, "AddEndereco",
                true);
       
    }
    @Override
    public void actionPerformed(ActionEvent event) {
        // Retorna o objeto selecionado
        final Collection<OsmPrimitive> sel = MainApplication.getLayerManager().getEditDataSet().getAllSelected();

    
        
        new Notification(
                            "<strong>" + tr("Quali OSM Plugin") + "</strong><br />" +
                                    tr("Tags de endereco inseridas com sucesso!")) 
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
            UndoRedoHandler.getInstance().add(new SequenceCommand(trn("Adicionar Endereco", "Adicionar Endereco", commands.size()), commands));
        }
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
          
            //Selecao de um unico objeto
            //if (addressItems.size() > 0) {
          
          //String building = addressItems.getString("building","");
           //if (!"".equals(building)){
             // if (selectedObject.get("addr:building") == null){
	      //selectedObject.put("addr:building", building); 
            //  }
          // }
            
            
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
				
	 			
	 String country = addressItems.getString("country","");
          if (!"".equals(country)){
              if (selectedObject.get("addr:country") == null){
	      selectedObject.put("addr:country", country); 
              }
          }	
          
	  String state = addressItems.getString("state","");
           if (!"".equals(state)){
              if (selectedObject.get("addr:state") == null){
	      selectedObject.put("addr:state", state); 
              }
           }			

          String housenumber = addressItems.getString("housenumber","");
          if (!"".equals(housenumber)){
              if (selectedObject.get("addr:housenumber") == null){
	      selectedObject.put("addr:housenumber", housenumber); 
              }
          }    
                //final OsmPrimitive newObject = selectedObject instanceof Node
                       // ? new Node(((Node) selectedObject))
                      //  : selectedObject instanceof Way
                       // ? new Way((Way) selectedObject)
                      //  : selectedObject instanceof Relation
                       // ? new Relation((Relation) selectedObject)
                      //  : null;
                
	
                 new Notification(
                            "<strong>" + tr("Quali OSM Plugin") + "</strong><br />" +
                                    tr("Teste!")) 
                            .setDuration(12500)
                            .show();
                
           

            
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

        // Definicao da Bounding Box
        String bbox ="[bbox:" +
                (position.getY() - 0.075) + "," +
                (position.getX() - 0.1  ) + "," +
                (position.getY() + 0.075) + "," +
                (position.getX() + 0.1  ) + "]";

   

        // Verifica as tags do objeto
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
        // Verifica se apenas um objeto estÃ¡ selecionado.
        setEnabled(selection != null && selection.size() >= 1);
    }

   

    
}
