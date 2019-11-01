package top.afan.encryption.service.Impl;

import org.springframework.stereotype.Service;
import top.afan.encryption.common.utils.FileUtil;
import top.afan.encryption.service.EncryptionService;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author fan
 * @blame fan
 * @date 2019-10-31 10:20
 **/
@Service
public class EncryptionServiceImpl implements EncryptionService {


    @Override
    public boolean encryptedFile(String filePath) throws IOException {
        return FileUtil.encryptedFile(filePath);
    }

    @Override
    public boolean decryptFile(String filePath, String keyPath) throws IOException {
        return FileUtil.decryptFile(filePath, keyPath);
    }
}
