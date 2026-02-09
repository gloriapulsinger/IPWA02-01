/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sheasepherd.ghostnettracker;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gloriapulsinger
 */
@Entity
public class Geisternetz implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long netzID;
    
    private Double latitude;
    private Double longitude;
    private Double groesse;
    
    @Enumerated(EnumType.STRING)
    private NetzStatus status;
    
    
    @OneToMany (mappedBy = "geisternetz",
    cascade = CascadeType.REMOVE,
    orphanRemoval = true)
    private List<Meldung> meldungen = new ArrayList<>();
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "personID")
    private Person bergendePerson;
    
   
    public Geisternetz (){
        
    }
    public Long getNetzID(){
        return netzID;
    
    }
    public void setNetzID(Long netzID){
        this.netzID = netzID;
    }
    
    public Double getLatitude(){
        return latitude;
    }
    
    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }
    
    public Double getLongitude(){
        return longitude;
    }
    
    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }
    
    public NetzStatus getStatus (){
        return status;
    }
    
    public void setStatus(NetzStatus status){
        this.status = status;
    }
    
    public Double getGroesse (){
        return groesse;
    }
    
    public void setGroesse(Double groesse){
        this.groesse = groesse;
    }
    
    
    public List<Meldung> getMeldungen (){
        return meldungen;
    }
    
    public void setMeldungen (List<Meldung> meldungen){
        this.meldungen = meldungen;
    }
    
    public Person getBergendePerson (){
        return bergendePerson;
    }
    
    public void setBergendePerson(Person bergendePerson){
        this.bergendePerson = bergendePerson;
    }
    
    public enum NetzStatus{
        GEMELDET,
        BERGUNGBEVORSTEHEND,
        GEBORGEN,
        VERSCHOLLEN,
    }
    
    
}
