package no.ks.fiks.JsonStat;

public class JsonStatUnit {
    private String base;
    private int decimals;

    public JsonStatUnit(String base, int decimals) {
        this.base = base;
        this.decimals = decimals;
    }

    public String getBase() {
        return base;
    }

    public int getDecimals() {
        return decimals;
    }
}
