import junit.framework.TestCase;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BankTest extends TestCase {
    Bank bank;
    Map<String, Account> accounts;
    Map<String, Account> accountsForRecording;
    Set<String> keyIsBlocked;

    @Override
    protected void setUp() {
        bank = new Bank();

        accounts = new HashMap<>();
        accounts.put("1", new Account("1", 40000));
        accounts.put("2", new Account("2", 60000));
        accounts.put("3", new Account("3", 40000));
        accounts.put("4", new Account("4", 60000));
        bank.setAccounts(accounts);

        accountsForRecording = new HashMap<>();
        accountsForRecording.put("11", new Account("11", 1100));
        accountsForRecording.put("22", new Account("22", 2200));
        accountsForRecording.put("33", new Account("33", 3300));

        keyIsBlocked = new HashSet<>();
        keyIsBlocked.add("1");
        keyIsBlocked.add("2");
    }

    public void testTransfer() {
        String message = "Остаток на счете после перевода.";
        System.out.println("\n" + message);
        bank.transfer(accounts.get("1").getAccNumber(), accounts.get("3").getAccNumber(), accounts.get("1").getMoney());
        long expected = 0;
        long actual = accounts.get("1").getMoney();
        System.out.println(expected + " -> " + actual);
        assertEquals(expected, actual);

        String message1 = "Перевод при нулевом балансе.";
        System.out.println("\n" + message1);
        long expected1 = 0;
        long actual1 = accounts.get("1").getMoney();
        System.out.println(expected1 + " -> " + actual1);
        assertEquals(expected1, actual1);

        String message2 = "Ответ от СБ на блокирование счетов.";
        System.out.println("\n" + message2);
        bank.transfer(accounts.get("2").getAccNumber(), accounts.get("3").getAccNumber(), accounts.get("2").getMoney());
        boolean expected2 = true;
        boolean actual2;
        try {
            actual2 = bank.isFraud(accounts.get("2").getAccNumber(), accounts.get("3").getAccNumber(), accounts.get("2").getMoney());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(expected2 + " -> " + actual2);
        assertEquals(expected2, actual2);

        String message3 = "Блокирование счета.";
        System.out.println("\n" + message3);
        String expected3 = "2, 3";
        String actual3 = bank.getKeyBlock().toString();
        System.out.println(expected3 + " -> " + actual3);
    }

    public void testGetBalance() {
        String message = "Возвращает сумму на счете клиента.";
        System.out.println("\n" + message + "\n");

        long expected = 60000;
        System.out.println(expected);
        long actual = bank.getBalance("2");
        System.out.println(actual);
        assertEquals(expected, actual);
    }

    public void testGetSumAllAccounts() {
        String message = "Возвращает сумму на всех счетах банка.";
        System.out.println("\n" + message + "\n");

        long expected = 200_000;
        System.out.println(expected);
        long actual = bank.getSumAllAccounts();
        System.out.println(actual);
        assertEquals(expected, actual);
    }

    public void testSetAccounts() {
        String message = "Добавляет Map<> коллекцию Account в класс Bank";
        System.out.println("\n" + message + "\n");

        Map<String, Account> expected = accountsForRecording;
        expected.forEach((s, account) -> System.out.println(s + " - " + account.getAccNumber() + " - " + account.getMoney()));
        System.out.println();
        bank.setAccounts(accountsForRecording);
        Map<String, Account> actual = bank.getAccounts();
        actual.forEach((s, account) -> System.out.println(s + " - " + account.getAccNumber() + " - " + account.getMoney()));

        assertEquals(expected, actual);
    }

    public void testGetAccounts() {
        String message = "Возвращает общий список счетов банка.";
        System.out.println("\n" + message + "\n");

        Map<String, Account> expected = accounts;
        expected.forEach((s, account) -> System.out.println(s + " - " + account.getAccNumber() + " - " + account.getMoney()));
        System.out.println();
        Map<String, Account> actual = bank.getAccounts();
        actual.forEach((s, account) -> System.out.println(s + " - " + account.getAccNumber() + " - " + account.getMoney()));
        assertEquals(expected, actual);
    }

    @Override
    protected void tearDown() {
        accounts.clear();
        accountsForRecording.clear();
    }
}
