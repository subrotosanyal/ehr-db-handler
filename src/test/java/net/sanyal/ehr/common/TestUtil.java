package net.sanyal.ehr.common;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sanyal.ehr.model.common.BaseEntity;

public class TestUtil {
    public static <T extends BaseEntity> List<T> getBaseEntityFromSearchJsonResponse(
            MvcResult searchResult, ObjectMapper objectMapper, Class<T> clazz)
            throws UnsupportedEncodingException, JsonProcessingException, JsonMappingException {
        
        String jsonResponse = searchResult.getResponse().getContentAsString();
        PageResponse<T> pageResponse = objectMapper.readValue(jsonResponse,
                objectMapper.getTypeFactory().constructParametricType(PageResponse.class, clazz));

        return pageResponse.getContent();
    }
}
