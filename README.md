# E-Ticketing Platform – Java Console Application

Acest proiect reprezintă o aplicație are simulează o platformă de e-ticketing pentru evenimente. Utilizatorii pot vizualiza evenimente, cumpăra bilete, trimite solicitări pentru organizarea propriilor evenimente, iar administratorii pot gestiona cererile, evenimentele și notificările.

---

## Funcționalități implementate

### Utilizator
- Vizualizare evenimente disponibile până la finalul anului
- Cumpărare bilet
- Anulare bilet
- Vizualizare bilete achiziționate
- Trimitere solicitare pentru adăugarea unui eveniment propriu
- Căutare eveniment într-o perioadă aleasă
- Vizualizare recenzii ale unui eveniment
- Adăugare recenzie
- Vizualizare notificări primite
- Delogare din platformă

### Admin
- Adăugare de evenimente în platformă
- Aprobare / respingere solicitări de la utilizatori pentru adăugarea evenimentelor
- Anulare eveniment
- Trimitere notificări către participanți

---

## Tipuri de obiecte

| Clasă        | Atribute principale |
|--------------|---------------------|
| **Event**     | `nume`, `data`, `locatie`, `capacitateTotala`, `numarBileteDisponibile`, `organizator` |
| **Bilet**     | `event`, `pret`, `tipBilet`, `user`, `plata` |
| **User**      | `nume`, `varsta`, `parola`, `listaBilete`, `notificari`, `evenimenteSuggerate` |
| **Locatie**   | `adresa`, `capacitate` |
| **Recenzie**  | `text`, `data`, `user`, `eveniment` |
| **Notificare**| `text`, `emitator`, `receptor`, `data` |
| **Plata**     | `status`, `suma`, `metodaPlata`, `ultimele4CifreCard` |

---