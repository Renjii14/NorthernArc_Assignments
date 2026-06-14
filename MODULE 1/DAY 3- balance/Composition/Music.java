package Composition;

public class Music {
    private String ton;

    public Music(String ton) {
        this.ton = ton;
    }

    public String getTon() {
        return ton;
    }

    public void setTon(String ton) {
        this.ton = ton;
    }

    @Override
    public String toString() {
        return "Music [ton=" + ton + "]";
    }
}
