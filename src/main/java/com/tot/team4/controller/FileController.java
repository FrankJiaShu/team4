package com.tot.team4.controller;



import com.alibaba.fastjson.JSONObject;
import com.tot.team4.config.ErrorCodeConfig;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tot.team4.entity.File;
import com.tot.team4.entity.User;
import com.tot.team4.service.DiskService;
import com.tot.team4.service.FileService;
import com.tot.team4.util.LoginUserContextHolder;
import com.tot.team4.util.ResultObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;


/**
 * @author liujh-r
 * @date 2023/8/30 9:03
 * @desc 文件上传下载控制器
 **/
@RestController
@RequestMapping("/files")
@Tag(name="文件处理器")
@Log4j2
public class FileController {


    @Autowired
    @Qualifier("diskServiceImpl")
    private DiskService diskService;

    @Autowired
    @Qualifier("fileServiceImpl")
    private FileService fileService;

    @Autowired
    private ErrorCodeConfig errorCode;

    /**
     * @desc 文件上传 post方法
     * @param file 文件
     *        fileType 文件类型
     * @return 上传的结果
     **/
    @PostMapping("/upload")
    @Operation(description="文件上传")
    public void uploadFileAsync(@RequestParam MultipartFile file, String fileType, HttpServletResponse response) throws IOException {
        String[] arr = file.getOriginalFilename().split("\\.");
        Long userid = LoginUserContextHolder.get().getId();
        String filePath = fileType + "/" + userid + UUID.randomUUID().toString().replace("-", "").toLowerCase() + "."+ arr[1];
        log.info("filepath: {}", filePath);
        // 插入文件表
        BigDecimal number = new BigDecimal(file.getSize() / 1024.0 / 1024.0);
        BigDecimal size = number.setScale(2, RoundingMode.HALF_UP);
        log.info("file size: {}", size);
        fileService.insertFile(new File(userid, filePath, fileType, file.getOriginalFilename(), size+""));
        log.info("插入文件表执行成功");
        // 调用服务层
        diskService.uploadFile(file, filePath);
       log.info("调用服务层执行成功");
        ResultObject<Object> resultObject =  ResultObject.success(null);
        response.getWriter().write(JSONObject.toJSONString(resultObject));
        response.getWriter().close();
    }

    /**
     * @author liujh-r
     * @date 2023/8/30 9:04
     * @desc 文件分片上传 post方法
     * @param file 文件
     *        fileType 文件类型
     * @return 分片上传的结果
     **/
    @PostMapping("/uploadByPartETag")
    @Operation(description="文件分片上传")
    public void uploadByPartETag(@RequestParam MultipartFile file, String fileType, HttpServletResponse response) throws IOException {
        String[] arr = file.getOriginalFilename().split("\\.");
        Long userid = LoginUserContextHolder.get().getId();
        String filePath = fileType + "/" + userid + UUID.randomUUID().toString().replace("-", "").toLowerCase() + "."+ arr[1];
        log.info("filepath: {}", filePath);
        // 插入文件表
        BigDecimal number = new BigDecimal(file.getSize() / 1024.0 / 1024.0);
        BigDecimal size = number.setScale(2, RoundingMode.HALF_UP);
        log.info("file size: {}", size);
        fileService.insertFile(new File(userid, filePath, fileType, file.getOriginalFilename(), size+""));
        // 文件类型转换
        InputStream inputStream = file.getInputStream();
        java.io.File file2 = new java.io.File(filePath);
        FileUtils.copyInputStreamToFile(inputStream, file2);
        // 调用服务层
        diskService.uploadByPartETag(filePath, file2);
        ResultObject<Object> resultObject =  ResultObject.success(null);
        response.getWriter().write(JSONObject.toJSONString(resultObject));
        response.getWriter().close();
    }

    /**
     * @date 2023/8/30 9:04
     * @desc 文件下载
     * @param fileId 文件号
     **/
    @GetMapping("/download")
    @Operation(description="文件下载")
    public ResponseEntity<InputStreamResource> download(String fileId) throws IOException {
        // 查表
        File file = fileService.getFileByFileId(Long.parseLong(fileId));
        log.info("test:{}", file.getFilePath());
        // 调用服务层
        InputStream inputStream = diskService.downloadFile(file.getFilePath());

        // 组装返回的流前端展示
        if (inputStream != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + file.getFileName());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(inputStream));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * @author liujh-r
     * @date 2023/8/31 9:04
     * @desc 根据用户名查询所有的文件
     * @return permissions = 0 -> 用户名下所有的文件列表
               permissions = 1 -> 超级用户所有的文件列表
     **/
    @GetMapping("/findByUserId")
    @Operation(description="根据用户id查询文件")
    public ResultObject<List<File>> getFilesByUserId() {
        // 调用服务层
        User user = LoginUserContextHolder.get();
        int permissions = user.getPermissions();
        if (permissions == 0) { // 查询自己的文件
            List<File> files = fileService.getFilesByUserId(user.getId());
            log.info("查询到用户文件信息：{}", files);
            if (files.isEmpty()) {
                int code = errorCode.getCode().get("notfound");
                String msg = errorCode.getMsg().get("notfound");
                return ResultObject.error(msg, code);
            } else {
                return ResultObject.success(files);
            }
        } else { // 查询所有的文件
            List<File> files = fileService.getAllFiles();
            log.info("查询到所有文件信息：{}", files);
            if (files.isEmpty()) {
                int code = errorCode.getCode().get("notfound");
                String msg = errorCode.getMsg().get("notfound");
                return ResultObject.error(msg, code);
            } else {
                return ResultObject.success(files);
            }
        }
    }

    /**
     * @author liujh-r
     * @date 2023/8/31 9:04
     * @desc 根据文件名查询相关的文件
     * @return 所有匹配的文件列表
     **/
    @GetMapping("/findByFileName")
    @Operation(description="根据文件名文件查询")
    public ResultObject<List<File>> getFilesByFileName(String fileName) {
        // 调用服务层查询
        List<File> files = fileService.getFilesByFileName(fileName);
        log.info("查询到用户文件信息：{}", files);
        if (files.isEmpty()) {
            int code = errorCode.getCode().get("notfound");
            String msg = errorCode.getMsg().get("notfound");
            return ResultObject.error(msg, code);
        } else {
            return ResultObject.success(files);
        }
    }

    /**
     *
     * 删除文件
     * @author scb
     * @param fileId 文件id
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @GetMapping("/delete")
    public ResultObject<Object> deleteFile(Long fileId) throws Exception {
        File filesByFileId = fileService.getFileByFileId(fileId);
        if (filesByFileId == null){
            int code = errorCode.getCode().get("notfound");
            String msg = errorCode.getMsg().get("notfound");
            return ResultObject.error(msg, code);
        }
        String filePath = filesByFileId.getFilePath();

        /*// 异步化
        CompletableFuture.supplyAsync(() -> {
            // 删除本地实体文件
            diskService.deleteFile(filePath);
            return "删除本地实体文件执行成功";
        }, forkJoinPool);
        CompletableFuture.supplyAsync(() -> {
            // 删除文件表中的数据
            fileService.deleteById(fileId);
            return "删除文件表中数据执行成功";
        }, forkJoinPool);*/

        diskService.deleteFile(filePath);
        fileService.deleteById(fileId);
        return ResultObject.success("删除成功");
    }

    /**
     * @author yzb
     * @date 2023/8/31
     * @desc 查询（改）
     * @param
     * @return
     **/
    @GetMapping("/querryFiles")
    public ResultObject<IPage<File>> querryFiles(int current, int size,String fileName,String fileType){
        ResultObject resultObject = new ResultObject();
        resultObject.setData(fileService.queryFiles(current,size,fileName,fileType));
        return resultObject;
    }
}


