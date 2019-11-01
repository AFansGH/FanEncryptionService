package top.afan.encryption.service;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author fan
 * @blame fan
 * @date 2019-10-31 10:16
 **/
public interface EncryptionService {

    /**
     * 加密文件
     *
     * @param filePath 需要加密的文件
     * @return 加密结果
     */
    boolean encryptedFile(String filePath) throws IOException;

    /**
     * 解密文件
     *
     * @param filePath 加密后文件路径
     * @param keyPath  密匙路径
     * @return 解密结果
     * @throws FileNotFoundException 文件不存在
     */
    boolean decryptFile(String filePath, String keyPath) throws IOException;

}
