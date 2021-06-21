package htlgkr.scorebook;

import java.util.Comparator;

public class Round  {
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
    }  // name of the golfclub

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    public int getOver() {
        return over;
    }

    public String getAddress() {
        return address;
    }

    public int getPar() {
        return par;
    }

    public int getPutts() {
        return putts;
    }

    public int getFairway() {
        return fairway;
    }

    public int getGir() {
        return gir;
    }



    static class SortByDate implements Comparator<Round> {


        @Override
        public int compare(Round o1, Round o2) {
            return o2.getDate().compareTo(o1.getDate());
        }
    }



    /*@Override
    public int compareTo(Round o) {
        return getDate().compareTo(o.getDate());
    }

     */
/*
    @Override
    public int compare(Round o1, Round o2) {
        return o1.getDate().compareTo(o2.getDate());
    }

 */
}



