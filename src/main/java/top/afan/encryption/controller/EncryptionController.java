package top.afan.encryption.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.afan.encryption.common.dto.AjaxResult;
import top.afan.encryption.common.dto.EncryptionDTO;
import top.afan.encryption.service.EncryptionService;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author fan
 * @blame fan
 * @date 2019-10-31 10:16
 **/
@RestController
@RequestMapping("/fan")
@Api(description = "文件的加密与解密服务")
public class EncryptionController {

    private Logger logger = LoggerFactory.getLogger(EncryptionController.class);

    private EncryptionService encryptionService;

    @Autowired
    public EncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @ApiOperation(value = "加密文件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "filePath", value = "示例：C:/xx/xx.xx", required = true)
    })
    @PostMapping(value = "/encryptedFile")
    public Object encryptedFile(@RequestParam String filePath) throws IOException {
        boolean result = encryptionService.encryptedFile(filePath);
        if(result){
            logger.info("加密文件：" + filePath);
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @ApiOperation(value = "解密文件")
    @PostMapping(value = "/decryptFile")
    public Object decryptFile(@Validated EncryptionDTO encryptionDTO) throws IOException {
        boolean result = encryptionService.decryptFile(encryptionDTO.getFilePath(), encryptionDTO.getKeyPath());
        if(result){
            logger.info("解密文件：" + encryptionDTO.getFilePath());
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }
}
