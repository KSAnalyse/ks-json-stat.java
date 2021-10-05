package no.ks.fiks.JsonStat;

/**
 * <h3>JsonStatUnit</h3>
 * <p>
 * A POJO class for unit object, contains dimensionName, base and decimal globals.
 * {@link #dimensionName} is the name of the dimension for the current unit.
 * {@link #base} is what the value is. F.ex if base for dimension (Personer1) is 'personer', it means that the value for
 * the row that has Personer1 is based on persons.
 * {@link #decimals} is how many decimals each dimension has.
 */

public class JsonStatUnit {
    private final String dimensionName;
    private final String base;
    private final int decimals;

    /**
     * <h3>JsonStatUnit constructor</h3>
     * <p>
     * Basic constructor that sets the global variables.
     *
     * @param dimensionName Name of the unit dimension.
     * @param base          This is what the dimension is based upon.
     * @param decimals      This says how many decimals the dimension has.
     */

    public JsonStatUnit(String dimensionName, String base, int decimals) {
        this.dimensionName = dimensionName;
        this.base = base;
        this.decimals = decimals;
    }

    /**
     * <h3>getDimensionName</h3>
     *
     * @return Returns dimension name String.
     */

    public String getDimensionName() {
        return dimensionName;
    }

    /**
     * <h3>getBase</h3>
     *
     * @return Returns the base of the dimension.
     */

    public String getBase() {
        return base;
    }

    /**
     * <h3>getDecimals</h3>
     *
     * @return Returns the number of decimals the dimension has.
     */

    public int getDecimals() {
        return decimals;
    }
}
