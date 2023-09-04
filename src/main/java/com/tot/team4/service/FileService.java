package com.tot.team4.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tot.team4.entity.File;
import com.tot.team4.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * @author liujh-r
 * @date 2023/8/31 9:33
 * @desc 文件服务层
 **/
public interface FileService extends IService<File> {
    List<File> getFilesByUserId(long userId);

    List<File> getAllFiles();

    List<File> getFilesByFileName(String fileName);

    File getFileByFileId(long fileId);

    IPage<File> selectPage(int current, int size);

    boolean deleteFile(MultipartFile file);

    boolean deleteById(Long id);

    void insertFile(File file);

    IPage<File> queryFiles(int current, int size,String fileName,String fileType);
}