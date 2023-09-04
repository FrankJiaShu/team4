package com.tot.team4.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: liujh-r
 * @Date: 2023/8/31 23:40
 * @Desc:
 **/

@Log4j2
@Component
public class OSSUtils {

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;


    /**
     * @author liujh-r
     * @date 2023/8/31 23:48
     * @desc 分片上传文件
     * @param fileKey 分片的key
     *        file 文件
     * @return 上传结果
     **/
    public Boolean uploadFileByPartETag(String fileKey, File file, OSS ossClient) {
        String uploadId = null;
        long start = System.currentTimeMillis();
        log.info("开始时间：{}", start);
        try {
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, fileKey);
            InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
            uploadId = upresult.getUploadId();
            List<PartETag> partETags = new ArrayList<>();
            // 计算文件有多少个分片 2MB
            final long partSize = 2 * 1024 * 1024L;
            final File sampleFile = new File(file.getAbsolutePath());
            long fileLength = sampleFile.length();
            int partCount = (int) (fileLength / partSize);
            if (fileLength % partSize != 0) {
                partCount++;
            }
            // 遍历分片上传
            for (int i = 0; i < partCount; i++) {
                int finalI = i;
                int finalPartCount = partCount;
                String finalUploadId = uploadId;
                // 开启线程池
                threadPoolExecutor.execute(() -> {
                    long startPos = finalI * partSize;
                    long curPartSize = (finalI + 1 == finalPartCount) ? (fileLength - startPos) : partSize;
                    InputStream instream;
                    try {
                        instream = Files.newInputStream(sampleFile.toPath());
                        // 跳过已经上传的分片
                        instream.skip(startPos);
                        UploadPartRequest uploadPartRequest = new UploadPartRequest();
                        uploadPartRequest.setBucketName(bucketName);
                        uploadPartRequest.setKey(fileKey);
                        uploadPartRequest.setUploadId(finalUploadId);
                        uploadPartRequest.setInputStream(instream);
                        uploadPartRequest.setPartSize(curPartSize);
                        uploadPartRequest.setPartNumber(finalI + 1);
                        UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                        partETags.add(uploadPartResult.getPartETag());
                    } catch (IOException e) {
                        log.error(e);
                    }
                });
            }
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, fileKey, uploadId, partETags);
            ossClient.completeMultipartUpload(completeMultipartUploadRequest);
            return true;
        } catch (Exception e) {
            // 取消分片上传，其中uploadId源自InitiateMultipartUpload
            AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(bucketName, fileKey, uploadId);
            ossClient.abortMultipartUpload(abortMultipartUploadRequest);
            log.error(e);
            return false;
        } finally {
            // 最终处理
            file.delete();
//            try {
//                if (ossClient != null) {
//                    ossClient.shutdown();
//                }
//            } catch (Exception e) {
//                log.error(e);
//            }
            long end = System.currentTimeMillis();
            log.info("结束时间：{}", end);
            log.info("分片上传执行时间：{}", end - start);
        }
    }
}
