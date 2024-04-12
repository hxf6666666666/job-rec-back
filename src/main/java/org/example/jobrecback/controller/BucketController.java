package org.example.jobrecback.controller;

import com.google.gson.Gson;
import com.qingstor.sdk.config.EnvContext;
import com.qingstor.sdk.exception.QSException;
import com.qingstor.sdk.service.Bucket;
import com.qingstor.sdk.service.QingStor;
import com.qingstor.sdk.service.Types;
import jakarta.annotation.Resource;
import org.example.jobrecback.config.QingStorConfig;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/bucket")
public class BucketController {
    @Resource
    private QingStorConfig qingStorConfig;

    // 打印所有bucket
    @RequestMapping("/listBuckets")
    public void test() {
        EnvContext ctx = new EnvContext(qingStorConfig.getAccessKeyId(),qingStorConfig.getSecretAccessKey());
        try {
            QingStor stor = new QingStor(ctx);
            QingStor.ListBucketsOutput output = stor.listBuckets(null);
            if (output.getStatueCode() == 200) {
                System.out.println("Count = " + output.getCount());
                List<Types.BucketModel> buckets = output.getBuckets();
                System.out.println("buckets = " + new Gson().toJson(buckets));

            }
        } catch (QSException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/getPdf")
    public String getPdfBase64(@RequestParam String filePath) {
        EnvContext env = new EnvContext(qingStorConfig.getAccessKeyId(), qingStorConfig.getSecretAccessKey());
        String zoneKey = "pek3b";
        String bucketName = "hexinfeng";
        Bucket bucket = new Bucket(env, zoneKey, bucketName);
        Bucket.GetObjectInput input = new Bucket.GetObjectInput();
        Bucket.GetObjectOutput output = null;
        try {
            output = bucket.getObject(filePath, input);
            InputStream inputStream = output.getBodyInputStream();

            // 读取 InputStream 中的数据并写入 ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            // 将 ByteArrayOutputStream 中的数据转换为 byte 数组
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            // 对 byte 数组进行 Base64 编码
            return Base64.getEncoder().encodeToString(pdfBytes);
        } catch (QSException | IOException e) {
            throw new RuntimeException("Error retrieving PDF file: " + e.getMessage(), e);
        }
    }



}
