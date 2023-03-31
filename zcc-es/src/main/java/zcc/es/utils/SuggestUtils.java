package zcc.es.utils;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.completion.FuzzyOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zcc.es.Bean.RangQueryBean;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

@Component
public class SuggestUtils {
    private static final Logger log = LoggerFactory.getLogger(SuggestUtils.class);
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    private static RestHighLevelClient client;

    public SuggestUtils() {
    }

    @PostConstruct
    public void initClient() {
        client = this.restHighLevelClient;
    }

    public static void main(String[] args) {
        LocalDateTime date1 = LocalDateTime.now().minusMonths(1L);
        String str = date1.toLocalDate().toString();
        String now1 = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        String preMonth = LocalDateTime.now().minusMonths(1L).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL));
        String now = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
    }

    public static Set<String> hostSearch(String indexName, String input, int size, String fields) {
        if (size == 0) {
            boolean size1 = true;
        }

        String preMonth = LocalDateTime.now().minusMonths(1L).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        String now = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        BoolQueryBuilder b = QueryBuilders.boolQuery();
        new ArrayList();
        new RangQueryBean();
        SearchResponse searchResponse = null;
        Aggregations aggregations = ((SearchResponse)searchResponse).getAggregations();
        Set<String> keywords = null;
        if (aggregations != null) {
            keywords = new HashSet();
            Terms hotSearch = (Terms)aggregations.get("hotSearch");
            List<? extends Terms.Bucket> buckets = hotSearch.getBuckets();
            Iterator var14 = buckets.iterator();

            while(var14.hasNext()) {
                Terms.Bucket bucket = (Terms.Bucket)var14.next();
                if (bucket.getKey().toString().length() <= 10) {
                    keywords.add((String)bucket.getKey());
                }
            }
        }

        return keywords;
    }

    public static Set<String> suggest(String indexName, String input, int size, String fields) {
        if (size == 0) {
            size = 5;
        }

        LinkedHashSet<String> result = new LinkedHashSet();
        String[] fieldArr = fields.split(",");
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        String[] var7 = fieldArr;
        int var8 = fieldArr.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            String field = var7[var9];
            CompletionSuggestionBuilder csb = ((CompletionSuggestionBuilder) SuggestBuilders.completionSuggestion(field).prefix(input).size(size)).skipDuplicates(true);
            suggestBuilder.addSuggestion(field.concat("_key"), csb);
        }

        SearchResponse response = searchRespones(indexName, suggestBuilder);
        Suggest suggest = response.getSuggest();
        String[] var16 = fieldArr;
        int var17 = fieldArr.length;

        for(int var18 = 0; var18 < var17; ++var18) {
            String field = var16[var18];
            Suggest.Suggestion suggestion = suggest.getSuggestion(field.concat("_key"));
            setResult(suggestion, result, size);
        }

        return result;
    }

    public static Set<String> suggest(String indexName, String searchValue, String field, int size) {
        if (size == 0) {
            size = 5;
        }

        if (RegexUtil.checkLetter(searchValue)) {
            return getPinyinSuggestResult(indexName, searchValue, field, size);
        } else {
            return RegexUtil.checkChinese(searchValue) ? getSuggestResult(indexName, searchValue, field, size) : getPinyinSuggestResult(indexName, PingYinUtil.getFullSpell(searchValue), field, size);
        }
    }

    private static void setResult(Suggest.Suggestion suggestTextsuggestion, LinkedHashSet<String> result) {
        List<Suggest.Suggestion.Entry> entries = suggestTextsuggestion.getEntries();
        entries.forEach((entry) -> {
            List<Suggest.Suggestion.Entry.Option> options = entry.getOptions();
            options.forEach((option) -> {
                result.add(option.getText().toString());
            });
        });
    }

    private static void setResult(Suggest.Suggestion suggestTextsuggestion, LinkedHashSet<String> result, int size) {
        if (result.size() < size) {
            List<Suggest.Suggestion.Entry> entries = suggestTextsuggestion.getEntries();
            entries.forEach((entry) -> {
                List<Suggest.Suggestion.Entry.Option> options = entry.getOptions();
                options.forEach((option) -> {
                    if (result.size() < size) {
                        result.add(option.getText().toString());
                    }
                });
            });
        }
    }

    private static SearchResponse searchRespones(String indexName, SuggestBuilder suggestBuilder) {
        SearchRequest searchRequest = new SearchRequest(new String[]{indexName});
        SearchSourceBuilder searchSourceBuilder = (new SearchSourceBuilder()).suggest(suggestBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse getResponse = null;

        try {
            getResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            return getResponse;
        } catch (IOException var6) {
            throw new RuntimeException("关键词搜索出错", var6);
        }
    }

    private static SearchResponse searchRespones(String indexName, SearchSourceBuilder suggestBuilder) {
        SearchRequest searchRequest = new SearchRequest(new String[]{indexName});
        searchRequest.source(suggestBuilder);
        SearchResponse getResponse = null;

        try {
            getResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            return getResponse;
        } catch (IOException var5) {
            throw new RuntimeException("关键词搜索出错", var5);
        }
    }

    public static Set<String> getPinyinSuggestResult(String indexName, String searchValue, String field, int size) {
        LinkedHashSet<String> result = new LinkedHashSet();
        getSuggestResult(result, field.concat(".full_pinyin"), indexName, searchValue, false, size);
        if (result.size() == 0) {
            getSuggestResult(result, field.concat(".full_pinyin"), indexName, searchValue, true, size);
            if (result.size() == 0) {
                getSuggestResult(result, field.concat(".prefix_pinyin"), indexName, searchValue, false, size);
            }
        }

        return result;
    }

    public static Set<String> getSuggestResult(String indexName, String searchValue, String field, int size) {
        LinkedHashSet<String> result = new LinkedHashSet();
        getSuggestResult(result, field, indexName, searchValue, false, size);
        return result;
    }

    private static void getSuggestResult(LinkedHashSet<String> result, String field, String indexName, String searchValue, boolean isFuzzy, int size) {
        SearchSourceBuilder sourceBuilder = getSearchSourceBuilder(field, searchValue, isFuzzy, size);
        SearchResponse response = searchRespones(indexName, sourceBuilder);
        Suggest.Suggestion suggestion = response.getSuggest().getSuggestion(field.concat("_key"));
        setResult(suggestion, result);
    }

    private static SuggestionBuilder getCompletionSuggestionBuilder(String field, String value, Boolean isFuzzy, int size) {
        return isFuzzy ? SuggestBuilders.completionSuggestion(field).prefix(value, FuzzyOptions.builder().setFuzziness(2).build()).skipDuplicates(true).size(size) : SuggestBuilders.completionSuggestion(field).prefix(value).skipDuplicates(true).size(size);
    }

    private static SearchSourceBuilder getSearchSourceBuilder(String field, String value, Boolean isFuzzy, int size) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        SuggestionBuilder completionSuggestionBuilder = getCompletionSuggestionBuilder(field, value, isFuzzy, size);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion(field.concat("_key"), completionSuggestionBuilder);
        sourceBuilder.suggest(suggestBuilder);
        return sourceBuilder;
    }
}
