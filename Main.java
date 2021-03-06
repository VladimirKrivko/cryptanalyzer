import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws IOException {
        welcomeScreen();

        String choice;
        while (!"exit".equalsIgnoreCase(choice = reader.readLine())) {
            switch (choice) {
                case "1" -> {
                    System.out.println("Шифр Цезаря (шифрование):");
                    try {
                        String pathFile = getPathFile();
                        int key = generateEncryptKey();

                        Cipher encrypt = new Cipher(pathFile, key);
                        String text = encrypt.getTextFromFile();
                        String encryptText = encrypt.encrypt(text);
                        encrypt.pushTextToFile(encryptText);
                    } catch (IOException e) {
                        System.out.println("_______________________________________");
                    }
                }
                case "2" -> {
                    System.out.println("Шифр Цезаря (расшифровка):");
                    try {
                        String pathFile = getPathFile();
                        int key = generateEncryptKey();

                        Cipher encrypt = new Cipher(pathFile, -key);
                        String text = encrypt.getTextFromFile();
                        String encryptText = encrypt.encrypt(text);
                        encrypt.pushTextToFile(encryptText);
                    } catch (IOException e) {
                        System.out.println("_______________________________________");
                    }
                }
                case "3" -> {
                    System.out.println("Криптоанализ методом BruteForce:");
                    try {
                        String pathFile = getPathFile();

                        Cipher encrypt = new Cipher(pathFile, 0);
                        String text = encrypt.getTextFromFile();
                        String encryptText = encrypt.bruteForce(text, ", ");
                        encrypt.pushTextToFile(encryptText);
                    } catch (IOException e) {
                        System.out.println("_______________________________________");
                    }
                }
                case "4" -> {
                    System.out.println("Криптоанализ методом статистического анализа:");
                    try {
                        String pathFile = getPathFile();

                        Cipher encryptText = new Cipher(pathFile, 0);
                        String text = encryptText.getTextFromFile();
                        char[] popLettersCipherText = encryptText.frequencyOfLetters(text);
                        System.out.println("Добавим файл с текстом того же автора (Введи add)?\n" +
                                           "Или обойдемся общей частотной статистикой русского алфавита (Введи что-хош)?");
                        String option;
                        if ("add".equals(option = reader.readLine())) {
                            String pathFileAuthor = getPathFile();
                            Cipher openText = new Cipher(pathFileAuthor, 0);
                            char[] popLettersOpenTextAuthor = openText.frequencyOfLetters(openText.getTextFromFile());
                            int key = encryptText.statAnal(popLettersOpenTextAuthor, popLettersCipherText);
                            Cipher.setEncryptKey(key); // разобраться с геттерами сеттерами!!!
                            encryptText.pushTextToFile(encryptText.encrypt(text));
                        } else {
                            char[] popLettersOpenTextAuthor = " оеаинтсрвл".toCharArray();
                            int key = encryptText.statAnal(popLettersOpenTextAuthor, popLettersCipherText);
                            Cipher.setEncryptKey(key);
                            encryptText.pushTextToFile(encryptText.encrypt(text));
                        }
                    } catch (IOException e) {
                        System.out.println("_______________________________________");
                    }
                }
                default -> {
                    System.out.println("Некорректный ввод. Попробуй еще раз.");
                }
            }
            welcomeScreen();
        }
        reader.close();
    }

    static void welcomeScreen() {
        System.out.println("Введи 1 - если надо зашифровать файл.\n" +
                           "Введи 2 - если надо расшифровать файл.\n" +
                           "Введи 3 - если надо расшифровать файл без ключа (brute force).\n" +
                           "Введи 4 - если надо расшифровать файл частотным анализом.\n" +
                           "Введи exit - для выхода из программы.");
    }

    static String getPathFile() throws IOException {
        System.out.println("Введи путь файла.");
        String pathFile = reader.readLine();
        Path fileInputName;
        fileInputName = Path.of(pathFile);
        while (!Files.exists(fileInputName) || Files.isDirectory(fileInputName) || pathFile.equals("")) {
            System.out.println("Указанный файл не существует. Повтори ввод.\n" +
                    "[Введи menu - для выхода в главное меню.]\n" +
                    "_______________________________________");
            pathFile = reader.readLine();
            if (pathFile.equals("menu")) {
                throw new IOException();
            }
            fileInputName = Path.of(pathFile);
        }
        return pathFile;
    }

    public static int generateEncryptKey() throws IOException {
        System.out.println("Введи ключ шифрования.");
        char[] chars = reader.readLine().toCharArray();
        int key = 0;
        for (int i = 0; i < chars.length; i++) {
            if (i % 2 == 0) {
                key += chars[i];
            } else {
                key -= chars[i];
            }
        }
        if (key > Cipher.alphabetRus.length) {
            key %= Cipher.alphabetRus.length;
        }
        return key;
    }
}