import java.util.*;

public class Main {
    public static void main(String[] args) {

        Bank townBank = new Bank();
        townBank.setAccounts(addAccount(300));
        System.out.println("Сумма в банке " + townBank.getSumAllAccounts() + " единиц" + "\n");

        List<Map.Entry<String, Account>> entries = new ArrayList<>(townBank.getAccounts().entrySet());
        Random random = new Random();

        for (Map.Entry entry : townBank.getAccounts().entrySet()
        ) {
            Map.Entry<String, Account> randomEntry = entries.get(random.nextInt(entries.size()));
            final String fromNum = entry.getKey().toString();
            String toNum = randomEntry.getKey();
            long amount = townBank.getAccounts().get(entry.getKey()).getMoney();
            if (!fromNum.equals(toNum)) {
                new Thread(() -> {
                    townBank.transfer(fromNum, toNum, amount);
                }).start();
            }
        }
    }

    private static Map<String, Account> addAccount(int clients) {
        Map<String, Account> clientAccount = new HashMap<>();
        long minMoney = 100;
        long maxMoney = 53_000;
        for (int i = 0; i < clients; i++) {
            String accNumber = String.valueOf(i);
            long money = minMoney + (long) (Math.random() * ((maxMoney - minMoney) + 1));
            clientAccount.put(accNumber, new Account(accNumber, money));
        }
        return clientAccount;
    }
}