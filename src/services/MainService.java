package services;



import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;

import classes.*;
import java.util.Scanner;
import java.util.Set;

public class MainService {

    public static void cumparaBilet(Scanner scanner, User user, Event event) {
        if(event.getNumarBileteDisponibile() < 1){
            System.out.println("Ne pare rau, dar nu mai avem bilete momentan.\n\n");
            return;
        }

        System.out.print("Ce tip de bilet doriti?\n1.VIP\n2.GENERAL ACCESS\n3.ONE DAY PASS\n\n");
        int tip = scanner.nextInt();
        Bilet bilet = null;
        Plata plata = null;
        System.out.println("Scrieti numarul cardului cu care doriti sa efectati plata: ");
        scanner.nextLine();
        String nr = scanner.nextLine();

        switch (tip) {
            case 1:
                plata = new Plata(event.getPrice() + 70, nr, "Plata efectuata");
                bilet = new Bilet(event.getNume(), user.getNume(), "VIP", plata);
                break;
            case 2:
                plata = new Plata(event.getPrice() + 70, nr, "Plata efectuata");
                bilet = new Bilet(event.getNume(), user.getNume(), "GENERAL ACCESS", plata);
                break;
            case 3:
                OneDayPass biletP = null;
                System.out.print("In a cata zi doriti sa veniti la eveniment?\n");
                int ziAcces = scanner.nextInt();
                LocalDate dataAcces = event.getData().plusDays(ziAcces);
                System.out.print("Doriti camping?\n");
                scanner.nextLine();
                String r = scanner.nextLine();
                if(r.equals("da")) {
                    plata = new Plata(event.getPrice() - 70, nr, "Plata efectuata");
                    biletP = new OneDayPass(event.getNume(), user.getNume(), dataAcces, true, "ONE DAY PASS", plata);
                    user.adaugaBilet(biletP);
                    System.out.println("Bilet cumpărat cu succes pentru " + biletP.getEventName() +
                            ", de către " + user.getNume());
                }
                else {
                    plata = new Plata(event.getPrice() - 100, nr, "Plata efectuata");
                    biletP = new OneDayPass(event.getNume(), user.getNume(), dataAcces, false, "ONE DAY PASS", plata);
                    user.adaugaBilet(biletP);
                    System.out.println("Bilet cumpărat cu succes pentru " + biletP.getEventName() +
                            ", de către " + user.getNume());
                }
                return;

        }
        event.setNumarBileteDisponibile(event.getNumarBileteDisponibile() - 1);
        user.adaugaBilet(bilet);
        System.out.println("Bilet cumpărat cu succes pentru " + bilet.getEventName() +
                ", de către " + user.getNume());
    }

    public static void anuleazaBilet(Scanner scanner, User user, List<Event>events) {
        List<Bilet> bilete = user.getBilete();
        System.out.print("De la ce eveniment doriti sa anulati biletul?\n");
        String eventName = scanner.next();
        for(Bilet bilet : bilete) {
            if(bilet.getEventName().equalsIgnoreCase(eventName)) {
                bilet.setValid(false);
                for(Event e : events)
                    if(e.getNume().equalsIgnoreCase(eventName))
                        e.setNumarBileteDisponibile(e.getNumarBileteDisponibile() + 1);
                System.out.println("Biletul pentru evenimentul " + bilet.getEventName() + " a fost anulat.");
            }
            else
                System.out.println("Acest bilet nu aparține utilizatorului.");

        }
        return;

    }

    public static void trimiteSolicitare(Scanner scanner, List<Event> solicitariEvenimente) {

        System.out.print("Nume eveniment: ");
        scanner.nextLine();
        String nume = scanner.nextLine();

        System.out.print("Data (format yyyy-MM-dd): ");
        String dataStr = scanner.next();
        LocalDate data = LocalDate.parse(dataStr);

        System.out.print("Descriere: ");
        scanner.nextLine(); // consum \n de la inputul anterior
        String descriere = scanner.nextLine();

        System.out.print("Locatie: ");
        String locatie = scanner.nextLine();

        System.out.print("Capacitatea totala: ");
        int capacitate = scanner.nextInt();

        System.out.print("Organizator: ");
        scanner.nextLine(); // consum \n de la int ul ant
        String organizator = scanner.nextLine();

        System.out.print("Pretul unui bilet standard: ");
        double pret = scanner.nextDouble();

        Event event = new Event(nume, data, descriere, locatie, capacitate, organizator);
        event.setPrice(pret);

        solicitariEvenimente.add(event);

        System.out.println("Solicitarea a fost trimisa spre aprobare.\n\n");
    }

    public static void afiseazaBileteUser(User user) {
        System.out.println("Biletele lui " + user.getNume() + ":");
        for (Bilet b : user.getBilete()) {
            System.out.println(b);
        }
    }

    public static void afiseazaNotificariUser(User user) {
        System.out.println("Notificari pentru " + user.getNume() + ":\n");
        for (Notificare n : user.getNotificari()) {
            System.out.println(n);
        }
    }

    public static User LogIn(Scanner scanner, Set<User> users) {
        System.out.print("Introdu un username: ");
        String username = scanner.next();
        System.out.print("Introdu un password: ");
        String password = scanner.next();
        boolean exista = false;
        for(User user : users) {
            if(user.getNume().equals(username)){
                while(!user.getPass().equals(password)){
                    System.out.print("Reintrodu un password: ");
                    password = scanner.next();
                }
                exista = true;
                return user;
            }
        }
        if(!exista){
            System.out.println("Utilizatorul nu exista. Doriti sa creati un nou cont? ");
            username = scanner.next();
            if(username.equals("da"))
                return SignIn(scanner, users);
            else
                return null;
        }
        return null;
    }

    public static User SignIn(Scanner scanner, Set<User> users) {
        System.out.print("Introdu un username: ");
        String username = scanner.next();
        User us = null;
        for(User user : users)
            if(user.getNume().equals(username))
                us = user;
        for(User user : users)
            if(user.getNume().equals(username))
                us = user;

        while(users.contains(us)) {
            System.out.print("Introdu un alt username: ");
            username = scanner.next();
            us = null;
            for(User user : users)
                if(user.getNume().equals(username))
                    us = user;

        }

        System.out.print("Introdu o parola: ");
        String password = scanner.next();
        System.out.print("Introdu varsta: ");

        int varsta = 0;
        try {
            varsta = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Eroare: trebuie sa introduci un numar intreg!");
            scanner.nextLine();
        }
        User user = new User(username, varsta, password);
        user.setRole("user");
        users.add(user);
        return user;
    }

    public static User LoggedOut(Scanner scanner, Set<User> users) {
        System.out.print("Bine ați venit! Ce acțiune doriți să efectuați?\n\n1. Să intru în contul meu.\n2. Să îmi creez un cont.\n\nIntrodu numărul acțiunii: ");
        int option = 0;
        try {
            option = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Eroare: trebuie sa introduci un numar intreg!");
            scanner.nextLine();
        }
        User user = null;
        switch(option) {
            case 1:
                return MainService.LogIn(scanner, users);

            case 2:
                return MainService.SignIn(scanner, users);
        }
        return null;
    }

    public static int MeniuUser(Scanner scanner, User user) {
        System.out.print("Bine te-am gasit, " + user.getNume() + ". Ce doresti sa faci astazi?\n\n1.Afiseaza toate evenimentele posibile din restul anului.\n2.Cumpara un bilet nou.\n3.Anuleaza un bilet existent.\n4.Vizualizeaza lista ta cu biletele achizitionate.\n5.Cauta un eveniment dupa data.\n6.Trimite-ne evenimentul tau pentru a-l urca pe platforma!\n7.Vezi recenziile altor clienti.\n8.Lasa o recenzie\n9.Vezi notificarile tale.\n");
        if (user.getRole().equals("admin"))
            System.out.print("10.Adauga un eveniment\n11.Anuleaza un eveniment.\n12.Gestioneaza solicitarile de evenimente.\n13.Trimite o notificare.\n");
        System.out.print("14.Iesi din cont.\n15.Iesi din aplicatie.\n\nIntrodu numarul actiunii: ");
        int choice = 0;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Eroare: trebuie sa introduci un numar intreg!");
            scanner.nextLine();
        }
        return choice;
    }

    public static void AfisareEvenimente(List<Event> events) {
        for(Event ev : events)
            if(ev.getNumarBileteDisponibile() > 0)
                System.out.println(ev);

        System.out.print("\n\n");
    }
    public static void Initializare(Set<User>users,List<Event> events) {
        User user = new User("robertanechita", 21, "roberta1");
        user.setRole("admin");
        users.add(user);
        user = new User("biancap", 21, "b11nc");
        user.setRole("user");
        users.add(user);
        user = new User("iriiina", 21, "12345");
        user.setRole("user");
        users.add(user);
        user = new User("ioanafl", 21, "florea12");
        user.setRole("user");
        users.add(user);
        user = new User("alex1", 21, "alx12");
        user.setRole("user");
        users.add(user);
        user = new User("amalia01", 21, "ama1");
        user.setRole("user");
        users.add(user);
        user = new User("ionuttte", 21, "f12ionut");
        user.setRole("user");
        users.add(user);
        user = new User("mariaioana", 21, "maria1");
        user.setRole("user");
        users.add(user);

        Event event = new Event("Untold", LocalDate.of(2025, 7, 15), "Va asteptam la cel mai tare festival din acest an!", "Cluj", 10000, "untoldfestivals");
        event.setPrice(539.99);
        events.add(event);
        event = new Event("Neversea", LocalDate.of(2025, 6, 15), "Va asteptam la cel mai tare festival din acest an!", "Constanta", 10000, "untoldfestivals");
        event.setPrice(339.99);
        events.add(event);
        event = new Event("Beach-Please", LocalDate.of(2025, 7, 2), "Va asteptam la cel mai tare festival din acest an!", "Costinesti", 10000, "SMN");
        event.setPrice(639.99);
        events.add(event);
        event = new Event("Nunta De Proba", LocalDate.of(2025, 5, 30), "Va asteptam la nunta de proba din acest an!", "Bucuresti", 100, "nuntiBucuresti");
        event.setPrice(139.99);
        events.add(event);
    }

    public static void cautaEvenimente(Scanner scanner, List<Event> events) {
        System.out.println("Introduceti data de inceput (format yyyy-MM-dd): ");
        String dataStartStr = scanner.next();
        System.out.println("Introduceti data de final (format yyyy-MM-dd): ");
        String dataFinalStr = scanner.next();
        try{
            LocalDate dataStart = LocalDate.parse(dataStartStr);
            LocalDate dataFinal = LocalDate.parse(dataFinalStr);

            System.out.println("Evenimente in perioada selectata:\n");

            boolean exista = false;

            for (Event event : events) {
                if ((event.getData().isEqual(dataStart) || event.getData().isAfter(dataStart)) &&
                        (event.getData().isEqual(dataFinal) || event.getData().isBefore(dataFinal))) {

                    System.out.println(event.getNume() + " | " + event.getData() + " | " + event.getLocatie());
                    exista = true;
                }
            }

            if (!exista) {
                System.out.println("Nu avem evenimente disponibile in perioada selectata.\n");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void veziRecenzii(List<Recenzie> recenzii) {
        for(Recenzie r : recenzii)
            System.out.println(r);
        System.out.print("\n\n");
    }

    public static void Recenzie(Scanner scanner, List<Recenzie> recenzii, User user) {
        System.out.println("Ce nota oferi serviciilor noastre? ");
        int nota = scanner.nextInt();
        System.out.println("Spune parerea ta despre serviciile noastre: ");
        scanner.nextLine();
        String text = scanner.nextLine();
        Recenzie recenzie = new Recenzie(user.getNume(), text, nota);
        recenzii.add(recenzie);
    }

    public static void gestioneazaSolicitari(Scanner scanner, List<Event> solicitari, List<Event> events) {
        if (solicitari.isEmpty()) {
            System.out.println("Nu sunt solicitari noi.");
            return;
        }

        for (Event ev : solicitari) {
            System.out.println(ev);

            System.out.print("Doriti sa aprobati acest eveniment? (da/nu): ");
            String ans = scanner.next();

            if (ans.equalsIgnoreCase("da")) {
                events.add(ev);
                System.out.println("Evenimentul a fost aprobat si adaugat in platforma.\n");
            } else {
                System.out.println("Evenimentul a fost refuzat.\n");
            }
        }

        solicitari.clear();
    }



    public static void adaugaEvent(Scanner scanner, List<Event> events) {
        System.out.print("Nume eveniment: ");
        scanner.nextLine();
        String nume = scanner.nextLine();

        System.out.print("Data (format yyyy-MM-dd): ");
        String dataStr = scanner.next();
        LocalDate data = LocalDate.parse(dataStr);

        System.out.print("Descriere: ");
        scanner.nextLine(); // consum \n de la inputul anterior
        String descriere = scanner.nextLine();

        System.out.print("Locatie: ");
        String locatie = scanner.nextLine();

        System.out.print("Capacitatea totala: ");
        int capacitate = scanner.nextInt();

        System.out.print("Organizator: ");
        scanner.nextLine(); // consum \n de la int ul ant
        String organizator = scanner.nextLine();

        System.out.print("Pretul unui bilet standard: ");
        double pret = scanner.nextDouble();

        Event event = new Event(nume, data, descriere, locatie, capacitate, organizator);
        event.setPrice(pret);
        events.add(event);
    }

    public static void trimiteNotificare(Scanner scanner, User from, Set<User>users) {
        User to = null;
        System.out.print("Catre cine doriti sa trimiteti o notificare?\n");
        scanner.nextLine();
        String nume = scanner.nextLine();
        boolean exista = false;
        for(User u : users)
            if(u.getNume().equalsIgnoreCase(nume)) {
                exista = true;
                to = u;
                break;
            }
        if(!exista) {
            System.out.println("Nu exista acest utilizator\n");
            return;
        }
        System.out.println("Ce mesaj doriti sa transmiteti?\n");
        String mesaj = scanner.nextLine();
        Notificare n = new Notificare(mesaj, from, to);
        to.adaugaNotificare(n);
        System.out.println("Notificare trimisa cu succes catre " + to.getNume());
    }

    public static void anuleazaEvent(Scanner scanner, List<Event> events, Set<User>users, User user) {
        System.out.print("Nume eveniment: ");
        scanner.nextLine();
        String nume = scanner.nextLine();
        System.out.print("Ce mesaj doriti sa transmiteti utilizatorilor care voiau sa vina la acest eveniment? ");
        String mesaj = scanner.nextLine();
        Event event = null;
        for(Event e : events)
            if(e.getNume().equalsIgnoreCase(nume)) {
                event = e;
                break;
            }
        if(event == null) {
            System.out.print("Nu exista acest eveniment.\n");
            return;
        }
        for(User u : users) {
            for(Bilet b : u.getBilete())
                if(b.getEventName().equals(nume)) {
                    Notificare n = new Notificare(mesaj, user, u);
                    u.adaugaNotificare(n);
                    b.setValid(false);
                }
        }
        events.remove(event);
    }
}


