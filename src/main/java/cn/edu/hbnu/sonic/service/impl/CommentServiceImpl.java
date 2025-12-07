package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.entity.Comment;
import cn.edu.hbnu.sonic.mapper.CommentMapper;
import cn.edu.hbnu.sonic.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}