package com.tot.team4.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author liujh-r
 * @date 2023/8/31 9:28
 * @desc 文件实体类
 **/
@Data
@JsonIgnoreProperties
@JsonFormat
@AllArgsConstructor
@NoArgsConstructor
public class File implements Serializable {
    @TableId(value = "file_id")
    // 文件号
    private long fileId;
    // 用户号
    private long userId;
    // 文件真实路径+名
    private String filePath;
    // 文件类型
    private String fileType;
    // 上传文件名
    private String fileName;
    // 文件大小
    private String fileSize;
    // 文件创建时间
    private Timestamp createDate;

    public File(long userId, String filePath, String fileType, String fileName, String fileSize) {
        this.userId = userId;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
}
