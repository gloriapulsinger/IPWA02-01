/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.sheasepherd.ghostnettracker;

import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gloriapulsinger
 */
@Named(value = "bergeController")
@ViewScoped
public class BergeController implements Serializable {

    @Valid
    private Person person = new Person();
    private PersonDAO personDAO = new PersonDAO();
    
    private GeisternetzDAO geisternetzDAO = new GeisternetzDAO();
    
    private MeldungDAO meldungDAO = new MeldungDAO();
    
    private Geisternetz selectedNetz; 
    private List<Geisternetz> alleNetze;
    private Map<Long, List<Meldung>> ersteFuenfMeldungen;
    private Map<Long, List<Meldung>> alleMeldungen;
    
    //speichert ob das formular zum eintragen zum bergen eines netztes angezeigt wird
    private Map<Long, Boolean> showBVForm = new HashMap<>();
    
    //speichert ob das formular vom bergen eines netzes angezeigt wird
    private Map<Long, Boolean> showGBForm = new HashMap<>();
    
    private Map<Long, Boolean> showAlleMeldungen = new HashMap<>();
     
     //zum filtern
     private SelectItem[] statusOptions;
     
    /**
     * Creates a new instance of MeldeController
     */
    public BergeController() {  
    }
    
    @PostConstruct
        public void init() {
            List<Geisternetz> alleNetzeIn = geisternetzDAO.findAll();
 
            alleNetze =alleNetzeIn;

            statusOptions = new SelectItem[] {
            new SelectItem("", "Alle"),  // leerer Wert = keine Filterung
            new SelectItem(Geisternetz.NetzStatus.GEMELDET, "Gemeldet"),
            new SelectItem(Geisternetz.NetzStatus.BERGUNGBEVORSTEHEND, "Bergung bevorstehend"),
            new SelectItem(Geisternetz.NetzStatus.GEBORGEN, "Geborgen"),
            new SelectItem(Geisternetz.NetzStatus.VERSCHOLLEN, "Verschollen")
            };
            ersteFuenfMeldungen = new HashMap<>();
            alleMeldungen = new HashMap<>();     

            for (Geisternetz netz : alleNetze) {
                List<Meldung> meldungen = meldungDAO.findAllSordetByID(netz);
                alleMeldungen.put(netz.getNetzID(), meldungen);
                
                List<Meldung> firstFive;
                if (meldungen.size() <= 5) {
                    firstFive = meldungen; // weniger als 5 Meldungen → alles nehmen
                } else {
                    firstFive = meldungen.subList(0, 5); // subList ist sehr effizient, teilt die Liste nur
                }
                
                ersteFuenfMeldungen.put(netz.getNetzID(), firstFive);
            }
             
        }
    
    
    public Person getPerson() {
    return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

     public Geisternetz getSelectedNetz() {
        return selectedNetz;
    }

    public void setSelectedNetz(Geisternetz selectedNetz) {
        this.selectedNetz = selectedNetz;
    }
    
    public void setAlleNetze(List<Geisternetz> alleNetze){
        this.alleNetze = alleNetze;   
        
    }
    public List<Geisternetz> getAlleNetze(){
        return alleNetze;   
        
    }
    
    public Map<Long, List<Meldung>> getAlleMeldungen() {
    return alleMeldungen;
}

    public void setAlleMeldungen(Map<Long, List<Meldung>> alleMeldungen) {
        this.alleMeldungen = alleMeldungen;
    }
    
    public Map<Long, List<Meldung>> getErsteFuenfMeldungen() {
        return ersteFuenfMeldungen;
    }

    public void setErsteFuenfMeldungen(Map<Long, List<Meldung>> ersteFuenfMeldungen) {
        this.ersteFuenfMeldungen = ersteFuenfMeldungen;
    }
    
    //funktionen zum aendern der sichtbarkeit des formular zum eintragen zum bergen
    public boolean bVFormVisible(Geisternetz netz) {
    return showBVForm.getOrDefault(netz.getNetzID(), false);
    }

    public void toggleBVForm(Geisternetz netz) {
        boolean current = showBVForm.getOrDefault(netz.getNetzID(), false);
        showBVForm.put(netz.getNetzID(), !current);
    }
    
    //funktionen zum aendern der sichtbarkeit des formulars zum eintragen einer erfolgreichen bergung
    public boolean gBFormVisible(Geisternetz netz) {
    return showGBForm.getOrDefault(netz.getNetzID(), false);
    }

    public void toggleGBForm(Geisternetz netz) {
        boolean current = showGBForm.getOrDefault(netz.getNetzID(), false);
        showGBForm.put(netz.getNetzID(), !current);
    }
    
    //funktionen zum umschalten ob alle meldungen sichtbar sind, oder nur fuenf
    public boolean alleMeldungenVisible(Geisternetz netz) {
    return showAlleMeldungen.getOrDefault(netz.getNetzID(), false);
    }

    public void toggleAlleMeldungen(Geisternetz netz) {
        boolean current = showAlleMeldungen.getOrDefault(netz.getNetzID(), false);
        showAlleMeldungen.put(netz.getNetzID(), !current);
    }
    
    public List<Meldung> angezeigteMeldungen(Geisternetz netz) {
        if (alleMeldungenVisible(netz)) {
            return alleMeldungen.get(netz.getNetzID());
        } else {
            return ersteFuenfMeldungen.get(netz.getNetzID());
        }
    }
    
    public boolean mehrAlsFuenfMeldungen(Geisternetz netz) {
        List<Meldung> alle = alleMeldungen.get(netz.getNetzID());
        return alle != null && alle.size() > 5;
    }
    
    //speichert das eine person sich zum bergen eingetragen hat
    public void bergungBevorstehendEintragen(Geisternetz geisternetz){
        
        person.getRollen().add(Person.RollenTyp.BERGEND);
        
        geisternetz.setStatus(Geisternetz.NetzStatus.BERGUNGBEVORSTEHEND);
        
        person = personDAO.add(person);
        personDAO.addGeisternetzToZuBergendeNetze(person, geisternetz);
        
        person = new Person();
        
        showBVForm.put(geisternetz.getNetzID(), false);
        
        alleNetze = geisternetzDAO.findAll();
    }
    
    //speichert das eine person ein netz geborgen hat
    public void geborgenEintragen(Geisternetz geisternetz){
        
        person.getRollen().add(Person.RollenTyp.BERGEND);
        
        geisternetz.setStatus(Geisternetz.NetzStatus.GEBORGEN);
        
        person = personDAO.add(person);
        personDAO.addGeisternetzToZuBergendeNetze(person, geisternetz);
        
        person = new Person();
        
        showGBForm.put(geisternetz.getNetzID(), false);
    }
    
    
    public SelectItem[] getStatusOptions() {
        return statusOptions;
    }
    
    public String getAktuelleForm(Geisternetz netz) {
    if (bVFormVisible(netz)) return "BV";
    if (gBFormVisible(netz)) return "GB";
    return "NONE";
}
    
    
}
