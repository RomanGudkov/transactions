import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Bank {
    private Map<String, Account> accounts;
    private Set<String> keyBlock;
    private final Random random = new Random();

    public Bank() {
        accounts = new ConcurrentHashMap<>();
        keyBlock = new CopyOnWriteArraySet();
    }

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    public void transfer(String fromAccountNum, String toAccountNum, long amount) {
        int fromNum = Integer.valueOf(fromAccountNum);
        int toNum = Integer.valueOf(toAccountNum);

        String messageTransfer = "Счет " + fromAccountNum
                + " перевел счету "
                + toAccountNum + " -> "
                + amount + " единиц";
        String messageBlocked = "Перевод c " + fromAccountNum
                + " на " + toAccountNum
                + " на " + amount
                + " единиц заблокирован" + "\n";

        synchronized (fromNum > toNum ? fromAccountNum : toAccountNum) {
            synchronized (fromNum > toNum ? toAccountNum : fromAccountNum) {
                boolean fromIsBlocked = keyBlock.contains(fromAccountNum);
                boolean toIsBlocked = keyBlock.contains(toAccountNum);

                if (fromIsBlocked || toIsBlocked) {
                    System.out.println("Необходимо проверить счета на блокирование" + "\n");
                    return;
                }
                if (getBalance(fromAccountNum) == 0 || amount > getBalance(fromAccountNum)) {
                    System.out.println("Перевод невозможен, пополните счет " + fromAccountNum + "\n");
                    return;
                }
                if (amount > 50000) {
                    boolean checkIsFraud = false;
                    try {
                        checkIsFraud = isFraud(fromAccountNum, toAccountNum, amount);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (checkIsFraud) {
                        isBlock(fromAccountNum, toAccountNum);
                        System.out.println(messageBlocked);
                        return;
                    }
                    amountChange(fromAccountNum, toAccountNum, amount);
                    System.out.println(messageTransfer);
                } else {
                    amountChange(fromAccountNum, toAccountNum, amount);
                    System.out.println(messageTransfer);
                }
                System.out.println("Сумма на всех счетах " + getSumAllAccounts() + " единиц" + "\n");
            }
        }
    }

    private void amountChange(String fromAccountNum, String toAccountNum, long amount) {
        long fromAmount = amount;
        long toAmount = accounts.get(toAccountNum).getMoney();
        long sum = fromAmount + toAmount;
        long difference = accounts.get(fromAccountNum).getMoney() - amount;
        accounts.replace(fromAccountNum, new Account(fromAccountNum, difference));
        accounts.replace(toAccountNum, new Account(toAccountNum, sum));
    }

    private void isBlock(String... numAmount) {
        for (String key : numAmount
        ) {
            keyBlock.add(key);
        }
    }

    public long getBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }

    public long getSumAllAccounts() {
        return accounts.values().stream()
                .mapToLong(Account::getMoney)
                .sum();
    }

    public void setAccounts(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public Set<String> getKeyBlock() {
        return keyBlock;
    }
}
