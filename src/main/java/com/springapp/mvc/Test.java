package com.springapp.mvc;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.spring.RemovableBeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author june
 *         2015年09月30日 13:53
 */
@Service //找不到这个bean
public class Test {

    @DataProvider
    public void query(Page<Map<String,Object>> page, Criteria criteria) {
        StringBuilder sb = new StringBuilder();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name","june");
        map.put("age","20");
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);

        page.setEntities(list);
        page.setEntityCount(4);
    }
}
