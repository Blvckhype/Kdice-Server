package Model;

public class Score implements Comparable<Score> {

    private long id;
    private int sum;

    public Score(long id, int sum) {
        this.id = id;
        this.sum = sum;
    }

    public long getId() {
        return id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = getSum() + sum;
    }

    @Override
    public int compareTo(Score o) {

        if (this.getSum() < o.getSum())
            return -1;
        else if (this.getSum() == o.getSum())
            if (this.getId() < o.getSum())
                return -1;
            else
                return 1;
        else
            return 1;
    }
}
