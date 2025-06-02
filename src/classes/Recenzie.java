package classes;


import classes.Interfaces.IRecenzie;

public class Recenzie implements IRecenzie {
    private final String autor;
    private String text;
    private int rating; // de la 1 la 10

    public Recenzie(String autor, String text, int rating) {
        this.autor = autor;
        this.text = text;
        this.rating = rating;
    }

    @Override
    public String getAutor() {
        return autor;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getRating() {
        return rating;
    }

    @Override
    public void setRating(int rating) {
        if (rating < 1 || rating > 10) {
            throw new IllegalArgumentException("Ratingul trebuie sa fie intre 1 si 10.");
        }
        this.rating = rating;
    }

    @Override
    public String toString() {
        return autor + ": " + text + " - " + rating + " / 10\n\n";

    }
}
