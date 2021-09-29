package no.ks.fiks.JsonStat;

public class JsonStatUnit {
    private final String dimensionName;
    private final String base;
    private final int decimals;

    public JsonStatUnit(String dimensionName, String base, int decimals) {
        this.dimensionName = dimensionName;
        this.base = base;
        this.decimals = decimals;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public String getBase() {
        return base;
    }

    public int getDecimals() {
        return decimals;
    }
}
