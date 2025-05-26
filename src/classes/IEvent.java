package classes;
import java.time.LocalDate;
import java.util.List;

public interface IEvent {
    void setNume(String nume);
    void setData(LocalDate data);
    void setDescriere(String descriere);
    void setLocatie(String locatie);
    void setNumarBileteDisponibile(int numar);
    void setCapacitateTotala(int capacitate);
    void setOrganizator(String organizator);
    void setPrice(double price);
    void actLineup(Artist artist);


    String getNume();
    LocalDate getData();
    String getDescriere();
    String getLocatie();
    int getNumarBileteDisponibile();
    int getCapacitateTotala();
    String getOrganizator();
    double getPrice();
    void getLineup();
}
