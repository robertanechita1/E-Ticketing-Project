package classes;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class User implements IUser {
    private String nume;
    private String pass;
    private String role;
    private int varsta;
    private List<Bilet> bilete;
    private List<Notificare> notificari;
    private List<Event> eventuriSugerate;

    public User() {
    }

    public User(String nume, int varsta, String pass) {
        this.nume = nume;
        this.varsta = varsta;
        this.bilete = new ArrayList<>();
        this.notificari = new ArrayList<>();
        this.eventuriSugerate = new ArrayList<>();
        this.pass = pass;
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
    public List<Bilet> getBilete() {
        return bilete;
    }

    @Override
    public List<Notificare> getNotificari() {
        return notificari;
    }

    @Override
    public List<Event> getEventuriSugerate() {
        return eventuriSugerate;
    }

    @Override
    public void adaugaBilet(Bilet b) {
        bilete.add(b);
    }

    @Override
    public void adaugaNotificare(Notificare n) {
        notificari.add(n);
    }


    @Override
    public String toString() {
        return "User{" +
                "nume='" + nume + '\'' +
                ", varsta=" + varsta +
                ", bilete=" + bilete.size() +
                ", notificari=" + notificari.size() +
                ", evenimente sugerate=" + eventuriSugerate.size() +
                '}';
    }
}