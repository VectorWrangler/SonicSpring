package cn.edu.hbnu.sonic.service.impl;

import cn.edu.hbnu.sonic.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Value("${media.api}")
    private String mediaApiUrl;

    @Override
    public String uploadAvatar(MultipartFile file, Long userId) throws IOException {
        logger.info("开始上传头像文件，用户ID: {}", userId);
        
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new IOException("文件不能为空");
        }

        // 检查文件类型，只允许上传图片文件
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("只允许上传图片文件");
        }

        // 获取原始文件名和扩展名，用于保持文件格式
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            // 提取文件扩展名（包括点号）
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        // 使用用户ID作为文件名，拼接原始扩展名，实现文件重命名
        // 这样可以确保文件名唯一且保持原有文件格式
        String newFilename = userId + extension;
        
        logger.debug("原始文件名: {}, 新文件名: {}", originalFilename, newFilename);
        
        // 构造上传路径，只包含目录，不包含文件名
        // 媒体服务器会根据请求中的文件名参数来保存文件
        String uploadPath = "/user/avatar";
        
        // 构造媒体服务器上传URL，确保mediaApiUrl以斜杠结尾
        String apiUrl = mediaApiUrl.endsWith("/") ? mediaApiUrl : mediaApiUrl + "/";
        String uploadUrl = apiUrl + "upload?path=" + uploadPath;
        
        logger.info("向媒体服务器上传文件: 地址={}, 路径={}, 文件名={}, 文件大小={} bytes", 
                   mediaApiUrl, uploadPath, newFilename, file.getSize());
        logger.debug("完整上传URL: {}", uploadUrl);

        try {
            // 使用RestTemplate上传文件到媒体服务器
            RestTemplate restTemplate = new RestTemplate();
            
            // 设置请求头，使用新的文件名
            // 通过getMultiValueMapHttpEntity方法传递文件名参数
            HttpEntity<MultiValueMap<String, Object>> requestEntity = getMultiValueMapHttpEntity(file, newFilename);

            logger.debug("发送上传请求到媒体服务器...");
            // 发送POST请求上传文件
            ResponseEntity<String> response = restTemplate.exchange(
                uploadUrl, 
                HttpMethod.POST, 
                requestEntity, 
                String.class
            );
            
            logger.debug("收到媒体服务器响应，状态码: {}", response.getStatusCode());
            
            // 检查响应状态码是否为303（重定向）
            // 303状态码表示文件上传成功，Location头部包含文件访问URL
            if (response.getStatusCode().value() == 303) {
                // 从location头部获取文件访问URL
                String location = Objects.requireNonNull(response.getHeaders().getLocation()).toString();
                logger.info("文件上传成功，媒体服务器返回位置: {}", location);
                
                // 直接构造相对路径，避免解析URL带来的问题
                // 格式为: /user/avatar/{userId}.{extension}
                String relativePath = "/user/avatar/" + newFilename;
                logger.info("构造的相对路径: {}", relativePath);
                return relativePath;
            } else {
                logger.error("文件上传失败，媒体服务器返回状态码: {}", response.getStatusCode());
                throw new IOException("文件上传失败，状态码: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("文件上传异常: ", e);
            throw new IOException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 创建包含文件数据的HTTP请求实体
     * 
     * @param file MultipartFile对象，包含上传的文件数据
     * @param filename 文件名，用于在请求中指定保存的文件名
     * @return 包含文件数据和请求头的HttpEntity对象
     * @throws IOException 当读取文件数据失败时抛出
     */
    private static HttpEntity<MultiValueMap<String, Object>> getMultiValueMapHttpEntity(MultipartFile file, String filename) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");

        // 创建ByteArrayResource来包装文件内容
        // 重写getFilename方法以指定文件名
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        // 设置请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        return new HttpEntity<>(body, headers);
    }
}