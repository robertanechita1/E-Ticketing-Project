package classes;


import java.util.List;
import java.util.Scanner;

public interface IUser {
    String getNume();

    String getPass();

    int getVarsta();

    List<Bilet> getBilete();
    List<Notificare> getNotificari();
    List<Event> getEventuriSugerate();

    void adaugaBilet(Bilet b);
    void adaugaNotificare(Notificare n);
    void setRole(String role);
    String getRole();

}
