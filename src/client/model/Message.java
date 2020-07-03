/*
 * @Isaac
 */
package client.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Isaac
 */
public class Message implements Serializable{
    private String content;
    private String emetteur;
    private String date;
    private String type = "MESSAGE";

    static Calendar c = GregorianCalendar.getInstance();
    
    public static String getTime(){
         c.setTime(new Date());
         return String.format("%2d : %2d",c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
    }
    
    public Message(){
        content="";
        date = getTime();
        emetteur="";
    }

    public Message(String content,String em) {
        this();
        this.content = content;
        emetteur = em;
    }
    
    public Message(String content, String emetteur, String date) {
        this.content = content;
        this.emetteur = emetteur;
        this.date = date;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(String emetteur) {
        this.emetteur = emetteur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    
}
