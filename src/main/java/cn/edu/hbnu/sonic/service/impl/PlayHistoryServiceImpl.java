package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.entity.PlayHistory;
import cn.edu.hbnu.sonic.mapper.PlayHistoryMapper;
import cn.edu.hbnu.sonic.service.PlayHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PlayHistoryServiceImpl extends ServiceImpl<PlayHistoryMapper, PlayHistory> implements PlayHistoryService {
}