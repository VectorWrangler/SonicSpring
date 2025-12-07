package cn.edu.hbnu.sonic.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    /**
     * 上传头像文件到媒体服务器
     * @param file 上传的文件
     * @param userId 用户ID，用作文件名
     * @return 文件相对于媒体服务器的访问路径
     * @throws IOException 文件上传异常
     */
    String uploadAvatar(MultipartFile file, Long userId) throws IOException;
}