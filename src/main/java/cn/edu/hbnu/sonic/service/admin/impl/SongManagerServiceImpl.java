package cn.edu.hbnu.sonic.service.admin.impl;

import cn.edu.hbnu.sonic.entity.Song;
import cn.edu.hbnu.sonic.service.SongService;
import cn.edu.hbnu.sonic.service.admin.SongManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class SongManagerServiceImpl implements SongManagerService {

    @Autowired
    private SongService songService;

    @Value("${media.api}")
    private String mediaApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean deleteSongWithMedia(Long songId) {
        // 首先获取歌曲信息
        Song song = songService.getById(songId);
        if (song == null) {
            log.warn("Song not found with id: {}", songId);
            return false;
        }

        // 标记媒体文件是否删除成功
        boolean mediaDeleted = false;
        
        // 构造媒体服务器删除URL
        String mediaPath = song.getUrl();
        if (mediaPath != null && mediaPath.startsWith("/")) {
            String deleteUrl = mediaApiUrl + "rm?path=" + mediaPath;
            
            try {
                log.info("Attempting to delete media file: {}", mediaPath);
                // 向媒体服务器发送删除请求
                ResponseEntity<String> response = restTemplate.postForEntity(deleteUrl, null, String.class);
                
                // 必须检查响应状态码是否为303才认为删除成功
                if (response.getStatusCode() == HttpStatus.SEE_OTHER) {
                    log.info("Media file deleted successfully: {}", mediaPath);
                    mediaDeleted = true;
                } else {
                    log.error("Failed to delete media file. Status code: {}, Path: {}", response.getStatusCode(), mediaPath);
                }
            } catch (RestClientException e) {
                // 如果媒体服务器删除失败，记录日志
                log.error("Failed to delete media file: {}, Error: {}", mediaPath, e.getMessage());
            }
        } else {
            // 如果没有有效的媒体路径，认为媒体文件删除操作成功（因为没有需要删除的文件）
            log.info("No valid media path found for song ID: {}, skipping media deletion", songId);
            mediaDeleted = true;
        }

        // 只有当媒体文件删除成功时，才删除数据库中的歌曲记录
        if (mediaDeleted) {
            boolean dbDeleted = songService.removeById(songId);
            if (dbDeleted) {
                log.info("Song deleted successfully from database, ID: {}", songId);
            } else {
                log.error("Failed to delete song from database, ID: {}", songId);
            }
            return dbDeleted;
        } else {
            // 如果媒体文件删除失败，不删除数据库记录
            log.warn("Skipping database deletion due to media deletion failure, Song ID: {}", songId);
            return false;
        }
    }
}