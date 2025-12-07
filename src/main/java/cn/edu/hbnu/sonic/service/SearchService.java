package cn.edu.hbnu.sonic.service;

import cn.edu.hbnu.sonic.entity.*;
import java.util.Map;

public interface SearchService {
    
    /**
     * 搜索接口
     * @param keywords 搜索关键词，多个关键词以空格分隔
     * @param limit 返回数量，默认为30
     * @param offset 偏移数量，用于分页，默认为0
     * @param type 搜索类型：1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单, 1002: MV，默认为1
     * @return 搜索结果
     */
    Map<String, Object> search(String keywords, Integer limit, Integer offset, Integer type);
}