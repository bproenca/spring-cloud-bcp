package br.com.bcp.limitsservice.bean;

public class LimitConfiguration {
    private int minimum;
    private int maximum;

    public LimitConfiguration(int minimum, int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    protected LimitConfiguration() {  }

    /**
     * @return the minimum
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * @return the maximum
     */
    public int getMaximum() {
        return maximum;
    }

    /**
     * @param maximum the maximum to set
     */
    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    /**
     * @param minimum the minimum to set
     */
    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    @Override
    public String toString() {
        return "{" +
            " minimum='" + getMinimum() + "'" +
            ", maximum='" + getMaximum() + "'" +
            "}";
    }

}