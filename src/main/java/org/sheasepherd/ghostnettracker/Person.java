/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sheasepherd.ghostnettracker;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gloriapulsinger
 */
@Entity
public class Person implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long personID;
    
    @NotBlank(message = "Name darf nicht leer sein")
    @Size(max = 50, message = "Maximal 50 Zeichen")
    @Pattern(
        regexp = "^$|\\p{L}+(?:['\\-]\\p{L}+)*",
        message = "Ungültiger Name")
    private String vorname;
    
    @NotBlank(message = "Name darf nicht leer sein")
    @Size(max = 50, message = "Maximal 50 Zeichen")
    @Pattern(
        regexp = "^$|\\p{L}+(?:[ '\\-]\\p{L}+)*",
        message = "Ungültiger Name") 
    private String nachname;
    
    @Pattern(
        regexp = "^$|\\+[1-9][0-9]{2,14}",
        message = "Ungültige Telefonnummer") 
    private String telefonnummer;
    private boolean anonym;
    
    @OneToMany(mappedBy="person")
    private List<Meldung> gemachteMeldungen = new ArrayList<>();
    
    @OneToMany(mappedBy="bergendePerson")
    private List<Geisternetz> zuBergendeNetze = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "Person_Rollen", joinColumns = @JoinColumn(name = "personID"))
    @Column(name = "rollen")
    private Set<RollenTyp> rollen= new HashSet<>();
   
    public Person (){ }
    
    public Long getPersonID(){
        return personID;
    
    }
    public void setPersonID(Long personID){
        this.personID = personID;
    
    }
    
    public String getVorname (){
        return vorname;
    }
    
    public void setVorname(String vorname){
        this.vorname =vorname;
    }
    
    public String getNachname (){
        return nachname;
    }
    
    public void setNachname(String nachname){
        this.nachname = nachname;
    }
    public String getTelefonnummer (){
        return telefonnummer;
    }
    
    public void setTelefonnummer(String telefonnummer){
        this.telefonnummer = telefonnummer;
    }
    
    public boolean getAnonym (){
        return anonym;
    }
    
    public void setAnonym(boolean anonym){
        this.anonym = anonym;
    }
    
    public List<Meldung> getGemachteMeldungen(){
        return gemachteMeldungen;
    }
    
    public void setGemachteMeldungen( List<Meldung> gemachteMeldungen){
        this.gemachteMeldungen = gemachteMeldungen;
    }
    
    public List<Geisternetz> getZuBergendeNetze(){
        return zuBergendeNetze;
    }
    
    public void setZuBergendeNetze( List<Geisternetz> zuBergendeNetze){
        this.zuBergendeNetze = zuBergendeNetze;
    }
    
    public Set<RollenTyp> getRollen(){
        return rollen;
    }
    
    public void setRollen(Set<RollenTyp> rollen){
        this.rollen = rollen;
    }
    
    public enum RollenTyp {
        MELDEND,
        BERGEND
    }
    
}
