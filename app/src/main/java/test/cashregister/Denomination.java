package test.cashregister;

import java.util.Objects;

/**
 * Denomination
 */
public class Denomination {
    private int value;
    private int count;

    public Denomination(int value, int startCount) {
        this.value = value;
        this.count = startCount;
    }
    
    public int getValue() {
        return value;
    }

    public DispenseResult countDispensableFromValue(int fromValue) {
        if (count==0) {
            return null;
        }
        int result = Math.round(fromValue / value);
        int resultRemainder = fromValue % value;
        return new DispenseResult(result, resultRemainder);
    }

    public boolean deductDispensable(DispenseResult dispense) {
        int result = count - dispense.count;
        if (result < 0) {
            return false;
        }
        count -= dispense.count;
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