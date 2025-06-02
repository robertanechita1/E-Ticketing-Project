package main;

import classes.Event;
import classes.Notificare;
import classes.Recenzie;
import classes.User;
import db.DatabaseContext;
import services.*;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {

        DatabaseContext.initializeDatabase();

        DatabaseContext readContext = DatabaseContext.getReadContext();
        DatabaseContext writeContext = DatabaseContext.getWriteContext();

        Scanner scanner = new Scanner(System.in);
        List<Event> solicitari = new ArrayList<>();
        List<Recenzie> recenzii = new ArrayList<>();
        List<Notificare>notificari = new ArrayList<>();
        int actiune = 11;
        User user = MainService.LoggedOut(scanner);
        if(user == null)
            return;
        actiune = MainService.MeniuUser(scanner, user);
        EventService eventService =  new EventService();
        BiletService biletService =  new BiletService();
        UserService userService =  new UserService();
        ArtistService artistService =  new ArtistService();
        while(true){
            switch (actiune) {
                case 1:
                    MainService.AfisareEvenimente();
                    break;
                case 2:
                    System.out.print("La ce eveniment doriti sa mergeti?\n");
                    String numeEveniment = scanner.next();

                    Optional<Event> eventOpt = eventService.read(numeEveniment);

                    if (eventOpt.isPresent()) {
                        Event e = eventOpt.get();
                        MainService.cumparaBilet(scanner, user, e, biletService, eventService);
                    }
                    else
                        System.out.print("Nu exista acest eveniment.\n\n");
                    break;

                case 3:
                    MainService.anuleazaBilet(scanner, user);
                    break;
                case 4:
                    MainService.afiseazaBileteUser(user);
                    break;
                case 5:
                    MainService.cautaEvenimente(scanner);
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
                    user.getNotificari(notificari);
                    break;
                case 10:
                    user = MainService.LoggedOut(scanner);
                    break;
                case 11:
                    return;
                case 12:
                    MainService.adaugaEvent(scanner, eventService);
                    break;
                case 13:
                    MainService.anuleazaEvent(scanner, user, eventService, biletService, userService, notificari);
                    break;//DE ADAUGAT STERGERE EVENT_ARTIST INAINTE DE STERGERE EVENT
                case 14:
                    MainService.gestioneazaSolicitari(scanner, solicitari, eventService);
                    break;
                case 15:
                    MainService.trimiteNotificare(scanner, user, notificari, userService);
                    break;
                case 16:
                    MainService.Lineup(scanner, eventService, artistService);
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

