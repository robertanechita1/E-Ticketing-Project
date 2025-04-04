package classes;
import java.time.LocalDate;

public interface IEvent {
    void setNume(String nume);
    void setData(LocalDate data);
    void setDescriere(String descriere);
    void setLocatie(String locatie);
    void setNumarBileteDisponibile(int numar);
    void setCapacitateTotala(int capacitate);
    void setOrganizator(String organizator);

    String getNume();
    LocalDate getData();
    String getDescriere();
    String getLocatie();
    int getNumarBileteDisponibile();
    int getCapacitateTotala();
    String getOrganizator();
    
}
