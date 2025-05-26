package classes;

import java.time.LocalDate;

public class Artist {
    private String nume;
    private LocalDate data;
    private String descriere;
    private Double views;

    public Artist(String nume, LocalDate data, String descriere, Double views) {
        this.nume = nume;
        this.data = data;
        this.descriere = descriere;
        this.views = views;
    }

    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }
    public String getDescriere() {
        return descriere;
    }
    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
    public Double getViews() {
        return views;
    }
    public void setViews(Double views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return nume + " va fi prezent la eveniment pe data de " + data +"\nDetalii despre artist: \n" + descriere + "\nViews: " + views + "M\n\n";
    }
}
