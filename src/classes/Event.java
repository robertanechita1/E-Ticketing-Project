package classes;

import java.time.LocalDate;

public class Event implements IEvent {
    private String nume;
    private LocalDate data;
    private String descriere;
    private String locatie;
    private int numarBileteDisponibile;
    private int capacitateTotala;
    private String organizator;
    private double price;

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
        return "Eveniment: " + nume + " | Data: " + data + " | Locatie: " + locatie +
                " | Bilete disponibile: " + numarBileteDisponibile + "/" + capacitateTotala;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }
}

