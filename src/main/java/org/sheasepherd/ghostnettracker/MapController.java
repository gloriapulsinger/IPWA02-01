/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.sheasepherd.ghostnettracker;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.util.List;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author gloriapulsinger
 */
@Named(value = "mapController")
@ViewScoped
public class MapController implements Serializable{

    private MapModel simpleModel;
    private Marker<String> marker;
     
    private GeisternetzDAO geisternetzDAO = new GeisternetzDAO();
    
    private List<Geisternetz> alleNetze;
    
    private Geisternetz selectedNetz; 
    
    private Long selectedNetzId;
    private int selectedPage;
    
    
    /**
     * Creates a new instance of MapController
     */
    public MapController() {
    }
    
    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel<>();
        
        alleNetze= geisternetzDAO.findAll();
        
        
        for(Geisternetz netz : alleNetze){
            Marker marker = new Marker(new LatLng(netz.getLatitude(), netz.getLongitude()),
                    netz.getNetzID().toString(), netz.getNetzID());

            switch (netz.getStatus()) {
                case GEBORGEN:
                    marker.setIcon("/ghostnetTracker/faces/jakarta.faces.resource/iconGreen.png?ln=images");
                    break;
                case GEMELDET:
                    marker.setIcon("/ghostnetTracker/faces/jakarta.faces.resource/iconRed.png?ln=images");
                    break;
                case BERGUNGBEVORSTEHEND:
                    marker.setIcon("/ghostnetTracker/faces/jakarta.faces.resource/iconYellow.png?ln=images");
                    break;
                default:
                    marker.setIcon("/ghostnetTracker/faces/jakarta.faces.resource/iconGrey.png?ln=images");
                    break;
            }
            simpleModel.addOverlay(marker);
        }

    }  

    public MapModel<Long> getSimpleModel() {
        return simpleModel;
    }
    
    public Long getSelectedNetzId() { 
        return selectedNetzId; 
    }
    
    public int getSelectedPage() { 
        return selectedPage; 
    }
    
    public void onMarkerSelect(OverlaySelectEvent<String> event) {
        Marker markerSelect = (Marker) event.getOverlay();

        selectedNetzId = (Long) markerSelect.getData();
        selectedNetz = geisternetzDAO.findById(selectedNetzId);
        
        selectedPage = getPageIndex(selectedNetzId);
    }
    
    public Marker<String> getMarker() {
        return marker;
    }
    
      public Geisternetz getSelectedNetz() {
        return selectedNetz;
    }

    public void setSelectedNetz(Geisternetz selectedNetz) {
        this.selectedNetz = selectedNetz;
        
    }
    
    public int getPageIndex(Long netzID) {
        int rowsPerPage = 10;

        for (int i = 0; i < alleNetze.size(); i++) {
            if (alleNetze.get(i).getNetzID().equals(netzID)) {
                return i / rowsPerPage;
            }
        }
        return 0;
    }
    
}
