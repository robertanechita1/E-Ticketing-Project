package services;



import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import classes.*;
import db.DbConnection;
import db.GenericDao;
import java.sql.Connection;

public class MainService {

    public static void cumparaBilet(Scanner scanner, User user, Event event) throws Exception {
        if (event.getNumarBileteDisponibile() < 1) {
            System.out.println("Ne pare rau, dar nu mai avem bilete momentan.\n\n");
            return;
        }

        System.out.println("Ce tip de bilet doriti?\n1. VIP\n2. GENERAL ACCESS\n3. ONE DAY PASS");
        int tip = scanner.nextInt();
        scanner.nextLine(); // consumă newline

        System.out.println("Scrieti numarul cardului cu care doriti sa efectuati plata: ");
        String nrCard = scanner.nextLine();

        Bilet bilet = null;
        Plata plata;
        GenericDao<Plata> plataDao = GenericDao.getInstance(Plata.class);
        GenericDao<Bilet> biletDao = GenericDao.getInstance(Bilet.class);

        switch (tip) {
            case 1 -> {
                plata = new Plata(event.getPrice() + 70, nrCard, "Plata efectuata");
                bilet = new Bilet(event.getNume(), user.getNume(), "VIP", plata.getcod());
            }
            case 2 -> {
                plata = new Plata(event.getPrice() + 20, nrCard, "Plata efectuata");
                bilet = new Bilet(event.getNume(), user.getNume(), "GENERAL ACCESS", plata.getcod());
            }
            case 3 -> {
                System.out.print("In a cata zi doriti sa veniti la eveniment?\n");
                int ziAcces = scanner.nextInt();
                scanner.nextLine(); // consumă newline
                LocalDate dataAcces = event.getData().plusDays(ziAcces);

                System.out.print("Doriti camping? (da/nu): ");
                String raspunsCamping = scanner.nextLine().trim().toLowerCase();
                boolean cuCamping = raspunsCamping.equals("da");

                double pret = cuCamping ? event.getPrice() - 70 : event.getPrice() - 100;
                plata = new Plata(pret, nrCard, "Plata efectuata");

                bilet = new OneDayPass(event.getNume(), user.getNume(), dataAcces, cuCamping, "ONE DAY PASS", plata);
            }
            default -> {
                System.out.println("Optiune invalida.");
                return;
            }
        }

        plataDao.addObj(plata);
        //  biletul in DB
        biletDao.addObj(bilet);

        // actualizez user-ul
       // user.adaugaBilet(bilet);

        // scad bilet disponibil
        event.setNumarBileteDisponibile(event.getNumarBileteDisponibile() - 1);
        // TO DO: GenericDao<Event>.getInstance(Event.class).updateObject(event);

        System.out.println("Bilet cumpărat cu succes pentru " + bilet.getEventName() +
                ", de către " + user.getNume());
    }


    public static void anuleazaBilet(Scanner scanner, User user) throws Exception {
        System.out.print("De la ce eveniment doriti sa anulati biletul?\n");
        String eventName = scanner.next();

        GenericDao<Bilet> biletDao = GenericDao.getInstance(Bilet.class);
        GenericDao<Event> eventDao = GenericDao.getInstance(Event.class);

        List<Bilet> toateBiletele = biletDao.getObjects();

        boolean gasit = false;

        for (Bilet bilet : toateBiletele) {
            if (bilet.getCumparator().equalsIgnoreCase(user.getNume()) &&
                    bilet.getEventName().equalsIgnoreCase(eventName) ) {
                bilet.setValid(false);
               // biletDao.updateObj(bilet); // update in DB
                gasit = true;
            }
        }

        if (gasit) {
            // actualizez biletele disponibile in event
            Optional<Event> optEvent = eventDao.findByField(eventName, "nume");
            if (optEvent.isPresent()) {
                Event e = optEvent.get();
                e.setNumarBileteDisponibile(e.getNumarBileteDisponibile() + 1);
               // eventDao.updateObj(e);
            }
            System.out.println("Biletul pentru evenimentul " + eventName + " a fost anulat.");
        }
        else {
            System.out.println("Utilizatorul nu are un bilet valid la acest eveniment.");
        }
    }



    public static void trimiteSolicitare(Scanner scanner, List<Event> solicitariEvenimente) {

        System.out.print("Nume eveniment: ");
        scanner.nextLine();
        String nume = scanner.nextLine();

        System.out.print("Data (format yyyy-MM-dd): ");
        String dataStr = scanner.next();
        LocalDate data = null;
        try {
            data = LocalDate.parse(dataStr);
        } catch (InputMismatchException e) {
            System.out.println("Eroare: trebuie sa introduci o data in acel format!");
            scanner.nextLine();
        }

        System.out.print("Descriere: ");
        scanner.nextLine(); // consum \n de la inputul anterior
        String descriere = scanner.nextLine();

        System.out.print("Locatie: ");
        String locatie = scanner.nextLine();

        System.out.print("Capacitatea totala: ");
        int capacitate = 0;
        try {
            capacitate = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Eroare: trebuie sa introduci un numar intreg!");
            scanner.nextLine();
        }

        System.out.print("Organizator: ");
        scanner.nextLine(); // consum \n de la int ul ant
        String organizator = scanner.nextLine();

        System.out.print("Pretul unui bilet standard: ");
        double pret = 0;
        try {
            pret = scanner.nextDouble();
        } catch (InputMismatchException e) {
            System.out.println("Eroare: trebuie sa introduci un numar intreg!");
            scanner.nextLine();
        }

        Event event = new Event(nume, data, descriere, locatie, capacitate, organizator);
        event.setPrice(pret);

        solicitariEvenimente.add(event);

        System.out.println("Solicitarea a fost trimisa spre aprobare.\n\n");
    }

    public static void afiseazaBileteUser(User user) throws Exception {
        System.out.println("Biletele lui " + user.getNume() + ":");

        GenericDao<Bilet> biletDao = GenericDao.getInstance(Bilet.class);
        List<Bilet> bilete = biletDao.getObjects(); //  toate biletele

        boolean areBilete = false;
        for (Bilet b : bilete) {
            if (b.getCumparator().equals(user.getNume())) {
                System.out.println(b);
                areBilete = true;
            }
        }
        if (!areBilete)
            System.out.println("Nu aveti bilete disponibile.");
    }



    public static void afiseazaNotificariUser(User user) {
        System.out.println("Notificari pentru " + user.getNume() + ":\n");
        if(user.getNotificari().isEmpty())
            System.out.println("Nu aveti notificari");
        for (Notificare n : user.getNotificari()) {
            System.out.println(n);
        }
    }

    public static User LogIn(Scanner scanner) {
        try {
            System.out.print("Introdu un username: ");
            String username = scanner.next();
            System.out.print("Introdu un password: ");
            String password = scanner.next();

            GenericDao<User> userDao = GenericDao.getInstance(User.class);
            Optional<User> optionalUser = userDao.findByField(username, "nume");

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                while (!user.getPass().equals(password)) {
                    System.out.print("Parola incorecta. Reintrodu parola: ");
                    password = scanner.next();
                }
                return user;
            }
            else {
                System.out.println("Utilizatorul nu exista. Doriti sa creati un nou cont? (da/nu)");
                String raspuns = scanner.next();
                if (raspuns.equalsIgnoreCase("da"))
                    return SignIn(scanner);
                else
                    return null;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static User SignIn(Scanner scanner) {
        try {
            GenericDao<User> userDao = GenericDao.getInstance(User.class);

            String username;
            Optional<User> optionalUser;
            do {
                System.out.print("Introdu un username: ");
                username = scanner.next();
                optionalUser = userDao.findByField(username, "nume");
                if (optionalUser.isPresent()) {
                    System.out.println("Username deja existent. Alege altul.");
                }
            } while (optionalUser.isPresent());

            System.out.print("Introdu o parola: ");
            String password = scanner.next();

            int varsta = 0;
            while (true) {
                System.out.print("Introdu varsta: ");
                try {
                    varsta = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Eroare: trebuie sa introduci un numar intreg!");
                    scanner.nextLine(); // clear buffer
                }
            }

            User user = new User(username, varsta, password);
            user.setRole("user");

            userDao.addObj(user); // salvare in baza de date

            System.out.println("Cont creat cu succes!");
            return user;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static User LoggedOut(Scanner scanner) {
        System.out.print("Bine ați venit! Ce acțiune doriți să efectuați?\n\n1. Să intru în contul meu.\n2. Să îmi creez un cont.\n\nIntrodu numărul acțiunii: ");
        int option = 0;
        try {
            option = scanner.nextInt();
        }
        catch (InputMismatchException e) {
            System.out.println("Eroare: trebuie sa introduci un numar intreg!");
            scanner.nextLine();
        }
        User user = null;
        switch(option) {
            case 1:
                return MainService.LogIn(scanner);

            case 2:
                return MainService.SignIn(scanner);
        }
        return null;
    }

    public static int MeniuUser(Scanner scanner, User user) {
        System.out.print("Bine te-am gasit, " + user.getNume() + ". Ce doresti sa faci astazi?\n\n1.Afiseaza toate evenimentele posibile din restul anului.\n2.Cumpara un bilet nou.\n3.Anuleaza un bilet existent.\n4.Vizualizeaza lista ta cu biletele achizitionate.\n5.Cauta un eveniment dupa data.\n6.Trimite-ne evenimentul tau pentru a-l urca pe platforma!\n7.Vezi recenziile altor clienti.\n8.Lasa o recenzie\n9.Vezi notificarile tale.\n10.Iesi din cont.\n11.Iesi din aplicatie.\n");
        if (user.getRole().equals("admin"))
            System.out.print("12.Adauga un eveniment\n13.Anuleaza un eveniment.\n14.Gestioneaza solicitarile de evenimente.\n15.Trimite o notificare.\n16.Actualizeaza lista artistilor prezenti la un eveniment.\n");
        System.out.print("\n\nIntrodu numarul actiunii: ");
        int choice = 0;
        try {
            choice = scanner.nextInt();
            if(choice > 11 && user.getRole().equals("user"))
                return 11;
        } catch (InputMismatchException e) {
            System.out.println("Eroare: trebuie sa introduci un numar intreg!");
            scanner.nextLine();
        }
        return choice;
    }

    public static void AfisareEvenimente() throws Exception {
        List<Event> ev = GenericDao.getInstance(Event.class).getObjects();
        for (Event e : ev) {
            System.out.println(e);
        }
        System.out.print("\n\n");
    }

    public static void cautaEvenimente(Scanner scanner) throws Exception {
        System.out.println("Introduceti data de inceput (format yyyy-MM-dd): ");
        String dataStartStr = scanner.next();
        System.out.println("Introduceti data de final (format yyyy-MM-dd): ");
        String dataFinalStr = scanner.next();

        try {
            LocalDate dataStart = LocalDate.parse(dataStartStr);
            LocalDate dataFinal = LocalDate.parse(dataFinalStr);

            GenericDao<Event> eventDao = GenericDao.getInstance(Event.class);
            List<Event> events = eventDao.getObjects(); // luam toate evenimentele din DB

            boolean exista = false;
            System.out.println("Evenimente in perioada selectata:\n");

            for (Event event : events) {
                LocalDate dataEveniment = event.getData();
                if ((dataEveniment.isEqual(dataStart) || dataEveniment.isAfter(dataStart)) &&
                        (dataEveniment.isEqual(dataFinal) || dataEveniment.isBefore(dataFinal))) {
                    System.out.println(event.getNume() + " | " + dataEveniment + " | " + event.getLocatie());
                    exista = true;
                }
            }

            if (!exista) {
                System.out.println("Nu avem evenimente disponibile in perioada selectata.\n");
            }

        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }



    public static void veziRecenzii(List<Recenzie> recenzii) {
        if(recenzii.isEmpty())
            System.out.println("Nu avem recenzii momentan.");
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

    public static void gestioneazaSolicitari(Scanner scanner, List<Event> solicitari) throws Exception {
        if (solicitari.isEmpty()) {
            System.out.println("Nu sunt solicitari noi.");
            return;
        }

        for (Event ev : solicitari) {
            System.out.println(ev);

            System.out.print("Doriti sa aprobati acest eveniment? (da/nu): ");
            String ans = scanner.next();

            if (ans.equalsIgnoreCase("da")) {
                GenericDao<Event> eventDao = GenericDao.getInstance(Event.class);
                eventDao.addObj(ev);
                System.out.println("Evenimentul a fost aprobat si adaugat in platforma.\n");
            }
            else {
                System.out.println("Evenimentul a fost refuzat.\n");
            }
        }

        solicitari.clear();
    }



    public static void adaugaEvent(Scanner scanner) throws Exception {
        System.out.print("Nume eveniment: ");
        scanner.nextLine();
        String nume = scanner.nextLine();

        System.out.print("Data (format yyyy-MM-dd): ");
        String dataStr = scanner.next();
        LocalDate data = null;
        try {
            data = LocalDate.parse(dataStr);
        }
        catch (InputMismatchException e) {
            System.out.println("Eroare: trebuie sa introduci o data in acel format!");
            scanner.nextLine();
        }

        System.out.print("Descriere: ");
        scanner.nextLine(); // consum \n de la inputul anterior
        String descriere = scanner.nextLine();

        System.out.print("Locatie: ");
        String locatie = scanner.nextLine();

        System.out.print("Capacitatea totala: ");
        int capacitate = 0;
        try {
            capacitate = scanner.nextInt();
        }
        catch (InputMismatchException e) {
            System.out.println("Eroare: trebuie sa introduci un numar intreg!");
            scanner.nextLine();
        }

        System.out.print("Organizator: ");
        scanner.nextLine(); // consum \n de la int ul ant
        String organizator = scanner.nextLine();

        System.out.print("Pretul unui bilet standard: ");
        double pret = 0;
        try {
            pret = scanner.nextDouble();
        }
        catch (InputMismatchException e) {
            System.out.println("Eroare: trebuie sa introduci un numar intreg!");
            scanner.nextLine();
        }
        Event event = new Event(nume, data, descriere, locatie, capacitate, organizator);
        event.setPrice(pret);
        event.setPrice(539.99);
        GenericDao<Event> eventDao = GenericDao.getInstance(Event.class);
        eventDao.addObj(event);
    }

    public static void trimiteNotificare(Scanner scanner, User from) throws Exception {
        GenericDao<User> userDao = GenericDao.getInstance(User.class);
        System.out.print("Catre cine doriti sa trimiteti o notificare?\n");
        scanner.nextLine();
        String nume = scanner.nextLine();
        Optional<User> us = userDao.findByField(nume, "nume");
        if (us.isPresent()) {
            User to = us.get();
            System.out.println("Ce mesaj doriti sa transmiteti?\n");
            String mesaj = scanner.nextLine();
            Notificare n = new Notificare(mesaj, from, to);
            to.adaugaNotificare(n);
            System.out.println("Notificare trimisa cu succes catre " + to.getNume());
        }
        else {
            System.out.println("Nu exista acest utilizator\n");
            return;
        }

    }

    public static void anuleazaEvent(Scanner scanner, User user) throws Exception {
        System.out.print("Nume eveniment: ");
        scanner.nextLine();
        String nume = scanner.nextLine();

        System.out.print("Ce mesaj doriti sa transmiteti utilizatorilor care voiau sa vina la acest eveniment? ");
        String mesaj = scanner.nextLine();

        GenericDao<Event> eventDao = GenericDao.getInstance(Event.class);
        GenericDao<Bilet> biletDao = GenericDao.getInstance(Bilet.class);

        Optional<Event> eventOptional = eventDao.findByField(nume, "nume");
        if(eventOptional.isEmpty()) {
            System.out.println("Nu exista acest eveniment.");
            return;
        }
        Event event = eventOptional.get();

        List<Bilet> bilete = biletDao.getObjects();

        for(Bilet b : bilete) {
            if(b.getEventName().equalsIgnoreCase(nume) && b.esteValid()) {
                // gasesc utilizatorul dupa username
                GenericDao<User> userDao = GenericDao.getInstance(User.class);
                User userCumparator = null;
                try {
                    Optional<User> optionalUser = userDao.findByField(b.getCumparator(), "nume");
                    if(optionalUser.isPresent()) {
                        userCumparator = optionalUser.get();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if(userCumparator != null) {
                    Notificare n = new Notificare(mesaj, user, userCumparator);
                    userCumparator.adaugaNotificare(n);
                }

                // anulez biletul
                b.setValid(false);
                //biletDao.updateObj(b);
            }
        }

        // sterg event din db
       // eventDao.deleteObj(event);

        System.out.println("Evenimentul si biletele aferente au fost anulate si sterse.");
    }



    public static void Lineup(Scanner scanner) throws Exception {
        boolean exista = false;
        GenericDao<Event> eventDao = GenericDao.getInstance(Event.class);
        System.out.print("La ce eveniment doriti sa actualizati lista?\n");
        scanner.nextLine();
        String numeEv = scanner.nextLine();
        Optional<Event>eventOptional = eventDao.findByField(numeEv, "nume");
        if(eventOptional.isPresent()) {
            Event e = eventOptional.get();
            exista = true;
            e.getLineup();
            System.out.print("Ce artist vreti sa adaugati?\n");
            String artist = scanner.nextLine();
            System.out.print("Ce ar trebui sa stie participantii despre acest artist?\n");
            String descriere = scanner.nextLine();
            System.out.print("Pe ce data va canta artistul?(format yyyy-MM-dd)\n");
            try {
                String data = scanner.nextLine();
                LocalDate dat = LocalDate.parse(data);
                System.out.print("Cate vizualizari are in ultimul an?(exprimat in M)\n");
                Double views = Double.parseDouble(scanner.nextLine());
                e.actLineup(new Artist(artist, dat, descriere, views));
            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        else
            System.out.println("Nu exista acest eveniment.\n");

    }
}


