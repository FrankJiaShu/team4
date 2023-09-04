package com.tot.team4.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.tot.team4.consts.FileCategoryConst;
import com.tot.team4.service.DiskService;
import com.tot.team4.util.OSSUtils;
import com.tot.team4.util.ResultObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author liujh-r
 * @date 2023/8/30 9:09
 * @desc OSS服务层实现类
 **/
@Service("diskServiceImpl")
@Log4j2
public class DiskServiceImpl implements DiskService {

    // 配置存储位置
    @Value("${storage.switch}")
    private String storgeLocation;

    @Value("${local.prefix}")
    private String prefix;

    @Autowired
    private OSS ossClient;

    @Autowired
    private OSSUtils ossUtils;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * @desc 文件上传服务
     * @param file 文件
     * @param filePath 文件
     * @return 文件上传的结果
     **/
    @Override
    public ResultObject<String> uploadFile(MultipartFile file, String filePath) {
        // storgeLocation = "oss";
        if (!file.isEmpty()) {
            try {
                if("file".equalsIgnoreCase(storgeLocation)){
                    File prefixPath = new File(prefix);
                    if(!prefixPath.exists()){
                        new File(prefix, FileCategoryConst.FILE_IMG).mkdirs();
                        new File(prefix,FileCategoryConst.FILE_VIDEO).mkdirs();
                        new File(prefix,FileCategoryConst.FILE_AUDIO).mkdirs();
                        new File(prefix,FileCategoryConst.FILE_DOC).mkdirs();
                        new File(prefix,FileCategoryConst.FILE_OTHER).mkdirs();
                    }
                    String path = prefix + filePath;
                    log.info("本地文件上传路径：{}", path);
                    // 创建目标文件对象
                    File destinationFile = new File(path);
                    // 将上传的文件写入目标文件
                    file.transferTo(destinationFile);
                }
                if("oss".equalsIgnoreCase(storgeLocation)){
                    log.info("远程文件上传路径：{}", filePath);
                    ossClient.putObject(bucketName, filePath, file.getInputStream());
                }

                return ResultObject.success("文件上传成功！");
            } catch (IOException e) {
                log.error(e);
                log.error("文件上传失败！");
            }
        } else {
            return ResultObject.error("上传的文件为空！", 103);
        }
        return ResultObject.error("文件上传失败！", 102);
    }

    /**
     * @desc 文件上传服务
     * @param file 文件
     * @param fileKey 文件key
     * @param file 文件
     * @return 文件上传的结果
     **/
    @Override
    public ResultObject<String> uploadByPartETag(String fileKey, File file) {
        log.info("开始分片上传文件...");
        if("oss".equalsIgnoreCase(storgeLocation)) {
            try {
                if (ossUtils.uploadFileByPartETag(fileKey, file, ossClient)) {
                    log.info("上传文件成功");
                    return ResultObject.success("文件上传成功！");
                } else {
                    log.info("上传文件失败");
                }
                return ResultObject.error("文件上传失败！", 102);
            } catch (Exception e) {
                log.error("上传文件失败------>" + e.getMessage());
            }
        }
        return ResultObject.error("文件上传失败！", 102);
    }

    /**
     * @param filePath 文件路径
     * @return 文件下载的流
     * @desc 文件下载
     **/
    @Override
    public InputStream downloadFile(String filePath) {
        InputStream inputStream = null;
        // file
        if("file".equalsIgnoreCase(storgeLocation)){
            filePath = prefix + filePath;
            log.info("本地文件下载路径：{}", filePath);
            File file = new File(filePath);
            // 检查文件是否存在
            if (file.exists()) {
                try {
                    // 创建文件输入流
                    inputStream = Files.newInputStream(file.toPath());
                } catch (Exception e) {
                    log.error("本地文件下载失败:"+e);
                }
            }
        }
        // oss
        if("oss".equalsIgnoreCase(storgeLocation)){
            try {
                log.info("远程文件下载路径：{}", filePath);
                if (!ossClient.doesObjectExist(bucketName, filePath)) {
                    return null;
                }
                OSSObject object = ossClient.getObject(bucketName, filePath);
                inputStream = object.getObjectContent();
            } catch (Exception e) {
                log.error("远程文件下载失败:"+e);
            }
        }
        return inputStream;
    }

    /**
     * 文件删除
     * @param filePath
     * @return
     */
    @Override
    public boolean deleteFile(String filePath) throws Exception {
        // file
        if("file".equalsIgnoreCase(storgeLocation)){
            filePath = prefix + filePath;
            log.info("本地文件删除路径：{}", filePath);
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    // 文件删除
                    return file.delete();
                } catch (Exception e) {
                    log.error("本地文件删除失败！");
                    throw new Exception(e);
                }
            }
        }
        // oss
        if("oss".equalsIgnoreCase(storgeLocation)){
            try {
                log.info("远程文件删除路径：{}", filePath);
                ossClient.deleteObject(bucketName,filePath);
                return true;
            } catch (Exception | Error e) {
                log.error("远程文件删除失败！");
                throw new Exception(e);
            }
        }

        return false;
    }



}

