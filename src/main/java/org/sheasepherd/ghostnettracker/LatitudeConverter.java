/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.sheasepherd.ghostnettracker;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;

/**
 *
 * @author gloriapulsinger
 */
@FacesConverter("latConverter")
public class LatitudeConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String dms){
        
        if(dms == null || dms.isBlank()){
            return null;
        }
       
        try{
            String[] dmsTeile = dms.split("[°'\"]");
            int grad = Integer.parseInt(dmsTeile[0].trim());
            int min = Integer.parseInt(dmsTeile[1].trim());
            double sek = Double.parseDouble(dmsTeile[2].trim());
            char dir = dmsTeile[3].trim().charAt(0);

            double dezimal = grad + (min / 60.0) + (sek / 3600.0);
            if(min < 0 || min > 59 || sek < 0 || sek >= 60 || (dir != 'N' && dir != 'S') || dezimal < -90 || dezimal > 90){
            throw new IllegalArgumentException();
        }   
        
        if (dir == 'S') {
            dezimal *= -1;
        }
        
        return dezimal;
             
        }catch (Exception e) {
            throw new ConverterException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ungültige Latitude. Bitte im DMS-Format eingeben, z.B. 48°12'30\"N.",
                    null
            ));
        }
        
    }
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object wert){
         if (wert == null) return "";
         
         if (!(wert instanceof Double)) {
             throw new ConverterException(
                "Expected Double but got " + wert.getClass()
            );
        }

        double dezimal = (Double) wert;
        
        
        
        double absDezimal = Math.abs(dezimal);
        int grad = (int) absDezimal;
        double rest = (absDezimal - grad) * 60;
        int min = (int) rest;
        double sek = (rest - min) * 60;
        
        char dir = 'S';
        if(dezimal>=0){
            dir = 'N';
        }
       
 
        
        return String.format("%d°%d'%.2f\"%c", grad, min, sek, dir);
    }

}
