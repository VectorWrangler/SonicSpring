package cn.edu.hbnu.sonic.dto;

import lombok.Data;

/**
 * ID和名称DTO
 * 用于表示具有ID和名称属性的简单数据传输对象
 * 常用于表示歌手、专辑等实体的基本信息
 */
@Data
public class IdNameDTO {
    private Long id;
    private String name;
}