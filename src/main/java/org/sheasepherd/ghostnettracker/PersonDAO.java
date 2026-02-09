/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sheasepherd.ghostnettracker;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gloriapulsinger
 */
public class PersonDAO {
     private EntityManagerFactory emf;
     
     public PersonDAO(){
         emf = Persistence.createEntityManagerFactory("ghostnetTrackerPU");
     }
    
    //gibt alle Personen zurück
    public List<Person> findAll(){
        EntityManager em = emf.createEntityManager();
        try{
            Query abfrage = em.createQuery("select a from Person a");
            List<Person> allePersonen = abfrage.getResultList();
            return allePersonen;
        }
        finally{
            em.close();
        }
        
    }
    
    //speichert Person in der Datenbank (und überprüft davor ob eine person mit diese namen schon exixstiert und überschreibt dann die Daten)
    public Person add(Person person){
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        Person result;
        
        try {
        
            String vorname = person.getVorname().trim();
            String nachname = person.getNachname().trim();


            Query abfrage = em.createQuery(
                "SELECT p FROM Person p WHERE LOWER(p.vorname) = LOWER(:vorname) AND LOWER(p.nachname) = LOWER(:nachname)",
                Person.class
            );
            abfrage.setParameter("vorname", vorname);
            abfrage.setParameter("nachname", nachname);

            List<Person> resultList = abfrage.getResultList();

            t.begin();

            if (!resultList.isEmpty()) {
                Person personExists = resultList.get(0);
                
                if (person.getRollen() != null) {
                    personExists.getRollen().addAll(person.getRollen());
                }

                // Aktualisiere bestehende Person
                if (!person.getTelefonnummer().isBlank()) {
                    personExists.setTelefonnummer(person.getTelefonnummer());
                    personExists.setAnonym(false);
                } else if (!personExists.getTelefonnummer().isBlank()) {
                    personExists.setAnonym(false);
                }
                result = em.merge(personExists);
            } else {
                // Neue Person speichern
                em.persist(person);
                result = person;
            }

            t.commit();
        } catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
        return result;
    }
    
    //updates bereits in der Datenbank gespeicherte Person
    public void update(Person person){
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        try{
        t.begin();
            em.merge(person);
        t.commit();
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
        
    }
    
    //entfernt Person aus der Datenbank
    public void delete(Person person){
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        try{
        t.begin();
             person = em.merge(person);
            
            //meldung entfernen
            for (Meldung meldung : person.getGemachteMeldungen()) {
                meldung.setPerson(null);
            }
            //person.getGemachteMeldungen().clear();
            
            //netze entfernen
            for (Geisternetz netz : person.getZuBergendeNetze()) {
                netz.setBergendePerson(null);
            }
            //person.getZuBergendeNetze().clear();
            em.remove(person);
        t.commit();
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    //Speichert Verbindung aus Person, Meldung und gemeldetem Netz in der Datenbank
    public void addGeisternetzToGemeldeteNetze(Person person, Geisternetz netz, Meldung meldung){
        
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        try{
        t.begin();

            person =  em.merge(person);
            netz = em.merge(netz);
            meldung = em.merge(meldung);
            
            person.getGemachteMeldungen().add(meldung);
            netz.getMeldungen().add(meldung);
            meldung.setPerson(person);
            meldung.setGeisternetz(netz);
        t.commit();
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    //löscht Verbindung aus Person und gemeldetem Netz in der Datenbank
    public void deleteGeisternetzFromGemeldeteNetze(Person person, Geisternetz netz, Meldung meldung){
        
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        try{
            t.begin();
                person =  em.merge(person);
                netz = em.merge(netz);
                meldung = em.merge(meldung);

                person.getGemachteMeldungen().remove(meldung);
                netz.getMeldungen().remove(meldung);
                meldung.setPerson(null);
                meldung.setGeisternetz(null);
                
            t.commit();
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
        
    }
    
    
    //Speichert Verbindung aus Person und zu bergendem Netz in der Datenbank
    public void addGeisternetzToZuBergendeNetze(Person person, Geisternetz netz){
        
         EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        try{
        t.begin();

            person = em.merge(person);
            netz = em.merge(netz);
            
            person.getZuBergendeNetze().add(netz);
            netz.setBergendePerson(person);
            
        t.commit();
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    //löscht Verbindung aus Person und zu bergende Netz in der Datenbank
    public void deleteGeisternetzFromZuBergendeNetze(Person person, Geisternetz netz){
        
       EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        try{
            t.begin();
                netz = em.merge(netz);
                person = em.merge(person);
                
                person.getZuBergendeNetze().remove(netz);
                netz.setBergendePerson(null);

            t.commit();
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    //speichert Verbindugn aus Rolle und Person in Datenbank 
    //und speichert die Rolle in der Datenbank
    public Person addRolleForPerson(Person person, Person.RollenTyp rolle){
        
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        try{
            t.begin();
                person = em.merge(person);
                person.getRollen().add(rolle);


            t.commit();
            return person;
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    //löscht Verbindugn aus Rolle und Person in Datenbank 
    //und löscht die Rolle in der Datenbank
    public Person removeRolleForPerson(Person person, Person.RollenTyp rolle){
        
         EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        try{
            t.begin();
                person = em.merge(person);
                person.getRollen().remove(rolle);

            t.commit();
            return person;
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
