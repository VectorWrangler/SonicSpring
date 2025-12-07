package cn.edu.hbnu.sonic.controller;

import cn.edu.hbnu.sonic.common.Result;
import cn.edu.hbnu.sonic.dto.SongDTO;
import cn.edu.hbnu.sonic.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "搜索模块", description = "音乐搜索相关接口")
@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @Operation(summary = "搜索音乐内容", description = "根据关键词搜索音乐、专辑、歌手、歌单或MV")
    @GetMapping
    public Result<Map<String, Object>> search(
            @Parameter(description = "搜索关键词，多个关键词以空格分隔") 
            @RequestParam String keywords,
            @Parameter(description = "返回数量，默认为30") 
            @RequestParam(defaultValue = "30") Integer limit,
            @Parameter(description = "偏移数量，用于分页，默认为0") 
            @RequestParam(defaultValue = "0") Integer offset,
            @Parameter(description = "搜索类型：1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单, 1002: MV，默认为1") 
            @RequestParam(defaultValue = "1") Integer type) {
        
        Map<String, Object> result = searchService.search(keywords, limit, offset, type);
        
        return Result.success(result);
    }
    
    @Operation(summary = "搜索单曲", description = "根据关键词搜索单曲，返回SongDTO列表")
    @GetMapping("/song")
    public Result<List<SongDTO>> searchSongs(
            @Parameter(description = "搜索关键词") 
            @RequestParam String keywords,
            @Parameter(description = "返回数量，默认为30") 
            @RequestParam(defaultValue = "30") Integer limit,
            @Parameter(description = "偏移数量，用于分页，默认为0") 
            @RequestParam(defaultValue = "0") Integer offset) {
        
        List<SongDTO> result = searchService.searchSongs(keywords, limit, offset);
        
        return Result.success(result);
    }
}