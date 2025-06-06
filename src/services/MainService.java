package services;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import classes.*;
import db.DatabaseContext;

public class MainService {

    public static void cumparaBilet(Scanner scanner, User user, Event event,
                                    BiletService biletService, EventService eventService) {
        try {
            if (event.getNumarBileteDisponibile() < 1) {
                System.out.println("Ne pare rau, dar nu mai avem bilete momentan.\n");
                return;
            }

            System.out.println("Ce tip de bilet doriti?\n1. VIP\n2. GENERAL ACCESS\n3. ONE DAY PASS");
            int tip = scanner.nextInt();
            scanner.nextLine(); // consumă newline

            System.out.println("Scrieti numarul cardului cu care doriti sa efectuati plata: ");
            String nrCard = scanner.nextLine();

            Bilet bilet = null;
            String codPlata = UUID.randomUUID().toString();
            String tipBilet = "";
            String statusPlata = "Plata efectuata";

            switch (tip) {
                case 1 -> {
                    tipBilet = "VIP";
                    bilet = new Bilet(event.getNume(), user.getNume(), tipBilet, codPlata);
                }
                case 2 -> {
                    tipBilet = "GENERAL ACCESS";
                    bilet = new Bilet(event.getNume(), user.getNume(), tipBilet, codPlata);
                }
                case 3 -> {
                    System.out.print("In a cata zi doriti sa veniti la eveniment?\n");
                    int ziAcces = scanner.nextInt();
                    scanner.nextLine(); // consumă newline
                    LocalDate dataAcces = event.getData().plusDays(ziAcces);

                    System.out.print("Doriti camping? (da/nu): ");
                    String raspunsCamping = scanner.nextLine().trim().toLowerCase();
                    boolean cuCamping = raspunsCamping.equals("da");

                    tipBilet = "ONE DAY PASS";
                    bilet = new OneDayPass(event.getNume(), user.getNume(), dataAcces, cuCamping, tipBilet, codPlata);
                }
                default -> {
                    System.out.println("Optiune invalida.");
                    return;
                }
            }

            // setez biletul ca valid
            bilet.setValid(true);

            // salvare in baza de date
            biletService.create(bilet); //daca e one day pass schimb

            // actualizez event-ul
            event.setNumarBileteDisponibile(event.getNumarBileteDisponibile() - 1);
            eventService.update(event);

            System.out.println("Bilet cumpărat cu succes pentru " + bilet.getEventName() +
                    ", de către " + user.getNume());

        } catch (Exception e) {
            System.out.println("A apărut o eroare la cumpărarea biletului: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public static void anuleazaBilet(Scanner scanner, User user, BiletService biletService, EventService eventService) throws SQLException {
        try {
            System.out.print("De la ce eveniment doriti sa anulati biletul?\n");
            String eventName = scanner.next();

            Optional<Bilet> bilet = biletService.readByCumparatorSiEvent(user.getNume(), eventName);

            if (bilet.isPresent()) {
                Bilet b = bilet.get();
                System.out.print(b.esteValid() + "dupa:");
                b.setValid(false); // anulez biletul
                System.out.print(b.esteValid());
                biletService.update(b);

                Optional<Event> event = eventService.read(eventName);

                if (event.isPresent()) {
                    Event e = event.get();
                    e.setNumarBileteDisponibile(e.getNumarBileteDisponibile() + 1);
                    eventService.update(e);
                }

                System.out.println("Biletul pentru evenimentul " + eventName + " a fost anulat.");
            }
            else {
                System.out.println("Utilizatorul nu are un bilet valid la acest eveniment.");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("A apărut o eroare la anularea biletului.");
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

        Event event = new Event(nume, data, descriere, locatie, capacitate, organizator, pret);

        solicitariEvenimente.add(event);
        System.out.println("Solicitarea a fost trimisa spre aprobare.\n\n");
    }

    public static void afiseazaBileteUser(User user) throws Exception {
        System.out.println("Biletele lui " + user.getNume() + ":");

        BiletService biletService = new BiletService();
        List<Bilet> bilete = biletService.readAll();

        boolean areBilete = false;
        for (Bilet b : bilete)
            if (b.getCumparator().equalsIgnoreCase(user.getNume()) && b.esteValid()) {
                System.out.println(b);
                areBilete = true;
            }

        if (!areBilete) {
            System.out.println("Nu aveti bilete disponibile.");
        }
    }


    public static User LogIn(Scanner scanner, UserService userService) {
        try {
            System.out.print("Introdu un username: ");
            String username = scanner.next();
            System.out.print("Introdu un password: ");
            String password = scanner.next();

            Optional<User> optionalUser = userService.read(username);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                while (!user.getPass().equals(password)) {
                    System.out.print("Parola incorecta. Reintrodu parola: ");
                    password = scanner.next();
                }
                return user;
            } else {
                System.out.println("Utilizatorul nu exista. Doriti sa creati un nou cont? (da/nu)");
                String raspuns = scanner.next();
                if (raspuns.equalsIgnoreCase("da"))
                    return SignIn(scanner, userService);
                else
                    return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static User SignIn(Scanner scanner, UserService userService) {
        try {
            String username;
            Optional<User> optionalUser;

            do {
                System.out.print("Introdu un username: ");
                username = scanner.next();
                optionalUser = userService.read(username);
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

            User user = new User(username, varsta, password, "user");
            userService.create(user); // salvare in baza de date

            System.out.println("Cont creat cu succes!");
            return user;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static User LoggedOut(Scanner scanner) throws SQLException {
        UserService userService = new UserService();
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
                return MainService.LogIn(scanner, userService);

            case 2:
                return MainService.SignIn(scanner, userService);
        }
        return null;
    }

    public static int MeniuUser(Scanner scanner, User user) {
        System.out.print("Bine te-am gasit, " + user.getNume() + ". Ce doresti sa faci astazi?\n\n1.Afiseaza toate evenimentele posibile din restul anului.\n2.Cumpara un bilet nou.\n3.Anuleaza un bilet existent.\n4.Vizualizeaza lista ta cu biletele achizitionate.\n5.Cauta un eveniment dupa data.\n6.Trimite-ne evenimentul tau pentru a-l urca pe platforma!\n7.Vezi recenziile altor clienti.\n8.Lasa o recenzie\n9.Vezi notificarile tale.\n10.Iesi din cont.\n11.Actualizeaza-ti profilul.\n12.Iesi din aplicatie.\n");
        if (user.getRole().equals("admin"))
            System.out.print("13.Adauga un eveniment\n14.Anuleaza un eveniment.\n15.Gestioneaza solicitarile de evenimente.\n16.Trimite o notificare.\n17.Actualizeaza lista artistilor prezenti la un eveniment.\n18.Actualizeaza datele despre un artist.\n");
        System.out.print("\n\nIntrodu numarul actiunii: ");
        int choice = 0;
        try {
            choice = scanner.nextInt();
            if(choice > 12 && user.getRole().equals("user"))
                return 12;
        } catch (InputMismatchException e) {
            System.out.println("Eroare: trebuie sa introduci un numar intreg!");
            scanner.nextLine();
        }
        return choice;
    }


    public static void AfisareEvenimente() throws Exception {
        EventService eventService = new EventService();
        List<Event> toateEvenimentele = eventService.readAll();

        for (Event e : toateEvenimentele) {
            System.out.println(e);
            List<Artist>lineup = eventService.getArtistiPentruEveniment(e.getNume());
            if(lineup.isEmpty())
                System.out.println("In curand...\n");
            else
                for (Artist a : lineup)
                    System.out.println(a.getNume() + ": " + a.getDescriere());

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

            EventService eventService = new EventService();
            List<Event> events = eventService.readAll(); // luam toate evenimentele din DB

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

    public static void gestioneazaSolicitari(Scanner scanner, List<Event> solicitari, EventService eventService) throws Exception {
        if (solicitari.isEmpty()) {
            System.out.println("Nu sunt solicitari noi.");
            return;
        }

        for (Event ev : solicitari) {
            System.out.println(ev);

            System.out.print("Doriti sa aprobati acest eveniment? (da/nu): ");
            String ans = scanner.next();

            if (ans.equalsIgnoreCase("da")) {
                eventService.create(ev);
                System.out.println("Evenimentul a fost aprobat si adaugat in platforma.\n");
            }
            else {
                System.out.println("Evenimentul a fost refuzat.\n");
            }
        }

        solicitari.clear();
    }



    public static void adaugaEvent(Scanner scanner, EventService eventService) throws Exception {
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
        scanner.nextLine(); // consum \n de la int ul anterior
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
        Event event = new Event(nume, data, descriere, locatie, capacitate, organizator, pret);
        eventService.create(event);
    }

    public static void trimiteNotificare(Scanner scanner, User from, List<Notificare> notificari, UserService userService) throws Exception {
        System.out.print("Catre cine doriti sa trimiteti o notificare?\n");
        scanner.nextLine();
        String nume = scanner.nextLine();

        Optional<User> toOpt = userService.read(nume);

        if (toOpt.isPresent()) {
            User to = toOpt.get();

            System.out.println("Ce mesaj doriti sa transmiteti?");
            String mesaj = scanner.nextLine();

            Notificare notificare = new Notificare(mesaj, from, to);
            notificari.add(notificare);
            Collections.sort(notificari);

            System.out.println("Notificare trimisa cu succes catre " + to.getNume());
        }
        else {
            System.out.println("Nu exista acest utilizator.");
        }
    }


    public static void anuleazaEvent(Scanner scanner, User user, EventService eventService, BiletService biletService, UserService userService, List<Notificare>notif) throws Exception {
        System.out.print("Nume eveniment: ");
        scanner.nextLine();
        String nume = scanner.nextLine();

        System.out.print("Ce mesaj doriti sa transmiteti utilizatorilor care voiau sa vina la acest eveniment? ");
        String mesaj = scanner.nextLine();

        Optional<Event> eventOptional = eventService.read(nume);
        if(eventOptional.isEmpty()) {
            System.out.println("Nu exista acest eveniment.");
            return;
        }
        Event event = eventOptional.get();

        List<Bilet> bilete = biletService.readAll();

        for(Bilet b : bilete) {
            if(b.getEventName().equalsIgnoreCase(nume)) {
                // gasesc utilizatorul dupa username
                User userCumparator = null;
                try {
                    Optional<User> optionalUser = userService.read(b.getCumparator());
                    if(optionalUser.isPresent()) {
                        userCumparator = optionalUser.get();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if(userCumparator != null) {
                    Notificare n = new Notificare(mesaj, user, userCumparator);
                    notif.add(n);
                }
                biletService.delete(b.getCodUnic());
            }
        }

        // sterg event din db
        eventService.delete(nume);

        System.out.println("Evenimentul si biletele aferente au fost anulate si sterse.");
    }



    public static void Lineup(Scanner scanner, EventService eventService, ArtistService artistService) throws Exception {
        boolean exista = false;
        System.out.print("La ce eveniment doriti sa actualizati lista?\n");
        scanner.nextLine();
        String numeEv = scanner.nextLine();
        Optional<Event>eventOptional = eventService.read(numeEv);
        if(eventOptional.isPresent()) {
            Event e = eventOptional.get();
            exista = true;
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
                artistService.create(new Artist(artist, descriere, views));
                Optional<Boolean> rezultat = artistService.adaugaArtistLaEveniment(artist, numeEv, Date.valueOf(dat));
                if (rezultat.isPresent()) {
                    System.out.println("Artistul a fost adaugat la eveniment cu succes.");
                }
                else {
                    System.out.println("Artistul sau evenimentul nu exista.");
                }

            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        else
            System.out.println("Nu exista acest eveniment.\n");

    }

    public static void ActArtist(Scanner scanner, ArtistService artistService) throws SQLException {
        System.out.print("Spune numele artistului: ");
        scanner.nextLine();
        String nume = scanner.nextLine();
        Optional<Artist>artist = artistService.readByNume(nume);
        if(artist.isPresent()) {
            Artist a = artist.get();
            System.out.println(a.getNume());
            System.out.println(a.getDescriere());
            System.out.println(a.getViews());
            System.out.println("Ce doresti sa modifici?\n1. Descriere\n2. Numarul de views\n3. Vreau sa sterg acest artist din lista.\n");
            int option = scanner.nextInt();
            scanner.nextLine(); // consuma newline-ul ramas dupa nextInt
            switch (option) {
                case 1:
                    a.setDescriere(scanner.nextLine());
                    break;
                case 2:
                    a.setViews(scanner.nextDouble());
                    break;
                case 3:
                    artistService.delete(nume);
                    return;
                default:
                    break;
            }
            artistService.update(a);

        }
        else
            System.out.println("Nu exista acest artist.\n");
    }
    public static User EditProfile(Scanner scanner, UserService userService, User user) throws SQLException {
        System.out.print("Ce doresti sa faci?\n1.Schimba Parola\n2.Actualizeaza varsta\n3.Dezacctivare Cont\n ");
        int option = scanner.nextInt();
        scanner.nextLine(); // consuma newline-ul ramas dupa nextInt
        switch (option) {
            case 1:
                System.out.print("Scrie parola actuala: ");
                String pass = scanner.nextLine();
                if(pass != null && pass.equals(user.getPass())) {
                    System.out.print("Scrie parola dorita: ");
                    pass = scanner.nextLine();
                    user.setPass(pass);
                    userService.update(user);
                    System.out.println("Parola a fost modificata.");
                }
                else
                    System.out.println("Parola incorecta. Nu aveti dreptul sa schimbati parola.\n");

                break;
            case 2:
                System.out.print("Scrie varsta: ");
                try{
                    int varsta = Integer.parseInt(scanner.nextLine());
                   // scanner.nextLine();
                    user.setVarsta(varsta);
                    userService.update(user);
                }
                catch(Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 3:
                System.out.print("Esti sigur? Contul nu poate fi recuperat. Y/N ");
                String input = scanner.nextLine().trim().toLowerCase();
                switch(input) {
                    case "y":
                        System.out.print("Scrie parola actuala: ");
                        pass = scanner.nextLine();
                        if(pass != null && pass.equals(user.getPass())) {
                            userService.delete(user.getNume());
                            System.out.println("Contul a fost sters.");
                            return LoggedOut(scanner);
                        }
                        else
                            System.out.println("Parola incorecta. Nu aveti dreptul sa stergeti acest cont.\n");
                        break;
                    case "n":
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;

        }
        return user;
    }
}


