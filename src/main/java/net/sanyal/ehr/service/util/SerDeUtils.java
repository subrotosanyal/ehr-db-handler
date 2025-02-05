package net.sanyal.ehr.service.util;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SerDeUtils {
    public static void applyUpdates(ObjectMapper objectMapper, Object targetObject, Map<String, Object> updates) {
        if (updates == null) {
            throw new IllegalArgumentException("Updates map cannot be null.");
        }

        BeanWrapper wrapper = new BeanWrapperImpl(targetObject);

        updates.forEach((key, value) -> {
            PropertyDescriptor pd = wrapper.getPropertyDescriptor(key);
            Class<?> propertyType = pd.getPropertyType();

            if (isSimpleType(propertyType)) {
                wrapper.setPropertyValue(key, value);
            } else if (propertyType == java.time.LocalDate.class) {
                wrapper.setPropertyValue(key, objectMapper.convertValue(value, java.time.LocalDate.class));
            } else if (propertyType == java.time.LocalDateTime.class) {
                wrapper.setPropertyValue(key, objectMapper.convertValue(value, java.time.LocalDateTime.class));
            } else if (propertyType == List.class) {
                handleListUpdates(objectMapper, wrapper, key, value);
            } else {
                Object nestedObject = wrapper.getPropertyValue(key);
                if (nestedObject == null) {
                    try {
                        nestedObject = propertyType.getDeclaredConstructor().newInstance();
                        wrapper.setPropertyValue(key, nestedObject);
                    } catch (Exception e) {
                        throw new RuntimeException("Unable to create instance for nested object: " + key, e);
                    }
                }
                if (null != value) {
                    applyUpdates(objectMapper, nestedObject, objectMapper.convertValue(value, Map.class));
                }
            }
        });
    }

    private static void handleListUpdates(ObjectMapper objectMapper, BeanWrapper wrapper, String key, Object value) {
        PropertyDescriptor pd = wrapper.getPropertyDescriptor(key);
        Class<?> listElementType = getGenericListType(pd);

        if (listElementType == null) {
            throw new IllegalArgumentException("Unable to determine list element type for property: " + key);
        }

        List<?> newList = objectMapper.convertValue(value,
                objectMapper.getTypeFactory().constructCollectionType(List.class, listElementType));

        List<Object> existingList = (List<Object>) wrapper.getPropertyValue(key);
        if (existingList != null) {
            if (!(existingList instanceof ArrayList)) {
                existingList = new ArrayList<>(existingList);
            }
            existingList.clear();
            existingList.addAll(newList);
            wrapper.setPropertyValue(key, existingList);
        } else {
            wrapper.setPropertyValue(key, newList);
        }
    }

    private static Class<?> getGenericListType(PropertyDescriptor pd) {
        try {
            java.lang.reflect.Method readMethod = pd.getReadMethod();
            java.lang.reflect.Type returnType = readMethod.getGenericReturnType();

            if (returnType instanceof java.lang.reflect.ParameterizedType) {
                java.lang.reflect.ParameterizedType type = (java.lang.reflect.ParameterizedType) returnType;
                return (Class<?>) type.getActualTypeArguments()[0];
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to determine list generic type for property: " + pd.getName(), e);
        }
        return null;
    }

    private static boolean isSimpleType(Class<?> type) {
        return type.isPrimitive() || type == String.class || Number.class.isAssignableFrom(type) ||
                type == Boolean.class || type == java.util.Date.class || type == LocalDateTime.class;
    }
}
