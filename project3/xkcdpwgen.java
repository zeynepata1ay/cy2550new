import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class xkcdpwgen {
    private static final String WORDLIST_FILE = "words.txt";

    public static void main(String[] args) {
        int words = 4;
        int caps = 0;
        int numbers = 0;
        int symbols = 0;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-w") || args[i].equals("--words")) {
                words = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("-c") || args[i].equals("--caps")) {
                caps = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("-n") || args[i].equals("--numbers")) {
                numbers = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("-s") || args[i].equals("--symbols")) {
                symbols = Integer.parseInt(args[i + 1]);
            }
        }

        List<String> wordList = readWordListFromFile(WORDLIST_FILE);
        String password = generatePassword(wordList, words, caps, numbers, symbols);
        System.out.println(password);
    }

    private static List<String> readWordListFromFile(String fileName) {
        List<String> wordList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordList.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading word list from file: " + e.getMessage());
            System.exit(1);
        }
        return wordList;
    }

    private static String generatePassword(List<String> wordList, int words, int caps, int numbers, int symbols) {
        Random random = new Random();
        List<String> randomWords = new ArrayList<>();

        for (int i = 0; i < words; i++) {
            randomWords.add(wordList.get(random.nextInt(wordList.size())));
        }

        for (int i = 0; i < caps; i++) {
            int index = random.nextInt(words);
            String word = randomWords.get(index);
            randomWords.set(index, capitalizeFirstLetter(word));
        }

        for (int i = 0; i < numbers; i++) {
            int index = random.nextInt(randomWords.size() + 1);
            randomWords.add(index, String.valueOf(random.nextInt(10)));
        }

        String symbolList = "~!@#$%^&*.:;";
        for (int i = 0; i < symbols; i++) {
            int index = random.nextInt(randomWords.size() + 1);
            randomWords.add(index, String.valueOf(symbolList.charAt(random.nextInt(symbolList.length()))));
        }

        StringBuilder password = new StringBuilder();
        for (String word : randomWords) {
            password.append(word);
        }

        return password.toString();
    }

    private static String capitalizeFirstLetter(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}