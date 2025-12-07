package cn.edu.hbnu.sonic.common;

import cn.edu.hbnu.sonic.entity.Song;
import cn.edu.hbnu.sonic.entity.Mv;
import cn.edu.hbnu.sonic.entity.User;
import cn.edu.hbnu.sonic.config.MediaApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MediaUrlUtils {
    
    @Autowired
    private MediaApiConfig mediaApiConfig;
    
    // 使用volatile确保多线程环境下的可见性
    private static volatile MediaUrlUtils instance;
    
    // 私有构造函数，防止外部实例化
    private MediaUrlUtils() {}
    
    // 静态工厂方法，提供全局访问点
    public static MediaUrlUtils getInstance() {
        if (instance == null) {
            synchronized (MediaUrlUtils.class) {
                if (instance == null) {
                    instance = new MediaUrlUtils();
                }
            }
        }
        return instance;
    }
    
    // 初始化方法，由Spring调用
    @PostConstruct
    public void init() {
        instance = this;
    }
    
    /**
     * 为路径添加媒体服务器地址前缀
     * @param path 原始路径
     * @return 添加前缀后的完整路径
     */
    private String addMediaServerPrefix(String path) {
        if (path == null || path.startsWith("http")) {
            return path;
        }
        
        String mediaApiUrl = mediaApiConfig.getApi();
        return mediaApiUrl + path;
    }
    
    /**
     * 为用户头像添加媒体服务器地址前缀
     * @param user 用户对象
     * @return 处理后的用户对象
     */
    public User processUserAvatarUrl(User user) {
        if (user == null) {
            return null;
        }
        
        user.setAvatar(addMediaServerPrefix(user.getAvatar()));
        return user;
    }
    
    /**
     * 为歌曲的url添加媒体服务器地址前缀
     * @param song 歌曲对象
     * @return 处理后的歌曲对象
     */
    public Song processSongUrls(Song song) {
        if (song == null) {
            return null;
        }
        
        song.setUrl(addMediaServerPrefix(song.getUrl()));
        return song;
    }
    
    /**
     * 为歌曲列表中的每个歌曲的url添加媒体服务器地址前缀
     * @param songs 歌曲列表
     * @return 处理后的歌曲列表
     */
    public List<Song> processSongListUrls(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            return songs;
        }
        
        return songs.stream()
                .map(this::processSongUrls)
                .collect(Collectors.toList());
    }
    
    /**
     * 为MV的videoUrl和cover添加媒体服务器地址前缀
     * @param mv MV对象
     * @return 处理后的MV对象
     */
    public Mv processMvUrls(Mv mv) {
        if (mv == null) {
            return null;
        }
        
        mv.setVideoUrl(addMediaServerPrefix(mv.getVideoUrl()));
        mv.setCover(addMediaServerPrefix(mv.getCover()));
        return mv;
    }
    
    /**
     * 为MV列表中的每个MV的videoUrl和cover添加媒体服务器地址前缀
     * @param mvs MV列表
     * @return 处理后的MV列表
     */
    public List<Mv> processMvListUrls(List<Mv> mvs) {
        if (mvs == null || mvs.isEmpty()) {
            return mvs;
        }
        
        return mvs.stream()
                .map(this::processMvUrls)
                .collect(Collectors.toList());
    }
    
    /**
     * 为歌词添加媒体服务器地址前缀
     * @param lyric 歌词内容
     * @return 处理后的歌词内容
     */
    public String processLyric(String lyric) {
        return addMediaServerPrefix(lyric);
    }
}