package main;

import classes.Event;
import classes.Recenzie;
import classes.User;
import db.GenericDao;
import services.MainService;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Set<User> users = new LinkedHashSet<>();
        List<Event> events = new ArrayList<>();
        List<Event> solicitari = new ArrayList<>();
        List<Recenzie> recenzii = new ArrayList<>();
        int actiune = 11;
        User user = MainService.LoggedOut(scanner);
        if(user == null)
            return;
        actiune = MainService.MeniuUser(scanner, user);
        while(true){
            switch (actiune) {
                case 1:
                    MainService.AfisareEvenimente();
                    break;
                case 2:
                    System.out.print("La ce eveniment doriti sa mergeti?\n");
                    String numeEveniment = scanner.next();

                    GenericDao<Event> eventDao = GenericDao.getInstance(Event.class);
                    Optional<Event> eventOpt = eventDao.findByField(numeEveniment, "nume");

                    if (eventOpt.isPresent()) {
                        Event e = eventOpt.get();
                        MainService.cumparaBilet(scanner, user, e);
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
                    MainService.afiseazaNotificariUser(user);
                    break;
                case 10:
                    user = MainService.LoggedOut(scanner);
                    break;
                case 11:
                    return;
                case 12:
                    MainService.adaugaEvent(scanner);
                    break;
                case 13:
                    MainService.anuleazaEvent(scanner, user);
                    break;
                case 14:
                    MainService.gestioneazaSolicitari(scanner, solicitari);
                    break;
                case 15:
                    MainService.trimiteNotificare(scanner, user); //TO DO
                    break;
                case 16:
                    MainService.Lineup(scanner); // TO CHECK, PT CA NU DA ERROR DAR NU AFISEAZA DECI CRED CA E DE LA AFISEAZA EVENIMENTE
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

