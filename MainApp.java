import java.util.Scanner;
import java.io.IOException;

public class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CaesarCipher cipher = new CaesarCipher();
        FileManager fileManager = new FileManager();
        Validator validator = new Validator();
        BruteForce bruteForce = new BruteForce();
        StatisticalAnalyzer analyzer = new StatisticalAnalyzer();
        boolean running = true;

        System.out.println("Добро пожаловать в программу 'Шифр Цезаря'!");
        System.out.println("------------------------------------------------");
        System.out.println("Подсказка: все файлы лучше класть в папку с программой и указывать только имя файла, например: input.txt");
        System.out.println("Если файл в другой папке, укажите полный путь, например: C:/Users/Имя/Desktop/input.txt");

        while (running) {
            System.out.println("\nМеню:");
            System.out.println("1. Шифрование файла (зашифровать текст)");
            System.out.println("2. Расшифровка файла с помощью ключа");
            System.out.println("3. Brute force (перебор всех ключей, вывод всех вариантов)");
            System.out.println("4. Статистический анализ (автоматический взлом, нужен пример текста)");
            System.out.println("5. Brute force (автоопределение, только один вариант)");
            System.out.println("0. Выход");
            System.out.print("\nВведите номер режима и нажмите Enter: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n--- Шифрование файла ---");
                    System.out.print("Введите имя или путь исходного файла: ");
                    String inputFile = scanner.nextLine().trim();
                    if (!validator.isFileExists(inputFile)) {
                        System.out.println("[Ошибка] Файл не найден! Проверьте имя и попробуйте снова.");
                        break;
                    }
                    System.out.print("Введите имя или путь для сохранения зашифрованного файла: ");
                    String outputFile = scanner.nextLine().trim();
                    System.out.print("Введите ключ (целое число от 0 до " + (CaesarCipher.ALPHABET.length-1) + "): ");
                    int key;
                    try {
                        key = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("[Ошибка] Ключ должен быть целым числом!");
                        break;
                    }
                    if (!validator.isValidKey(key, CaesarCipher.ALPHABET)) {
                        System.out.println("[Ошибка] Ключ вне диапазона! Допустимые значения: 0 - " + (CaesarCipher.ALPHABET.length-1));
                        break;
                    }
                    try {
                        String text = fileManager.readFile(inputFile);
                        String encrypted = cipher.encrypt(text, key);
                        fileManager.writeFile(encrypted, outputFile);
                        System.out.println("[Успех] Шифрование завершено! Результат сохранён в " + outputFile);
                    } catch (IOException e) {
                        System.out.println("[Ошибка] Не удалось обработать файлы: " + e.getMessage());
                    }
                    break;
                case "2":
                    System.out.println("\n--- Расшифровка файла с помощью ключа ---");
                    System.out.print("Введите имя или путь зашифрованного файла: ");
                    String encFile = scanner.nextLine().trim();
                    if (!validator.isFileExists(encFile)) {
                        System.out.println("[Ошибка] Файл не найден! Проверьте имя и попробуйте снова.");
                        break;
                    }
                    System.out.print("Введите имя или путь для сохранения расшифрованного файла: ");
                    String decFile = scanner.nextLine().trim();
                    System.out.print("Введите ключ (целое число от 0 до " + (CaesarCipher.ALPHABET.length-1) + "): ");
                    int decKey;
                    try {
                        decKey = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("[Ошибка] Ключ должен быть целым числом!");
                        break;
                    }
                    if (!validator.isValidKey(decKey, CaesarCipher.ALPHABET)) {
                        System.out.println("[Ошибка] Ключ вне диапазона! Допустимые значения: 0 - " + (CaesarCipher.ALPHABET.length-1));
                        break;
                    }
                    try {
                        String text = fileManager.readFile(encFile);
                        String decrypted = cipher.decrypt(text, decKey);
                        fileManager.writeFile(decrypted, decFile);
                        System.out.println("[Успех] Расшифровка завершена! Результат сохранён в " + decFile);
                    } catch (IOException e) {
                        System.out.println("[Ошибка] Не удалось обработать файлы: " + e.getMessage());
                    }
                    break;
                case "3":
                    System.out.println("\n--- Brute force (перебор всех ключей) ---");
                    System.out.println("ВНИМАНИЕ: В файл будут записаны ВСЕ варианты расшифровки. Откройте файл и выберите правильный вариант вручную.");
                    System.out.print("Введите имя или путь зашифрованного файла: ");
                    String bruteFile = scanner.nextLine().trim();
                    if (!validator.isFileExists(bruteFile)) {
                        System.out.println("[Ошибка] Файл не найден! Проверьте имя и попробуйте снова.");
                        break;
                    }
                    System.out.print("Введите имя или путь для сохранения всех вариантов: ");
                    String bruteOut = scanner.nextLine().trim();
                    try {
                        String text = fileManager.readFile(bruteFile);
                        String allVariants = bruteForce.decryptByBruteForce(text, CaesarCipher.ALPHABET, cipher);
                        fileManager.writeFile(allVariants, bruteOut);
                        System.out.println("[Успех] Brute force завершён! Все варианты сохранены в " + bruteOut);
                    } catch (IOException e) {
                        System.out.println("[Ошибка] Не удалось обработать файлы: " + e.getMessage());
                    }
                    break;
                case "4":
                    System.out.println("\n--- Статистический анализ (автоматический взлом) ---");
                    System.out.println("Для этого режима нужен пример текста (репрезентативный файл) на том же языке и стиле, что и зашифрованный текст.");
                    System.out.print("Введите имя или путь зашифрованного файла: ");
                    String statFile = scanner.nextLine().trim();
                    if (!validator.isFileExists(statFile)) {
                        System.out.println("[Ошибка] Файл не найден! Проверьте имя и попробуйте снова.");
                        break;
                    }
                    System.out.print("Введите имя или путь к репрезентативному (примерному) тексту: ");
                    String sampleFile = scanner.nextLine().trim();
                    if (!validator.isFileExists(sampleFile)) {
                        System.out.println("[Ошибка] Файл не найден! Проверьте имя и попробуйте снова.");
                        break;
                    }
                    System.out.print("Введите имя или путь для сохранения расшифрованного файла: ");
                    String statOut = scanner.nextLine().trim();
                    try {
                        String encrypted = fileManager.readFile(statFile);
                        String sample = fileManager.readFile(sampleFile);
                        int bestKey = analyzer.findMostKey(encrypted, CaesarCipher.ALPHABET, sample, cipher);
                        String decrypted = cipher.decrypt(encrypted, bestKey);
                        fileManager.writeFile(decrypted, statOut);
                        System.out.println("[Успех] Статистический анализ завершён! Ключ: " + bestKey + ". Результат сохранён в " + statOut);
                    } catch (IOException e) {
                        System.out.println("[Ошибка] Не удалось обработать файлы: " + e.getMessage());
                    }
                    break;
                case "5":
                    System.out.println("\n--- Brute force (автоопределение) ---");
                    System.out.println("Программа выберет только один наиболее вероятный вариант (по количеству пробелов).");
                    System.out.print("Введите имя или путь зашифрованного файла: ");
                    String bruteAutoFile = scanner.nextLine().trim();
                    if (!validator.isFileExists(bruteAutoFile)) {
                        System.out.println("[Ошибка] Файл не найден! Проверьте имя и попробуйте снова.");
                        break;
                    }
                    System.out.print("Введите имя или путь для сохранения результата: ");
                    String bruteAutoOut = scanner.nextLine().trim();
                    try {
                        String text = fileManager.readFile(bruteAutoFile);
                        String bestVariant = bruteForce.bruteForceAuto(text, CaesarCipher.ALPHABET, cipher);
                        fileManager.writeFile(bestVariant, bruteAutoOut);
                        System.out.println("[Успех] Brute force (авто) завершён! Результат сохранён в " + bruteAutoOut);
                    } catch (IOException e) {
                        System.out.println("[Ошибка] Не удалось обработать файлы: " + e.getMessage());
                    }
                    break;
                case "0":
                    System.out.println("Спасибо за использование программы! До свидания.");
                    running = false;
                    break;
                default:
                    System.out.println("[Ошибка] Некорректный выбор! Введите число от 0 до 5.");
            }
        }
        scanner.close();
    }
} 