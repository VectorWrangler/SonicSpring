package cn.edu.hbnu.sonic.controller;

import cn.edu.hbnu.sonic.common.Result;
import cn.edu.hbnu.sonic.entity.*;
import cn.edu.hbnu.sonic.service.*;
import cn.edu.hbnu.sonic.service.admin.SongManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理员模块", description = "管理员后台管理接口")
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final SongService songService;
    private final ArtistService artistService;
    private final AlbumService albumService;
    private final MvService mvService;
    private final PlaylistService playlistService;
    private final SongManagerService songManagerService;

    public AdminController(UserService userService, SongService songService, 
                          ArtistService artistService, AlbumService albumService, 
                          MvService mvService, PlaylistService playlistService,
                          SongManagerService songManagerService) {
        this.userService = userService;
        this.songService = songService;
        this.artistService = artistService;
        this.albumService = albumService;
        this.mvService = mvService;
        this.playlistService = playlistService;
        this.songManagerService = songManagerService;
    }

    // 用户管理接口
    @Operation(summary = "获取所有用户", description = "管理员获取所有用户列表")
    @GetMapping("/users")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return Result.success(users);
    }

    @Operation(summary = "根据ID获取用户", description = "管理员根据用户ID获取用户详情")
    @GetMapping("/users/{id}")
    public Result<User> getUserById(
            @Parameter(description = "用户ID") 
            @PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.notFound("用户不存在");
        }
        return Result.success(user);
    }

    @Operation(summary = "删除用户", description = "管理员根据用户ID删除用户")
    @DeleteMapping("/users/{id}")
    public Result<String> deleteUser(
            @Parameter(description = "用户ID") 
            @PathVariable Long id) {
        boolean removed = userService.removeById(id);
        if (removed) {
            return Result.success("用户删除成功");
        } else {
            return Result.error("用户删除失败");
        }
    }

    @Operation(summary = "更新用户角色", description = "管理员更新用户角色")
    @PutMapping("/users/{id}/role")
    public Result<String> updateUserRole(
            @Parameter(description = "用户ID") 
            @PathVariable Long id,
            @Parameter(description = "用户角色") 
            @RequestParam String role) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.notFound("用户不存在");
        }
        
        user.setRole(role);
        userService.updateById(user);
        return Result.success("用户角色更新成功");
    }

    // 歌曲管理接口
    @Operation(summary = "获取所有歌曲", description = "管理员获取所有歌曲列表")
    @GetMapping("/songs")
    public Result<List<Song>> getAllSongs() {
        List<Song> songs = songService.list();
        return Result.success(songs);
    }

    @Operation(summary = "添加歌曲", description = "管理员添加新歌曲")
    @PostMapping("/songs")
    public Result<String> addSong(@RequestBody Song song) {
        songService.save(song);
        return Result.success("歌曲添加成功");
    }

    @Operation(summary = "更新歌曲", description = "管理员更新歌曲信息")
    @PutMapping("/songs/{id}")
    public Result<String> updateSong(
            @Parameter(description = "歌曲ID") 
            @PathVariable Long id,
            @RequestBody Song song) {
        song.setId(id);
        songService.updateById(song);
        return Result.success("歌曲更新成功");
    }

    @Operation(summary = "删除歌曲", description = "管理员根据歌曲ID删除歌曲及其关联的媒体资源")
    @DeleteMapping("/songs/{id}")
    public Result<String> deleteSong(
            @Parameter(description = "歌曲ID") 
            @PathVariable Long id) {
        boolean removed = songManagerService.deleteSongWithMedia(id);
        if (removed) {
            return Result.success("歌曲及媒体资源删除成功");
        } else {
            return Result.error("歌曲删除失败");
        }
    }

    // 艺术家管理接口
    @Operation(summary = "获取所有艺术家", description = "管理员获取所有艺术家列表")
    @GetMapping("/artists")
    public Result<List<Artist>> getAllArtists() {
        List<Artist> artists = artistService.list();
        return Result.success(artists);
    }

    @Operation(summary = "添加艺术家", description = "管理员添加新艺术家")
    @PostMapping("/artists")
    public Result<String> addArtist(@RequestBody Artist artist) {
        artistService.save(artist);
        return Result.success("艺术家添加成功");
    }

    @Operation(summary = "更新艺术家", description = "管理员更新艺术家信息")
    @PutMapping("/artists/{id}")
    public Result<String> updateArtist(
            @Parameter(description = "艺术家ID") 
            @PathVariable Long id,
            @RequestBody Artist artist) {
        artist.setId(id);
        artistService.updateById(artist);
        return Result.success("艺术家更新成功");
    }

    @Operation(summary = "删除艺术家", description = "管理员根据艺术家ID删除艺术家")
    @DeleteMapping("/artists/{id}")
    public Result<String> deleteArtist(
            @Parameter(description = "艺术家ID") 
            @PathVariable Long id) {
        boolean removed = artistService.removeById(id);
        if (removed) {
            return Result.success("艺术家删除成功");
        } else {
            return Result.error("艺术家删除失败");
        }
    }

    // 专辑管理接口
    @Operation(summary = "获取所有专辑", description = "管理员获取所有专辑列表")
    @GetMapping("/albums")
    public Result<List<Album>> getAllAlbums() {
        List<Album> albums = albumService.list();
        return Result.success(albums);
    }

    @Operation(summary = "添加专辑", description = "管理员添加新专辑")
    @PostMapping("/albums")
    public Result<String> addAlbum(@RequestBody Album album) {
        albumService.save(album);
        return Result.success("专辑添加成功");
    }

    @Operation(summary = "更新专辑", description = "管理员更新专辑信息")
    @PutMapping("/albums/{id}")
    public Result<String> updateAlbum(
            @Parameter(description = "专辑ID") 
            @PathVariable Long id,
            @RequestBody Album album) {
        album.setId(id);
        albumService.updateById(album);
        return Result.success("专辑更新成功");
    }

    @Operation(summary = "删除专辑", description = "管理员根据专辑ID删除专辑")
    @DeleteMapping("/albums/{id}")
    public Result<String> deleteAlbum(
            @Parameter(description = "专辑ID") 
            @PathVariable Long id) {
        boolean removed = albumService.removeById(id);
        if (removed) {
            return Result.success("专辑删除成功");
        } else {
            return Result.error("专辑删除失败");
        }
    }

    // MV管理接口
    @Operation(summary = "获取所有MV", description = "管理员获取所有MV列表")
    @GetMapping("/mvs")
    public Result<List<Mv>> getAllMvs() {
        List<Mv> mvs = mvService.list();
        return Result.success(mvs);
    }

    @Operation(summary = "添加MV", description = "管理员添加新MV")
    @PostMapping("/mvs")
    public Result<String> addMv(@RequestBody Mv mv) {
        mvService.save(mv);
        return Result.success("MV添加成功");
    }

    @Operation(summary = "更新MV", description = "管理员更新MV信息")
    @PutMapping("/mvs/{id}")
    public Result<String> updateMv(
            @Parameter(description = "MVID") 
            @PathVariable Long id,
            @RequestBody Mv mv) {
        mv.setId(id);
        mvService.updateById(mv);
        return Result.success("MV更新成功");
    }

    @Operation(summary = "删除MV", description = "管理员根据MVID删除MV")
    @DeleteMapping("/mvs/{id}")
    public Result<String> deleteMv(
            @Parameter(description = "MVID") 
            @PathVariable Long id) {
        boolean removed = mvService.removeById(id);
        if (removed) {
            return Result.success("MV删除成功");
        } else {
            return Result.error("MV删除失败");
        }
    }

    // 歌单管理接口
    @Operation(summary = "获取所有歌单", description = "管理员获取所有歌单列表")
    @GetMapping("/playlists")
    public Result<List<Playlist>> getAllPlaylists() {
        List<Playlist> playlists = playlistService.list();
        return Result.success(playlists);
    }

    @Operation(summary = "删除歌单", description = "管理员根据歌单ID删除歌单")
    @DeleteMapping("/playlists/{id}")
    public Result<String> deletePlaylist(
            @Parameter(description = "歌单ID") 
            @PathVariable Long id) {
        boolean removed = playlistService.removeById(id);
        if (removed) {
            return Result.success("歌单删除成功");
        } else {
            return Result.error("歌单删除失败");
        }
    }
}