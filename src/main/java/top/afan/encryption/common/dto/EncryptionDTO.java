package top.afan.encryption.common.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author fan
 * @blame fan
 * @date 2019-10-31 17:00
 **/
@ApiModel(description = "解压参数")
public class EncryptionDTO {

    @NotNull
    @ApiModelProperty(required = true, value = "加密后的文件路径 示例：C:/xx/xx.fan")
    private String filePath;

    @NotNull
    @ApiModelProperty(required = true, value = "密匙路径 示例：C:/xx/xx.key")
    private String keyPath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    @Override
    public String toString() {
        return "EncryptionDTO{" +
                "filePath='" + filePath + '\'' +
                ", keyPath='" + keyPath + '\'' +
                '}';
    }
}
