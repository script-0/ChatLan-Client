/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model;

import java.io.Serializable;

/**
 *
 * @author Isaac
 */
public class MyFile implements Serializable{
    
    private byte[] content;
    private String nameFile;
    private String type;
    private String emetteur;
    private String Date;

    public MyFile(byte[] content, String nameFile, String type, String emetteur, String Date) {
        this.content = content;
        this.nameFile = nameFile;
        this.type = type;
        this.emetteur = emetteur;
        this.Date = Date;
    }
    
    public MyFile(byte[] content, String nameFile, String type,String em) {
        this.content = content;
        this.nameFile = nameFile;
        this.type = type;
        this.emetteur = em;
        this.Date = Message.getTime();
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(String emetteur) {
        this.emetteur = emetteur;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }
    
    
    
}
