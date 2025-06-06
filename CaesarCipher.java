import java.util.HashMap;
import java.util.Map;

public class CaesarCipher {
    public static final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з',
        'и','к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
        'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};
    private static final Map<Character, Integer> alphabetIndex = new HashMap<>();
    static {
        for (int i = 0; i < ALPHABET.length; i++) {
            alphabetIndex.put(ALPHABET[i], i);
        }
    }

    public String encrypt(String text, int key) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            Integer idx = alphabetIndex.get(c);
            if (idx == null) {
                sb.append(c);
            } else {
                int newIdx = (idx + key) % ALPHABET.length;
                if (newIdx < 0) newIdx += ALPHABET.length;
                sb.append(ALPHABET[newIdx]);
            }
        }
        return sb.toString();
    }

    public String decrypt(String text, int key) {
        return encrypt(text, -key);
    }

    public String bruteForce(String text) {
        StringBuilder result = new StringBuilder();
        for (int k = 1; k < ALPHABET.length; k++) {
            result.append("Ключ: ").append(k).append("\n");
            result.append(decrypt(text, k)).append("\n----------------------\n");
        }
        return result.toString();
    }

    public int statisticalAnalysis(String encrypted, String sample) {
        int bestKey = 0;
        double minDiff = Double.MAX_VALUE;
        double[] sampleFreq = getCharFrequencies(sample);
        for (int k = 0; k < ALPHABET.length; k++) {
            String decrypted = decrypt(encrypted, k);
            double[] decryptedFreq = getCharFrequencies(decrypted);
            double diff = 0;
            for (int i = 0; i < ALPHABET.length; i++) {
                diff += Math.pow(sampleFreq[i] - decryptedFreq[i], 2);
            }
            if (diff < minDiff) {
                minDiff = diff;
                bestKey = k;
            }
        }
        return bestKey;
    }

    private double[] getCharFrequencies(String text) {
        double[] freq = new double[ALPHABET.length];
        int total = 0;
        for (char c : text.toCharArray()) {
            Integer idx = alphabetIndex.get(c);
            if (idx != null) {
                freq[idx]++;
                total++;
            }
        }
        if (total > 0) {
            for (int i = 0; i < freq.length; i++) {
                freq[i] /= total;
            }
        }
        return freq;
    }
} 