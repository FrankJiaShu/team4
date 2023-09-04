package com.tot.team4.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tot.team4.entity.File;
import com.tot.team4.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujh-r
 * @date 2023/8/31 9:39
 * @desc 文件dao层
 **/
@Mapper
public interface FileMapper extends BaseMapper<File> {
    List<File> getFilesByUserId(long userId);

    List<File> getFilesByFileName(String fileName);

    File getFilesByFileId(long fileId);

    List<File> getAllFiles();

    boolean deleteByUserIdAndFileName(Long userId, String fileName);

    boolean deleteById(Long id);
}