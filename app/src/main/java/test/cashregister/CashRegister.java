package test.cashregister;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CashRegister {
    List<Denomination> denominations;
    public static void main(String[] args) {
        CashRegister cashRegister = new CashRegister();
        Scanner scanner = new Scanner(System.in);

        System.out.println("ready");

        cashRegister.initialize();

        String input = "";
        while (!(input = scanner.nextLine()).equals("quit")) {
            String[] inputArgs = input.split(" ");
            switch(inputArgs[0]) {
                case "show":
                    cashRegister.showMessage();
                    break;
                case "put":
                    cashRegister.put(Arrays.copyOfRange(inputArgs, 1, inputArgs.length));
                    break;
                case "take":
                    cashRegister.take(Arrays.copyOfRange(inputArgs, 1, inputArgs.length));
                    break;
                case "change":
                    cashRegister.change(inputArgs[1]);
                    break;
            }
        }
        
        System.out.println("Bye");

        scanner.close();
    }

    public void initialize() {
        this.denominations = List.of(
            new Denomination(20, 1),
            new Denomination(10, 2),
            new Denomination(5, 3),
            new Denomination(2, 4),
            new Denomination(1, 5)
        );
    }

    public void put(String... inputs) throws NumberFormatException {
        if (inputs.length!=5) {
            throw new NumberFormatException("Invalid number of arguments supplied for put");
        }
        for (int i=0; i < inputs.length; i++) {
            String val = inputs[i];
            if (!val.matches("[0-9]+")) {
                throw new NumberFormatException(String.format("Invalid number supplied for put at index: %s", val));
            }
            denominations.get(i).addCount(Integer.parseInt(val));
        }
        showMessage();
    }

    public void take(String... inputs) throws NumberFormatException {
        if (inputs.length!=5) {
            throw new NumberFormatException("Invalid number of arguments supplied for take");
        }
        for (int i=0; i < inputs.length; i++) {
            String val = inputs[i];
            if (!val.matches("[0-9]+")) {
                throw new NumberFormatException(String.format("Invalid number supplied for put at index: %s", val));
            }
            denominations.get(i).subtractCount(Integer.parseInt(val));
        }
        showMessage();
    }

    public boolean change(String input) throws NumberFormatException {
        String val = input;
        if (input==null || !val.matches("[0-9]+")) {
            throw new NumberFormatException("Invalid number supplied for change value");
        }

        int initialTotal = denominations.stream()
            .mapToInt(d->d.calculate())
            .sum();

        int changeValue = Integer.parseInt(val);
        Map<Integer, Integer> results = new HashMap<Integer, Integer>();

        if (changeValue < 1 || initialTotal<changeValue) {
            System.out.println("sorry");
            return false;
        }
        
        int lastDispensedIndex = -1;
        int currentValue = changeValue;
        Map<Integer, Integer> initialDenominations = denominations.stream()
                .collect(Collectors.toMap(Denomination::getValue, Denomination::getCount));

        for (int i=0; i < denominations.size(); i++) {
            Denomination denomination = denominations.get(i);
            DispenseResult dispense = denomination.countDispensableFromValue(currentValue);
            boolean hasDeduction = dispense != null && dispense.count > 0;
            if (hasDeduction) {
                lastDispensedIndex = i;
                results.put(denomination.getValue(), dispense.count);
                denomination.deductDispensable(dispense);
                currentValue -= (dispense.count * denomination.getValue());
            } else {
                results.put(denomination.getValue(), 0);
            }

            if (currentValue > 0 && lastDispensedIndex > -1 && i==(denominations.size()-1)) {
                Denomination lastDispensedDenomination = denominations.get(lastDispensedIndex);
                lastDispensedDenomination.addCount(1);
                results.put(lastDispensedIndex, results.get(lastDispensedIndex) - 1);
                currentValue += lastDispensedDenomination.getValue();
                i = lastDispensedIndex;
                lastDispensedIndex = 0;
            }
        }

        boolean hasFailed = results.entrySet().stream().filter(e->initialDenominations.get(e.getKey()) < e.getValue()).count() > 0;
        if (hasFailed) {
            System.out.println("Sorry");
            return false;
        } else {
            System.out.println(String.join(" ", results.values().stream().map(e->Integer.toString(e)).collect(Collectors.toList())));
            return true;
        }
    }

    public List<Denomination> getDenominations() {
        return denominations;
    }

    public void showMessage() {
        String strDenominations = denominations.stream()
            .map(d->Integer.toString(d.getCount())).collect(Collectors.joining(" "));
        int total = denominations.stream()
            .mapToInt(d->d.calculate())
            .sum();
        System.out.println(String.format("$%s %s", total, strDenominations));
    }

}
