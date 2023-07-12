package com.xlhl.sky.controller.admin;

import com.xlhl.sky.constant.MessageConstant;
import com.xlhl.sky.result.Result;
import com.xlhl.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "通用接口")
public class CommonController {

    @Resource(name = "aliOssUtil")
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     *
     * @param file 需要上传的文件
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("文件开始上传......");
        if (file == null) {
            log.error("文件上传为空...");
            return Result.error("上传文件为空");
        }
        try {
            //原始文件名称
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.trim().equals("")) {
                log.error("文件名为空");
            }
            //获取文件后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;

            String fileURL = aliOssUtil.upload(file.getBytes(), fileName);

            return Result.success(fileURL);
        } catch (IOException e) {
            log.error("文件上传失败{}", e.getMessage());
            e.printStackTrace();
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
