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
import java.util.List;

/**
 *
 * @author gloriapulsinger
 */
public class MeldungDAO {
    private EntityManagerFactory emf;
     
     public MeldungDAO(){
         emf = Persistence.createEntityManagerFactory("ghostnetTrackerPU");
     }
    
    //gibt alle Personen zurück
    public List<Meldung> findAll(){
        EntityManager em = emf.createEntityManager();
        try{
            Query abfrage = em.createQuery("select a from Meldung a");
            return abfrage.getResultList();
        }
        finally{
            em.close(); 
        }
    }
    
    public void add(Meldung meldung){
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        
        try {
            t.begin();
            em.persist(meldung);
            t.commit();
        } catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    //updates bereits in der Datenbank gespeicherte Person
    public void update(Meldung meldung){
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        try{
        t.begin();
            em.merge(meldung);
        t.commit();
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
        
    }
    
    public void delete(Meldung meldung){
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        try{
        t.begin();
            
            meldung = em.merge(meldung);
            //Person entfernen 
            meldung.setPerson(null);
            
            
            //Netz entfernen
            Geisternetz geisternetz = meldung.getGeisternetz();
            meldung.setGeisternetz(null);
            
            em.remove(meldung);
            em.flush(); 
            
            //falls die geloeschte Meldung die einzige war soll das netz geloescht werden
            if(geisternetz != null){
                if(geisternetz.getMeldungen().isEmpty()){
                     if (geisternetz.getBergendePerson() != null){
                        Person person = geisternetz.getBergendePerson();
                        person.getZuBergendeNetze().remove(geisternetz);
                        geisternetz.setBergendePerson(null);
                    }
                     em.remove(geisternetz);
                } 
            }
            
        t.commit();
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
     public List<Meldung> findAllSordetByID(Geisternetz geisternetz){
        EntityManager em = emf.createEntityManager();
        try{
            Query abfrage = em.createQuery("SELECT m FROM Meldung m WHERE m.geisternetz = :geisternetz " 
                    + "ORDER BY m.zeitpunkt DESC", Meldung.class);
            abfrage.setParameter("geisternetz", geisternetz);
            List<Meldung> fiveMeldungen = abfrage.getResultList();
            return fiveMeldungen;  
        }finally{
        em.close();
        } 
    }
}
