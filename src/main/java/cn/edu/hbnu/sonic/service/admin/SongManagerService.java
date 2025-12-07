package cn.edu.hbnu.sonic.service.admin;

public interface SongManagerService {
    /**
     * 删除歌曲及其关联的媒体资源
     * @param songId 歌曲ID
     * @return 删除是否成功
     */
    boolean deleteSongWithMedia(Long songId);
}