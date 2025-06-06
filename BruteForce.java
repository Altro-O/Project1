public class BruteForce {
    public String decryptByBruteForce(String encryptedText, char[] alphabet, CaesarCipher cipher) {
        StringBuilder result = new StringBuilder();
        for (int k = 1; k < alphabet.length; k++) {
            result.append("Ключ: ").append(k).append("\n");
            result.append(cipher.decrypt(encryptedText, k)).append("\n----------------------\n");
        }
        return result.toString();
    }

    public String bruteForceAuto(String encryptedText, char[] alphabet, CaesarCipher cipher) {
        int bestKey = 1;
        int maxSpaces = -1;
        String bestDecryption = "";
        for (int k = 1; k < alphabet.length; k++) {
            String decrypted = cipher.decrypt(encryptedText, k);
            int spaces = 0;
            for (char c : decrypted.toCharArray()) {
                if (c == ' ') spaces++;
            }
            if (spaces > maxSpaces) {
                maxSpaces = spaces;
                bestKey = k;
                bestDecryption = decrypted;
            }
        }
        return "Ключ: " + bestKey + "\n" + bestDecryption;
    }
} 