package test.cashregister;

import java.util.Objects;

/**
 * Denomination
 */
public class Denomination {
    private int value;
    private int count;

    public Denomination(Denomination d) {
        this.value = d.getValue();
        this.count = d.getCount();
    }

    public Denomination(int value, int startCount) {
        this.value = value;
        this.count = startCount;
    }
    
    public int getValue() {
        return value;
    }

    public int countDispensableFromValue(int fromValue) {
        if (count==0) {
            return -1;
        }
        return Math.round(fromValue / value);
    }

    public boolean deductDispensable(int dispenseCount) {
        int result = count - dispenseCount;
        if (result < 0) {
            return false;
        }
        count -= dispenseCount;
        return true;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void subtractCount(int count) {
        this.count -= count;
    }

    public int calculate() {
        return count * value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==null) {
            return false;
        }
        Denomination that = (Denomination) obj;
        return  Objects.equals(this.count, that.count) &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, count);
    }

}