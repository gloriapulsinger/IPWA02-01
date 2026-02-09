/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sheasepherd.ghostnettracker;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author gloriapulsinger
 */
@Entity
public class Meldung implements Serializable {

     @Id
    @GeneratedValue( strategy  = GenerationType.AUTO)
    private Long meldungID;
    
    private Date zeitpunkt;
    
    @ManyToOne
    @JoinColumn(name = "geisternetzID")
    private Geisternetz geisternetz;
    @ManyToOne
    @JoinColumn(name = "personID")
    private Person person;
    
    public Meldung(){}
    
    public Long getMeldungID(){
        return meldungID;
    
    }
    public void setMeldungID(Long meldungID){
        this.meldungID =meldungID;
    
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
    
    public void setPerson(Person person){
        this.person =person;
    }
    
    public Date getZeitpunkt(){
        return zeitpunkt;
    }
    
    public void setZeitpunkt(Date zeitpunkt){
        this.zeitpunkt = zeitpunkt;
    }
}
