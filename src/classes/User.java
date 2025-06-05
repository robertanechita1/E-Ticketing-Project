package classes;

import java.util.ArrayList;
import java.util.List;

import classes.Interfaces.IUser;

public class User implements IUser {
    private String nume;
    private String pass;
    private String role;
    private int varsta;
    //private List<Bilet> bilete;
   // private List<Notificare> notificari;
    //private List<Event> eventuriSugerate;

    public User() {
    }


    public User(String nume, int varsta, String pass, String role) {
        this.nume = nume;
        this.varsta = varsta;
        this.pass = pass;
        this.role = role;
    }

    @Override
    public String getNume() {
        return nume;
    }

    @Override
    public String getRole(){
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getPass() {
        return pass;
    }
    @Override
    public int getVarsta() {
        return varsta;
    }

    @Override
    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    @Override
    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public void getNotificari(List<Notificare> notificari) {
        boolean found = false;
        for(Notificare n : notificari) {
            if(n.getReceptor().getNume().equals(nume)) {
                System.out.println(n);
                found = true;
            }
        }
        if(!found)
            System.out.println("Nu aveti notificari.\n");
    }

    @Override
    public void getEventuriSugerate(List<Event> events) {
        boolean found = false;
        for(Event e : events) {
            if(e.getNume().equals(nume)) {
                System.out.println(e);
                found = true;
            }
        }
        if(!found)
            System.out.println("Nu aveti eventuri sugerate\n");

    }


    @Override
    public String toString() {
        return "User-ul: " +
                "nume='" + nume + '\'' +
                ", varsta=" + varsta ;
    }
}