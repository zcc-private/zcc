package zcc.es.utils;

import com.hankcs.hanlp.HanLP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HanlpUtils {
    public HanlpUtils() {
    }

    public static List<String> getSplitWords(String sentence) {
        return (List) HanLP.segment(sentence).stream().map((a) -> {
            return a.word;
        }).filter((s) -> {
            return !"`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？ ".contains(s);
        }).collect(Collectors.toList());
    }

    private static List<String> mergeList(List<String> list1, List<String> list2) {
        List<String> result = new ArrayList();
        result.addAll(list1);
        result.addAll(list2);
        return (List)result.stream().distinct().collect(Collectors.toList());
    }

    private static int[] statistic(List<String> allWords, List<String> sentWords) {
        int[] result = new int[allWords.size()];

        for(int i = 0; i < allWords.size(); ++i) {
            result[i] = Collections.frequency(sentWords, allWords.get(i));
        }

        return result;
    }

    public static double getSimilarity(String sentence1, String sentence2) {
        List<String> sent1Words = getSplitWords(sentence1);
        System.out.println(sent1Words);
        List<String> sent2Words = getSplitWords(sentence2);
        System.out.println(sent2Words);
        List<String> allWords = mergeList(sent1Words, sent2Words);
        int[] statistic1 = statistic(allWords, sent1Words);
        int[] statistic2 = statistic(allWords, sent2Words);
        double dividend = 0.0D;
        double divisor1 = 0.0D;
        double divisor2 = 0.0D;

        for(int i = 0; i < statistic1.length; ++i) {
            dividend += (double)(statistic1[i] * statistic2[i]);
            divisor1 += Math.pow((double)statistic1[i], 2.0D);
            divisor2 += Math.pow((double)statistic2[i], 2.0D);
        }

        return dividend / (Math.sqrt(divisor1) * Math.sqrt(divisor2));
    }
}
