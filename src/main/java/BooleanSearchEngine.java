import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> wordList = new HashMap<>();


    public BooleanSearchEngine(File pdfsDir) throws IOException {
        for (File pdf : Objects.requireNonNull(pdfsDir.listFiles())) {
            if (pdf.isDirectory())
                continue;
            var doc = new PdfDocument(new PdfReader(pdf));

            for (int pageNum = 1; pageNum <= doc.getNumberOfPages(); pageNum++) {
                PdfPage page = doc.getPage(pageNum);

                var text = PdfTextExtractor.getTextFromPage(page);

                var words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(), freqs.getOrDefault(word, 0) + 1); // Подсчитываю частоту использования слов
                }

                for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                    List<PageEntry> pagesList = new ArrayList<>();
                    if (wordList.containsKey(entry.getKey())) {
                        pagesList = wordList.get(entry.getKey());
                    }
                    pagesList.add(new PageEntry(pdf.getName(), pageNum, entry.getValue()));
                    wordList.put(entry.getKey(), pagesList);
                    Collections.sort(wordList.get(entry.getKey()));
                }
            }
        }
    }


    @Override
    public List<PageEntry> search(String word) {
        if (wordList.containsKey(word)) {
            return wordList.get(word);
        }
        return Collections.emptyList();
    }


}
