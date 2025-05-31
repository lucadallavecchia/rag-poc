package com.ldv.rag_poc.utils;

import java.text.BreakIterator;
import java.util.*;

public class TextUtils {

    public static List<String> cleanAndSplit(String rawText, int maxChunkSize) {
        // Pulizia base
        String cleaned = rawText
                .replaceAll("\\\\n", "\n") // <-- questo converte \\n in newline reale
                //.replaceAll("!\\[.*?\\]\\(.*?\\)", "") // immagini markdown
               //.replaceAll("\\[.*?\\]\\(.*?\\)", "")  // link markdown
                //.replaceAll("[*_#>`|\\-]+", "")        // markdown / simboli
                //.replaceAll("\\s{2,}", " ")            // spazi multipli
                //.replaceAll("\\n+", " ")               // newline => spazio
                .trim();

        // Split per frasi e aggregazione in chunk
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.ENGLISH);
        iterator.setText(cleaned);

        List<String> chunks = new ArrayList<>();
        StringBuilder currentChunk = new StringBuilder();

        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            String sentence = cleaned.substring(start, end).trim();
            if (sentence.isEmpty()) continue;

            if (currentChunk.length() + sentence.length() > maxChunkSize) {
                chunks.add(currentChunk.toString().trim());
                currentChunk.setLength(0);
            }

            currentChunk.append(sentence).append(" ");
        }

        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString().trim());
        }

        return chunks;
    }
}

