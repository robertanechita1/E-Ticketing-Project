package classes.Interfaces;


import java.util.List;

import classes.Bilet;
import classes.Event;
import classes.Notificare;

public interface IUser {
    String getNume();

    String getPass();
    void setPass(String pass);

    int getVarsta();
    void setVarsta(int varsta);

    void getNotificari(List<Notificare> notificari);
    void getEventuriSugerate(List<Event> events);

    void setRole(String role);
    String getRole();

}
