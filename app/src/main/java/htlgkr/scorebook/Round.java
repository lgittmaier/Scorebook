package htlgkr.scorebook;

public class Round {
    String name;        //name of the golf club
    String address;     //address when Button setAddress was hit
    String date;        //format: dd-MM-yyyy HH:mm
    int par;
    int score;
    int over;           //over par
    boolean fairway;    //fairway hit
    boolean gir;        //green in regulation

    public Round(String name, String address, String date, int par, int score, int over, boolean fairway, boolean gir) {
        this.name = name;
        this.address = address;
        this.date = date;
        this.par = par;
        this.score = score;
        this.over = over;
        this.fairway = fairway;
        this.gir = gir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPar() {
        return par;
    }

    public void setPar(int par) {
        this.par = par;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getOver() {
        return over;
    }

    public void setOver(int over) {
        this.over = over;
    }

    public boolean isFairway() {
        return fairway;
    }

    public void setFairway(boolean fairway) {
        this.fairway = fairway;
    }

    public boolean isGir() {
        return gir;
    }

    public void setGir(boolean gir) {
        this.gir = gir;
    }
}
