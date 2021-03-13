package test.cashregister;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class CashRegisterTest {
    @Test
    public void testCashRegisterHasInitializedWithCorrectValues() {
        CashRegister classUnderTest = new CashRegister();
        classUnderTest.initialize();
        checkValues(classUnderTest, 1, 2, 3, 4, 5, 68);
    }

    @Test
    public void testCashRegisterPutWithCorrectValues() {
        CashRegister classUnderTest = new CashRegister();
        classUnderTest.initialize();

        classUnderTest.put("1 0 0 0 1".split(" "));
        checkValues(classUnderTest, 2, 2, 3, 4, 6, 89);
    }

    @Test
    public void testCashRegisterTakeWithCorrectValues() {
        CashRegister classUnderTest = new CashRegister();
        classUnderTest.initialize();

        classUnderTest.take("1 1 0 0 2".split(" "));
        checkValues(classUnderTest, 0, 1, 3, 4, 3, 36);
    }

    @Test
    public void testCashRegisterChangeWithCorrectValues() {
        CashRegister classUnderTest = new CashRegister();
        classUnderTest.initialize();

        boolean operationResult = classUnderTest.change("32");
        assertTrue(operationResult);
        checkValues(classUnderTest, 0, 1, 3, 3, 5, 36);
    }

    @Test
    public void testCashRegisterChangeWithInvalidChange() {
        CashRegister classUnderTest = new CashRegister();
        classUnderTest.initialize();

        boolean operationResult = classUnderTest.change("69");
        assertFalse(operationResult);
    }

    @Test
    public void testCashRegisterDefaultCase() {
        CashRegister classUnderTest = new CashRegister();
        classUnderTest.initialize();

        //put 1 2 3 0 5
        classUnderTest.put("1 2 3 0 5".split(" "));
        checkValues(classUnderTest, 2, 4, 6, 4, 10, 128);

        //take 1 4 3 0 10
        classUnderTest.take("1 4 3 0 10".split(" "));
        checkValues(classUnderTest, 1, 0, 3, 4, 0, 43);

        //change 11
        assertTrue(classUnderTest.change("11"));
        checkValues(classUnderTest, 1, 0, 2, 1, 0, 32);

        //change 14
        assertFalse(classUnderTest.change("14"));
    }

    private void checkValues(
            CashRegister classUnderTest, 
            int d0, int d1, int d2, int d3, int d4,
            int expectedTotal
        ) {
        assertEquals(
            classUnderTest.denominations,
            List.of(
                new Denomination(20, d0),
                new Denomination(10, d1),
                new Denomination(5, d2),
                new Denomination(2, d3),
                new Denomination(1, d4)
            )
        );
        int total = classUnderTest.getDenominations()
            .stream()
            .mapToInt(d->d.calculate())
            .sum();
        assertEquals(
            expectedTotal,
            total
        );
    }
}
