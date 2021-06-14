package htlgkr.scorebook;

public class Round {
    String name;        //name of the golf club
    String address;     //address when Button setAddress was hit
    String date;        //format: dd-MM-yyyy HH:mm
    int par;
    int score;
    int over;           //over par
    int putts;
    int fairway;    //fairways hit
    int gir;        //greens in regulation

    public Round(String name, String address, String date, int par, int score, int over, int putts, int fairway, int gir) {
        this.name = name;
        this.address = address;
        this.date = date;
        this.par = par;
        this.score = score;
        this.over = over;
        this.putts = putts;
        this.fairway = fairway;
        this.gir = gir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }
}
