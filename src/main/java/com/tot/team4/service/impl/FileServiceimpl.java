package com.tot.team4.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tot.team4.entity.File;
import com.tot.team4.mapper.FileMapper;
import com.tot.team4.service.FileService;
import com.tot.team4.util.LoginUserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author liujh-r
 * @date 2023/8/31 9:37
 * @desc 文件服务实现类
 * @param
 * @return
 **/
@Service("fileServiceImpl")
public class FileServiceimpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Resource
    private FileMapper fileMapper;

    @Override
    public List<File> getFilesByUserId(long userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    @Override
    public List<File> getAllFiles() {
        return fileMapper.getAllFiles();
    }

    @Override
    public List<File> getFilesByFileName(String fileName) {
        return fileMapper.getFilesByFileName(fileName);
    }

    @Override
    public File getFileByFileId(long fileId) {
        return fileMapper.getFilesByFileId(fileId);
    }


    @Override
    public IPage<File> selectPage(int current, int size) {
        Integer id = 2;
        LambdaQueryWrapper<File> fileLambdaQueryWrapper = Wrappers.lambdaQuery();
        fileLambdaQueryWrapper.eq(File::getUserId , id);
        Page<File> filePage = new Page<>(current, size);
        IPage<File> fileIPage = fileMapper.selectPage(filePage ,fileLambdaQueryWrapper);
        System.out.println("总页数： "+filePage.getPages());
        System.out.println("总记录数： "+filePage.getTotal());
        return fileIPage;
    }

    @Override
    public void insertFile(File file) {
        fileMapper.insert(file);
    }

    @Override
    public boolean deleteFile(MultipartFile file) {
        return fileMapper.deleteByUserIdAndFileName(LoginUserContextHolder.get().getId(), file.getOriginalFilename());
    }

    @Override
    public boolean deleteById(Long id) {
        return fileMapper.deleteById(id);
    }


    @Override
    public IPage<File> queryFiles(int current, int size, String fileName,String fileType) {
        long id = LoginUserContextHolder.get().getId();
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        int permission = LoginUserContextHolder.get().getPermissions();
        //超级管理员
        if (permission == 1){
            if (!fileName.equals("")) {
                //搜索查询
                queryWrapper.eq("file_name",fileName).eq("file_type",fileType);
            }else{
                queryWrapper.eq("file_type",fileType);
            }
        }
        //一般用户
        else{
            if (fileName.equals("")) {
                //无搜索，查所有
                queryWrapper.eq("user_id", id).eq("file_type",fileType);
            }else{
                //搜索查询
                queryWrapper.eq("user_id", id).eq("file_name",fileName).eq("file_type",fileType);
            }
        }

        Page<File> filePage = new Page<>(current, size);
        IPage<File> fileIPage = fileMapper.selectPage(filePage ,queryWrapper);
        System.out.println("总页数： "+filePage.getPages());
        System.out.println("总记录数： "+filePage.getTotal());
        return fileIPage;
    }

}
