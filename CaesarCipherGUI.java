import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CaesarCipherGUI extends JFrame {
    private final CaesarCipher cipher = new CaesarCipher();
    private final FileManager fileManager = new FileManager();
    private final Validator validator = new Validator();
    private final BruteForce bruteForce = new BruteForce();
    private final StatisticalAnalyzer analyzer = new StatisticalAnalyzer();
    private final String projectDir = System.getProperty("user.dir");

    public CaesarCipherGUI() {
        setTitle("Шифр Цезаря");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton encryptBtn = new JButton("Шифрование");
        JButton decryptBtn = new JButton("Расшифровка с ключом");
        JButton bruteBtn = new JButton("Brute force (перебор)");
        JButton bruteAutoBtn = new JButton("Brute force (автоопределение)");
        JButton statBtn = new JButton("Статистический анализ");
        JButton exitBtn = new JButton("Выход");

        mainPanel.add(encryptBtn);
        mainPanel.add(decryptBtn);
        mainPanel.add(bruteBtn);
        mainPanel.add(bruteAutoBtn);
        mainPanel.add(statBtn);
        mainPanel.add(exitBtn);
        add(mainPanel, BorderLayout.CENTER);

        encryptBtn.addActionListener(e -> showEncryptPanel());
        decryptBtn.addActionListener(e -> showDecryptPanel());
        bruteBtn.addActionListener(e -> showBrutePanel());
        bruteAutoBtn.addActionListener(e -> showBruteAutoPanel());
        statBtn.addActionListener(e -> showStatPanel());
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private JPanel fileFieldWithBrowse(JTextField field, String label) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JButton browseBtn = new JButton("Обзор...");
        browseBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(projectDir);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                field.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        panel.add(new JLabel(label), BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.add(browseBtn, BorderLayout.EAST);
        return panel;
    }

    private void showEncryptPanel() {
        JTextField inputField = new JTextField();
        JTextField outputField = new JTextField();
        JTextField keyField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.add(fileFieldWithBrowse(inputField, "Файл для шифрования:"));
        panel.add(fileFieldWithBrowse(outputField, "Файл для сохранения:"));
        JPanel keyPanel = new JPanel(new BorderLayout(5, 5));
        keyPanel.add(new JLabel("Ключ (0-" + (CaesarCipher.ALPHABET.length-1) + "): "), BorderLayout.WEST);
        keyPanel.add(keyField, BorderLayout.CENTER);
        panel.add(keyPanel);
        int res = JOptionPane.showConfirmDialog(this, panel, "Шифрование", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String in = inputField.getText().trim();
            String out = outputField.getText().trim();
            int key;
            try {
                key = Integer.parseInt(keyField.getText().trim());
            } catch (Exception ex) {
                showError("Ключ должен быть целым числом!"); return;
            }
            if (!validator.isFileExists(in)) { showError("Файл не найден!"); return; }
            if (!validator.isValidKey(key, CaesarCipher.ALPHABET)) { showError("Ключ вне диапазона!"); return; }
            try {
                String text = fileManager.readFile(in);
                String encrypted = cipher.encrypt(text, key);
                fileManager.writeFile(encrypted, out);
                showInfo("Шифрование завершено! Результат: " + out);
            } catch (IOException ex) { showError("Ошибка: " + ex.getMessage()); }
        }
    }

    private void showDecryptPanel() {
        JTextField inputField = new JTextField();
        JTextField outputField = new JTextField();
        JTextField keyField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.add(fileFieldWithBrowse(inputField, "Файл для расшифровки:"));
        panel.add(fileFieldWithBrowse(outputField, "Файл для сохранения:"));
        JPanel keyPanel = new JPanel(new BorderLayout(5, 5));
        keyPanel.add(new JLabel("Ключ (0-" + (CaesarCipher.ALPHABET.length-1) + "): "), BorderLayout.WEST);
        keyPanel.add(keyField, BorderLayout.CENTER);
        panel.add(keyPanel);
        int res = JOptionPane.showConfirmDialog(this, panel, "Расшифровка с ключом", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String in = inputField.getText().trim();
            String out = outputField.getText().trim();
            int key;
            try {
                key = Integer.parseInt(keyField.getText().trim());
            } catch (Exception ex) {
                showError("Ключ должен быть целым числом!"); return;
            }
            if (!validator.isFileExists(in)) { showError("Файл не найден!"); return; }
            if (!validator.isValidKey(key, CaesarCipher.ALPHABET)) { showError("Ключ вне диапазона!"); return; }
            try {
                String text = fileManager.readFile(in);
                String decrypted = cipher.decrypt(text, key);
                fileManager.writeFile(decrypted, out);
                showInfo("Расшифровка завершена! Результат: " + out);
            } catch (IOException ex) { showError("Ошибка: " + ex.getMessage()); }
        }
    }

    private void showBrutePanel() {
        JTextField inputField = new JTextField();
        JTextField outputField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.add(fileFieldWithBrowse(inputField, "Файл для перебора:"));
        panel.add(fileFieldWithBrowse(outputField, "Файл для сохранения всех вариантов:"));
        int res = JOptionPane.showConfirmDialog(this, panel, "Brute force", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String in = inputField.getText().trim();
            String out = outputField.getText().trim();
            if (!validator.isFileExists(in)) { showError("Файл не найден!"); return; }
            try {
                String text = fileManager.readFile(in);
                String all = bruteForce.decryptByBruteForce(text, CaesarCipher.ALPHABET, cipher);
                fileManager.writeFile(all, out);
                showInfo("Brute force завершён! Все варианты: " + out);
            } catch (IOException ex) { showError("Ошибка: " + ex.getMessage()); }
        }
    }

    private void showBruteAutoPanel() {
        JTextField inputField = new JTextField();
        JTextField outputField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.add(fileFieldWithBrowse(inputField, "Файл для перебора:"));
        panel.add(fileFieldWithBrowse(outputField, "Файл для сохранения результата:"));
        int res = JOptionPane.showConfirmDialog(this, panel, "Brute force (автоопределение)", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String in = inputField.getText().trim();
            String out = outputField.getText().trim();
            if (!validator.isFileExists(in)) { showError("Файл не найден!"); return; }
            try {
                String text = fileManager.readFile(in);
                String best = bruteForce.bruteForceAuto(text, CaesarCipher.ALPHABET, cipher);
                fileManager.writeFile(best, out);
                showInfo("Brute force (авто) завершён! Результат: " + out);
            } catch (IOException ex) { showError("Ошибка: " + ex.getMessage()); }
        }
    }

    private void showStatPanel() {
        JTextField inputField = new JTextField();
        JTextField sampleField = new JTextField();
        JTextField outputField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.add(fileFieldWithBrowse(inputField, "Зашифрованный файл:"));
        panel.add(fileFieldWithBrowse(sampleField, "Репрезентативный файл:"));
        panel.add(fileFieldWithBrowse(outputField, "Файл для сохранения результата:"));
        int res = JOptionPane.showConfirmDialog(this, panel, "Статистический анализ", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String in = inputField.getText().trim();
            String sample = sampleField.getText().trim();
            String out = outputField.getText().trim();
            if (!validator.isFileExists(in)) { showError("Зашифрованный файл не найден!"); return; }
            if (!validator.isFileExists(sample)) { showError("Репрезентативный файл не найден!"); return; }
            try {
                String encrypted = fileManager.readFile(in);
                String sampleText = fileManager.readFile(sample);
                int bestKey = analyzer.findMostKey(encrypted, CaesarCipher.ALPHABET, sampleText, cipher);
                String decrypted = cipher.decrypt(encrypted, bestKey);
                fileManager.writeFile(decrypted, out);
                showInfo("Статистический анализ завершён! Ключ: " + bestKey + ". Результат: " + out);
            } catch (IOException ex) { showError("Ошибка: " + ex.getMessage()); }
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Успех", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CaesarCipherGUI().setVisible(true));
    }
} 