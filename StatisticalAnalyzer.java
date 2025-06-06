public class StatisticalAnalyzer {
    public int findMostKey(String encryptedText, char[] alphabet, String sampleText, CaesarCipher cipher) {
        return cipher.statisticalAnalysis(encryptedText, sampleText);
    }
} 