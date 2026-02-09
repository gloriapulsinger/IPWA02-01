/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.sheasepherd.ghostnettracker;

import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

/**
 *
 * @author gloriapulsinger
 */
@Named(value = "indexController")
@RequestScoped
public class IndexController {

    /**
     * Creates a new instance of IndexController
     */
    public IndexController() {
    }
    
    // Button-Aktionen
    public String zurMeldeseite() {
        return "meldeformular.xhtml?faces-redirect=true";
    }

    public String zurBergungsseite() {
        return "bergenetze.xhtml?faces-redirect=true";
    }
    
}
