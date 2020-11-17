package cs2030.simulator;

public class Pair<T,U> {

    public T first;
    public U second;

    private Pair(T first, U second) {
        this.first  = first;
        this.second = second;
    }

    public static <T,U> Pair<T,U> of(T first, U second) {
        return new Pair<>(first, second);
    }

    public T first() {
        return this.first;
    }

    public U second() {
        return this.second;
    }

    public String toString(){
        return String.format("Pair value: {%s, %s}", this.first(), this.second());
    }
}
