package lets.play.quiziyapa;

import java.util.ArrayList;
import java.util.List;

public class Scorelist {

    public int s;
    public int t;
    public String n;


    List<String> list=new ArrayList<>();
    List<String> cj = new ArrayList<>();

    public Scorelist(int S, int T) {
        this.s = S;
        this.t = T;
        list.add(String.valueOf(s)); 
        list.add(ResultActivity.a);

    }

    public Scorelist(List<String> CJ) {
        this.cj = CJ;
    }

    public String TOTAL(String N) {
        this.n = N;
        return N;
    }

    public List<String> getCj() {
        return cj;
    }

    public int getT() {
        return t;
    }

    public String getN() {
        return n;
    }

    public List<String> getList() {
        return list;
    }


}
