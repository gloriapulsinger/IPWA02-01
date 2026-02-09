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
public class GeisternetzDAO {
    private EntityManagerFactory emf;
    
    public GeisternetzDAO(){
        emf = Persistence.createEntityManagerFactory("ghostnetTrackerPU");
    }
    
    //gibt alle Geisternetze zurück
    public List<Geisternetz> findAll(){
        EntityManager em = emf.createEntityManager();
        try{
            Query abfrage = em.createQuery("SELECT g FROM Geisternetz g "
                    + "LEFT JOIN FETCH g.meldungen m " 
                    +" LEFT JOIN FETCH m.person",    Geisternetz.class);
            List<Geisternetz> alleNetze = abfrage.getResultList();
            return alleNetze;
        } finally{
            em.close();
        }   
    }
    
    public Geisternetz findById(Long netzID){
    EntityManager em = emf.createEntityManager();
        try{
            return em.find(Geisternetz.class, netzID);
        } finally{
            em.close();
        }   
    }
    
    
    //speichert Geisternetz in der Datenbank
    public Geisternetz add(Geisternetz geisternetz){
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        
        double groesseDelta = geisternetz.getGroesse()* 0.1f;
        double minGroesse = geisternetz.getGroesse() - groesseDelta;
        double maxGroesse = geisternetz.getGroesse() + groesseDelta;
    
        Geisternetz result;
        try{
            Query abfrage = em.createQuery("SELECT g FROM Geisternetz g "
                    + "WHERE g.groesse BETWEEN :minGroesse AND :maxGroesse "
                    + "AND (6371000 * acos(cos(radians(:latitude)) * cos(radians(g.latitude)) * " 
                    + "cos(radians(g.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(g.latitude)))) < :radius "
                    + "ORDER BY (6371000 * acos(cos(radians(:latitude)) * cos(radians(g.latitude)) * " 
                    + "cos(radians(g.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(g.latitude)))) * 0.7"
                    + "+ abs(g.groesse - :groesse) * 0.3 ASC", Geisternetz.class);
            abfrage.setParameter("longitude", geisternetz.getLongitude());
            abfrage.setParameter("latitude", geisternetz.getLatitude());
            abfrage.setParameter("radius", 200);
            abfrage.setParameter("minGroesse", minGroesse);
            abfrage.setParameter("maxGroesse", maxGroesse);
            abfrage.setParameter("groesse", geisternetz.getGroesse());
            
            
            //ausgeklammert beim schreiben
            //abfrage.setMaxResults(1);
            
            List<Geisternetz> resultList = abfrage.getResultList();
            Geisternetz netzExists;
            t.begin();
            if (resultList.isEmpty()){
                em.persist(geisternetz);
                result = geisternetz;
            }else{
               netzExists = resultList.get(0);
               netzExists.setGroesse(geisternetz.getGroesse());
               netzExists.setLatitude(geisternetz.getLatitude());
               netzExists.setLongitude(geisternetz.getLongitude());
               result = em.merge(netzExists);
            }
                
            t.commit();
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
        return result;
    }
    
    
    //updates bereits in der Datenbank gespeichertes Geisternetz
    public void update(Geisternetz geisternetz){
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        try{
        t.begin();
            em.merge(geisternetz);
        t.commit();
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    /*loeschen eine geisternetztes beinhaltet:
        #loeschen aller Meldungen fuer das netz damit auch loeschen der Meldung bei Person
        #loeschen bergende Netze
    */
    public void delete(Geisternetz geisternetz){
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        Person person;
        try{
        t.begin();
            geisternetz = em.merge(geisternetz);
            
            if (geisternetz.getBergendePerson() != null){
                person = geisternetz.getBergendePerson();
                person.getZuBergendeNetze().remove(geisternetz);
                geisternetz.setBergendePerson(null);
            }
           
            em.remove(geisternetz);
        t.commit();
        }catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        } finally {
            em.close();
        }
    
    
    }
}
