import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.*;
public class MarkovChains {
    private static final HashMap<String, ArrayList<String>> markovChain = new HashMap<>();
        private static final ArrayList<String> sentenceStarters = new ArrayList<>();

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the filename if'n you so please: ");
            String filename = scanner.nextLine();

            try {
                trainModel(filename);
            } catch (IOException e) {
                System.err.println("Error reading the file: " + e.getMessage());
                return;
            }

            System.out.print("How many words would you like to generate? ");
            int numberOfWords = scanner.nextInt();
            scanner.nextLine(); // consumes the new line

            String generatedText = generateText(numberOfWords);

            System.out.print("Enter output filename: ");
            String outputFilename = scanner.nextLine();
            try {
                Files.writeString(Paths.get(outputFilename), generatedText);
                System.out.println("Generated text saved to " + outputFilename);
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }

        private static void trainModel(String filename) throws IOException {
            List<String> lines = Files.readAllLines(Paths.get(filename));

            String prevWord = ".";
            for (String line : lines) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    markovChain.putIfAbsent(prevWord, new ArrayList<>());
                    markovChain.get(prevWord).add(word);

                    if (prevWord.endsWith(".") || prevWord.endsWith("!") || prevWord.endsWith("?")) {
                        sentenceStarters.add(word);
                    }

                    prevWord = word;
                }
            }
        }

        private static String generateText(int numberOfWords) {
            if (sentenceStarters.isEmpty()) return "";

            Random random = new Random();
            StringBuilder text = new StringBuilder();
            String currentWord = sentenceStarters.get(random.nextInt(sentenceStarters.size()));

            for (int i = 0; i < numberOfWords; i++) {
                text.append(currentWord).append(" ");
                ArrayList<String> nextWords = markovChain.get(currentWord);
                if (nextWords == null || nextWords.isEmpty()) break;
                currentWord = nextWords.get(random.nextInt(nextWords.size()));
            }

            return text.toString().trim();
        }
    }


