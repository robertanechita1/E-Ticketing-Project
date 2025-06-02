package classes.Interfaces;
import java.time.LocalDate;
import classes.Artist;

public interface IEvent {
    void setNume(String nume);
    void setData(LocalDate data);
    void setDescriere(String descriere);
    void setLocatie(String locatie);
    void setNumarBileteDisponibile(int numar);
    void setCapacitateTotala(int capacitate);
    void setOrganizator(String organizator);
    void setPrice(double price);


    String getNume();
    LocalDate getData();
    String getDescriere();
    String getLocatie();
    int getNumarBileteDisponibile();
    int getCapacitateTotala();
    String getOrganizator();
    double getPrice();

}
