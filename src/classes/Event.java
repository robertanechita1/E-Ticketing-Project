package classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Event implements IEvent {
    private String nume;
    private LocalDate data;
    private String descriere;
    private String locatie;
    private int numarBileteDisponibile;
    private int capacitateTotala;
    private String organizator;
    private double price;
    private List<Artist> lineup = new ArrayList<>();

    public Event() {
        this.lineup = new ArrayList<>();
    }


    public Event(String nume, LocalDate data, String descriere, String locatie,
                     int capacitateTotala, String organizator) {
        this.nume = nume;
        this.data = data;
        this.descriere = descriere;
        this.locatie = locatie;
        this.capacitateTotala = capacitateTotala;
        this.numarBileteDisponibile = capacitateTotala; // initial toate biletele sunt disponibile
        this.organizator = organizator;
    }

    @Override
    public String getNume() {
        return nume;
    }

    @Override
    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public LocalDate getData() {
        return data;
    }

    @Override
    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public String getDescriere() {
        return descriere;
    }

    @Override
    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    @Override
    public String getLocatie() {
        return locatie;
    }

    @Override
    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    @Override
    public int getNumarBileteDisponibile() {
        return numarBileteDisponibile;
    }

    @Override
    public void setNumarBileteDisponibile(int numar) {
        this.numarBileteDisponibile = numar;
    }

    @Override
    public int getCapacitateTotala() {
        return capacitateTotala;
    }

    @Override
    public void setCapacitateTotala(int capacitate) {
        this.capacitateTotala = capacitate;
    }

    @Override
    public String getOrganizator() {
        return organizator;
    }

    @Override
    public void setOrganizator(String organizator) {
        this.organizator = organizator;
    }
    @Override
    public String toString() {
        String lnp = "In curand...";
        if(!lineup.isEmpty()) {
            lnp = lineup.toString().replace("[", "").replace("]", "");
        }
        return "Eveniment: " + nume + " | Data: " + data + " | Locatie: " + locatie +
                " | Bilete disponibile: " + numarBileteDisponibile + "/" + capacitateTotala + "\nLine-up:\n" + lnp;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public void actLineup(Artist artist) {
        this.lineup.add(artist);
    }
    public void getLineup() {
        for(Artist a : lineup)
            System.out.println(a);

    }
}

