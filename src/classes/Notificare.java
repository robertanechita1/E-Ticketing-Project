package classes;

import java.time.LocalDateTime;

public class Notificare implements classes.INotificare {
    private final String text;
    private final User emitator;
    private final User receptor;
    private final LocalDateTime data;

    public Notificare(String text, User emitator, User receptor) {
        this.text = text;
        this.emitator = emitator;
        this.receptor = receptor;
        this.data = LocalDateTime.now();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public User getEmitator() {
        return emitator;
    }

    @Override
    public User getReceptor() {
        return receptor;
    }

    @Override
    public LocalDateTime getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Pe data de " + data.toLocalDate() + " " + emitator.getNume() + " v-a scris: " + text + "\n";
    }
}