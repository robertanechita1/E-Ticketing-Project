package main;
/*

Proiectul meu este o platforma care se ocupa cu gestionarea evenimentelor.

* Lista cu actiuni/interogari:
    - vizualizare events restul anului -done
    - cumparare bilet -done
    - anulare bilet -done
    - vizualizare lista bilete achizitionate -done
    - trimitere solicitare adaugare in platforma event propriu -done
    - cautare event dupa data -done
    - vezi recenzii -done
    - lasa o recenzie -done
    - vezi notificari -done
    - delogare -done
    - adaugare de events in platforma (admin) -done
    - aprobare/respingere solicitari (admin) -done
    - anulare event (admin) -done
    - notificare participanti (admin) -done

* Lista tipuri de obiecte:
    - event: data, locatie, bilete disp...
    - bilet: event, pret, tipBilet, user...
    - user: lista bilete, varsta, nume...
    - locatie: adresa, capacitate...
    - recenzie: text, data, user....
    - notificare: text, emitator, receptor, data....
    - plata: status, suma, metoda de plata, ultimele 4 cifre card....

*
* */

import classes.Event;
import classes.Bilet;
import classes.Recenzie;
import classes.User;
import services.MainService;

import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Set<User> users = new LinkedHashSet<>();
        List<Event> events = new ArrayList<>();
        List<Event> solicitari = new ArrayList<>();
        List<Recenzie> recenzii = new ArrayList<>();
        MainService.Initializare(users, events);
        int actiune = 11;
        User user = MainService.LoggedOut(scanner, users);
        if(user == null)
            return;
        actiune = MainService.MeniuUser(scanner, user);
        while(true){
            switch (actiune) {
                case 1:
                    MainService.AfisareEvenimente(events);
                    break;
                case 2:
                    System.out.print("La ce eveniment doriti sa mergeti?\n");
                    String numeEveniment = scanner.next();
                    for(Event e : events){
                        if(numeEveniment.equalsIgnoreCase(e.getNume())){
                            MainService.cumparaBilet(scanner, user, e);
                            break;
                        }
                    }
                    System.out.print("Nu exista acest eveniment.\n\n");
                    break;
                case 3:
                    MainService.anuleazaBilet(scanner, user, events);
                    break;
                case 4:
                    MainService.afiseazaBileteUser(user);
                    break;
                case 5:
                    MainService.cautaEvenimente(scanner, events);
                    break;
                case 6:
                    MainService.trimiteSolicitare(scanner, solicitari);
                    break;
                case 7:
                    MainService.veziRecenzii(recenzii);
                    break;
                case 8:
                    MainService.Recenzie(scanner, recenzii, user);
                    break;
                case 9:
                    MainService.afiseazaNotificariUser(user);
                    break;
                case 10:
                    user = MainService.LoggedOut(scanner, users);
                    break;
                case 11:
                    return;
                case 12:
                    MainService.adaugaEvent(scanner, events);
                    break;
                case 13:
                    MainService.anuleazaEvent(scanner, events, users, user);
                    break;
                case 14:
                    MainService.gestioneazaSolicitari(scanner, solicitari, events);
                    break;
                case 15:
                    MainService.trimiteNotificare(scanner, user, users);
                    break;

                default:
                    return;
            }
            if(user == null)
                return;
            actiune = MainService.MeniuUser(scanner, user);
        }

    }

}

