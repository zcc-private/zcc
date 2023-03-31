package zcc.es.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public enum JacksonUtil {
    INSTANCE {
        public String toJson(Object object) {
            String json = null;
            if (null != object) {
                try {
                    json = JacksonUtil.mapper.writeValueAsString(object);
                } catch (JsonProcessingException var4) {
                    throw new RuntimeException("转化为Json", var4);
                }
            }

            return json;
        }

        public <T> T jsonToBean(String jsonStr, Class<T> cls) {
            T t = null;
            if (!StringUtils.isEmpty(jsonStr)) {
                try {
                    JacksonUtil.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                    JacksonUtil.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    t = JacksonUtil.mapper.readValue(jsonStr, cls);
                } catch (IOException var5) {
                    throw new RuntimeException("json转换Bean出现错误", var5);
                }
            }

            return t;
        }

        public Map<String, Object> jsonToMap(String jsonStr) {
            if (!StringUtils.isEmpty(jsonStr)) {
                try {
                    return (Map)JacksonUtil.mapper.readValue(jsonStr, Map.class);
                } catch (IOException var3) {
                    throw new RuntimeException("json转换Map出现错误", var3);
                }
            } else {
                return null;
            }
        }

        public <T> T mapToBean(Map<?, ?> map, Class<T> clazz) {
            if (!Tools.isEmpty(map)) {
                try {
                    JacksonUtil.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                    JacksonUtil.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    return JacksonUtil.mapper.readValue(this.toJson(map), clazz);
                } catch (IOException var4) {
                    throw new RuntimeException("map转化Json异常出现错误", var4);
                }
            } else {
                return null;
            }
        }

        public <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            } else {
                JavaType javaType = JacksonUtil.mapper.getTypeFactory().constructParametricType(List.class, new Class[]{beanType});

                try {
                    List<T> list = (List)JacksonUtil.mapper.readValue(jsonData, javaType);
                    return list;
                } catch (Exception var5) {
                    throw new RuntimeException("转化为List<T>出现错误", var5);
                }
            }
        }

        public List<Map<String, Object>> jsonToListMap(String jsonData) {
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            } else {
                JavaType javaType = JacksonUtil.mapper.getTypeFactory().constructParametricType(List.class, new Class[]{Map.class});

                try {
                    List<Map<String, Object>> list = (List)JacksonUtil.mapper.readValue(jsonData, javaType);
                    return list;
                } catch (Exception var4) {
                    throw new RuntimeException("转化为List<Map>出现错误", var4);
                }
            }
        }

        public JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
            return JacksonUtil.mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        }

        public JsonNode readTree(String content) {
            if (null == content) {
                return null;
            } else {
                try {
                    return JacksonUtil.mapper.readTree(content);
                } catch (IOException var3) {
                    throw new RuntimeException("Json字符串转换为树形结构出现错误", var3);
                }
            }
        }
    };

    private static final Logger log = LoggerFactory.getLogger(JacksonUtil.class);
    private static ObjectMapper mapper = new ObjectMapper();

    private JacksonUtil() {
    }

    public abstract String toJson(Object var1);

    public abstract <T> T jsonToBean(String var1, Class<T> var2);

    public abstract Map<String, Object> jsonToMap(String var1);

    public abstract <T> T mapToBean(Map<?, ?> var1, Class<T> var2);

    public abstract <T> List<T> jsonToList(String var1, Class<T> var2);

    public abstract List<Map<String, Object>> jsonToListMap(String var1);

    public abstract JsonNode readTree(String var1);
}
