package classes.Interfaces;


import java.util.List;

import classes.Bilet;
import classes.Event;
import classes.Notificare;

public interface IUser {
    String getNume();

    String getPass();

    int getVarsta();

    void getNotificari(List<Notificare> notificari);
    void getEventuriSugerate(List<Event> events);

    void setRole(String role);
    String getRole();

}
