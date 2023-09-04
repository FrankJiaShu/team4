package com.tot.team4.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.tot.team4.service.OSSService;
import com.tot.team4.util.ResultObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author liujh-r
 * @date 2023/8/30 9:09
 * @desc OSS服务层实现类
 **/
@Service
@Log4j2
public class OSSServiceImpl implements OSSService {

    @Autowired
    private OSS ossClient;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * @author liujh-r
     * @date 2023/8/30 9:11
     * @desc 文件上传服务
     * @param file 文件
     * @param filePath 文件
     * @return 文件上传的结果
     **/
    @Override
    public ResultObject<String> uploadFile(MultipartFile file, String filePath) {
        String objectName;
        try {
            log.info("文件上传路径：{}", filePath);
            ossClient.putObject(bucketName, filePath, file.getInputStream());
            return ResultObject.success("文件上传成功！");
        } catch (Exception | Error e) {
            log.error("文件上传失败！");
        }
        return ResultObject.error("文件上传失败！", 102);
    }

    /**
     * @param fileName 文件名
     * @return 文件下载的流
     * @author liujh-r
     * @date 2023/8/30 9:04
     * @desc 文件下载
     **/
    @Override
    public InputStream downloadFile(String fileName) {
        try {
            log.info("文件下载路径：{}", fileName);
            if (!ossClient.doesObjectExist(bucketName, fileName)) {
                return null;
            }
            OSSObject object = ossClient.getObject(bucketName, fileName);
            return object.getObjectContent();
        } catch (Exception | Error e) {
            log.error("文件下载失败！");
        }
        return null;
    }

    @Override
    public boolean deleteFile(String filePath) {
        try {
            log.info("文件上传路径：{}", filePath);
            ossClient.deleteObject(bucketName,filePath);
            return true;
        } catch (Exception | Error e) {
            log.error("文件上传失败！");
        }
        return false;
    }



}

