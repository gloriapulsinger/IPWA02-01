/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.sheasepherd.ghostnettracker;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author gloriapulsinger
 */
@Named(value = "meldeController")
@ViewScoped
public class MeldeController implements Serializable{

    @Valid
    private Person person = new Person();
    private final PersonDAO personDAO = new PersonDAO();
    
    private Geisternetz geisternetz= new Geisternetz ();
    private final GeisternetzDAO geisternetzDAO = new GeisternetzDAO();
    
    private Meldung meldung = new Meldung();
    private final MeldungDAO meldungDAO = new MeldungDAO();
    
    //zum ein- und ausblenden des formulars
    private boolean submitted = false;
    
    /**
     * Creates a new instance of MeldeController
     */
    public MeldeController() {
    }
    
    public Geisternetz getGeisternetz() {
    return geisternetz;
    }

    public void setGeisternetz(Geisternetz geisternetz) {
        this.geisternetz = geisternetz;
    }
    
    public Person getPerson() {
    return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
    public Meldung getMeldung() {
    return meldung;
    }

    public void setMeldung(Meldung meldung) {
        this.meldung = meldung;
    }
    
    public boolean isSubmitted() { 
        return submitted; 
    }
    
    public void setSubmitted(boolean submitted) { 
        this.submitted = submitted; 
    }

    
    public void save(){
        
        if(person.getTelefonnummer() == null || person.getTelefonnummer().isBlank()){
            person.setAnonym(true);
        }
        person.getRollen().add(Person.RollenTyp.MELDEND);
       
        geisternetz.setStatus(Geisternetz.NetzStatus.GEMELDET);
        
        meldung.setZeitpunkt(new Date());
        
        person = personDAO.add(person);
        geisternetz = geisternetzDAO.add(geisternetz);
        meldungDAO.add(meldung);
        personDAO.addGeisternetzToGemeldeteNetze(person, geisternetz, meldung);
        
        submitted=true;
       
        
    }
    
    public void resetForm() {
        person = new Person();
        geisternetz = new Geisternetz();
        meldung = new Meldung();
        submitted = false;  
    }
    
    
}
