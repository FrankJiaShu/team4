package com.tot.team4.service;

import com.tot.team4.util.ResultObject;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

/**
 * @author liujh-r
 * @date 2023/8/30 11:43
 * @desc OSS服务层
 **/
public interface OSSService {

    ResultObject<String> uploadFile(MultipartFile file, String filePath);

    InputStream downloadFile(String fileName);

     boolean deleteFile(String filePath);

}