package zcc.es.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zcc.es.Bean.FieldMapping;
import zcc.es.Bean.FieldType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FieldMappingUtils {
    private static final Logger log = LoggerFactory.getLogger(FieldMappingUtils.class);

    public FieldMappingUtils() {
    }

    public static List<FieldMapping> getFieldInfo(Class clazz) {
        return getFieldInfo(clazz, (String)null);
    }

    public static List<FieldMapping> getFieldInfo(Class clazz, String fieldName) {
        Field[] fields = clazz.getDeclaredFields();
        List<FieldMapping> fieldMappingList = new ArrayList();
        Field[] var4 = fields;
        int var5 = fields.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            FieldType fieldInfo = (FieldType)field.getAnnotation(FieldType.class);
            if (fieldInfo != null) {
                if (!"object".equals(fieldInfo.type()) && !"nested".equals(fieldInfo.type())) {
                    String name = field.getName();
                    fieldMappingList.add(new FieldMapping(name, fieldInfo.type(), fieldInfo.analyzer(), fieldInfo.searchAnalyze(), fieldInfo.store(), fieldInfo.format()));
                } else {
                    Class fc = field.getType();
                    if (fc.isPrimitive()) {
                        String name = field.getName();
                        if (StringUtils.isNotBlank(fieldName)) {
                            name = name + "." + fieldName;
                        }

                        fieldMappingList.add(new FieldMapping(name, fieldInfo.type(), fieldInfo.analyzer(), fieldInfo.searchAnalyze(), fieldInfo.store(), fieldInfo.format()));
                    } else if (fc.isAssignableFrom(List.class)) {
                        log.debug("List类型：{}", field.getName());
                        Type gt = field.getGenericType();
                        ParameterizedType pt = (ParameterizedType)gt;
                        Class listClass = (Class)pt.getActualTypeArguments()[0];
                        List<FieldMapping> fieldMappings = getFieldInfo(listClass, field.getName());
                        FieldMapping fieldMapping = new FieldMapping(field.getName(), fieldInfo.type(), fieldInfo.analyzer(), fieldInfo.searchAnalyze(), fieldInfo.store(), fieldInfo.format());
                        fieldMapping.setFieldMappingList(fieldMappings);
                        fieldMappingList.add(fieldMapping);
                    } else {
                        List<FieldMapping> fieldMappings = getFieldInfo(fc, field.getName());
                        FieldMapping fieldMapping = new FieldMapping(field.getName(), fieldInfo.type(), fieldInfo.analyzer(), fieldInfo.searchAnalyze(), fieldInfo.store(), fieldInfo.format());
                        fieldMapping.setFieldMappingList(fieldMappings);
                        fieldMappingList.add(fieldMapping);
                    }
                }
            }
        }

        return fieldMappingList;
    }
}
