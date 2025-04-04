package main;
/*
* Lista cu actiuni/interogari:
    - vizualizare events restul anului
    - cumparare bilet
    - anulare bilet
    - vizualizare lista bilete achizitionate
    - trimitere solicitare adaugare in platforma event propriu
    - cautare event dupa loc, data, etc
    - adaugare de events in platforma (admin)
    - aprobare/respingere solicitari (admin)
    - gestionare nr de bilete disponibile (admin)
    - anulare event is notificare participanti (admin)

* Lista tipuri de obiecte:
    - event: data, locatie, bilete disp...
    - bilet: event, pret, tipBilet, user...
    - user: lista bilete, varsta, nume...
    - admin
    - locatie: adresa, capacitate...
    - recenzie: text, data, user....
    - notificare: text, emitator, receptor, data....
    - plata: status, suma, metoda de plata, ultimele 4 cifre card....
*
*
* */

import classes.Event;
import classes.Bilet;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        //  Cream un eveniment
        Event e = new Event(
                "Concert Rock",
                LocalDate.of(2025, 7, 20),
                "Un concert epic în aer liber.",
                "Arena Națională",
                5000,
                "OrganizatorX"
        );


        System.out.println("DETALII EVENIMENT:");
        System.out.println("Nume: " + e.getNume());
        System.out.println("Data: " + e.getData());
        System.out.println("Descriere: " + e.getDescriere());
        System.out.println("Locatie: " + e.getLocatie());
        System.out.println("Bilete disponibile: " + e.getNumarBileteDisponibile());
        System.out.println("Organizator: " + e.getOrganizator());

        // Testam setterii
        e.setDescriere("Concert de rock alternativ în aer liber.");
        e.setCapacitateTotala(6000);
        e.setNumarBileteDisponibile(6000); // presupunem ca s-a marit capacitatea

        System.out.println("\nDUPĂ UPDATE:");
        System.out.println(e); // toString()

        // Cream un bilet
        Bilet b = new Bilet(e.getNume(), "Ana Popescu", 150.0, "VIP");

        // Testam getterii biletului
        System.out.println("\nDETALII BILET:");
        System.out.println("Cod: " + b.getCodUnic());
        System.out.println("Eveniment: " + b.getEventName());
        System.out.println("Cumparator: " + b.getCumparator());
        System.out.println("Pret: " + b.getPret());
        System.out.println("Valid: " + b.esteValid());
        System.out.println("Tip: " + b.getTip());

        // anulam biletul
        b.anuleaza();
        System.out.println("Valid după anulare: " + b.esteValid());
    }
}

