package top.afan.encryption.common.model;

import java.util.List;

/**
 * 密匙文件实体类
 *
 * @author fan
 * @blame fan
 * @date 2019-10-31 15:39
 **/
public class KeyFileModel {

    private String fileType;

    private List<Integer> encodeLens;

    public KeyFileModel(List<Integer> encodeLens) {
        this.encodeLens = encodeLens;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public List<Integer> getEncodeLens() {
        return encodeLens;
    }

    public void setEncodeLens(List<Integer> encodeLens) {
        this.encodeLens = encodeLens;
    }

    @Override
    public String toString() {
        return "KeyFileModel{" +
                "fileType='" + fileType + '\'' +
                ", encodeLens=" + encodeLens +
                '}';
    }
}
