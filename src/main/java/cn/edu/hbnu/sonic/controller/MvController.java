package cn.edu.hbnu.sonic.controller;

import cn.edu.hbnu.sonic.entity.Mv;
import cn.edu.hbnu.sonic.service.MvService;
import cn.edu.hbnu.sonic.common.MediaUrlUtils;
import cn.edu.hbnu.sonic.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MV模块", description = "MV相关接口")
@RestController
@RequestMapping("/mv")
public class MvController {

    private final MvService mvService;
    
    public MvController(MvService mvService) {
        this.mvService = mvService;
    }
    
    @Operation(summary = "获取所有MV", description = "获取所有MV列表")
    @GetMapping("/all")
    public Result<List<Mv>> getAllMvs() {
        List<Mv> mvs = mvService.list();
        // 为每个MV的videoUrl添加媒体服务器地址前缀
        List<Mv> processedMvs = MediaUrlUtils.getInstance().processMvListUrls(mvs);
        return Result.success(processedMvs);
    }
    
    @Operation(summary = "根据ID获取MV", description = "根据MVID获取MV详情")
    @GetMapping("/url")
    public Result<Mv> getMvById(
            @Parameter(description = "MV ID") 
            @RequestParam Long id) {
        Mv mv = mvService.getById(id);
        if (mv == null) {
            return Result.notFound("MV不存在");
        }
        // 为MV的videoUrl添加媒体服务器地址前缀
        Mv processedMv = MediaUrlUtils.getInstance().processMvUrls(mv);
        return Result.success(processedMv);
    }
}