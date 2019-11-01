package top.afan.encryption.common.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author fan
 * @blame fan
 * @date 2019-10-31 10:21
 **/
public class EncryptionUtil {


    private static final String DES = "DES";

    private static final String DEFAULT_SECRET_KEY = "?:P)(OL><KI*&UJMNHY^%TGBVFR$#EDCXSW@!QAZ";

    private static Key DEFAULT_KEY;

    static {
        try {
            DEFAULT_KEY = obtainKey(DEFAULT_SECRET_KEY);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("初始化失败！");
        }
    }


    /**
     * 获得密匙Key
     *
     * @param keyStr 密匙Str
     * @return  密匙Key
     * @throws NoSuchAlgorithmException  转化失败
     */
    private static Key obtainKey(String keyStr) throws NoSuchAlgorithmException {
        Key key = DEFAULT_KEY;
        if (Objects.nonNull(keyStr)) {
            KeyGenerator generator = KeyGenerator.getInstance(DES);
            generator.init(new SecureRandom(keyStr.getBytes()));
            key = generator.generateKey();
        }
        return key;
    }


    /**
     * 默认密匙加密 以byte[]明文输入, byte[]密文输出
     *
     * @param data   明文输入
     * @return 密文输出
     */
    public static byte[] obtainEncode(byte[] data) {
        return obtainEncode(null, data);
    }

    /**
     * 以byte[]明文输入, byte[]密文输出
     *
     * @param keyStr 密匙Str
     * @param data   明文输入
     * @return 密文输出
     */
    public static byte[] obtainEncode(String keyStr, byte[] data) {
        byte[] byteFina = null;
        try {
            Key key = obtainKey(keyStr);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byteFina = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFina;
    }

    /**
     * 默认密匙解密 以byte[]密文输入,以byte[]明文输出
     *
     * @param data 密文输入
     * @return 明文输出
     */
    public static byte[] obtainDecode(byte[] data) {
       return obtainDecode(null , data);
    }

    /**
     * 以byte[]密文输入,以byte[]明文输出
     *
     * @param keyStr 密匙Str
     * @param data   密文输入
     * @return 明文输出
     */
    public static byte[] obtainDecode(String keyStr, byte[] data) {
        byte[] byteFina = null;
        try {
            Key key = obtainKey(keyStr);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byteFina = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFina;
    }


    /**
     * 压缩
     *
     * @param data 待压缩数据
     * @return byte[] 压缩后的数据
     */
    public static byte[] compress(byte[] data) {
        byte[] output;
        Deflater compresser = new Deflater();
        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                arrayOutputStream.write(buf, 0, i);
            }
            output = arrayOutputStream.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                arrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        compresser.end();
        return output;
    }

    /**
     * 解压缩
     *
     * @param data 待压缩的数据
     * @return byte[] 解压缩后的数据
     */
    public static byte[] decompress(byte[] data) {
        byte[] output;
        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                arrayOutputStream.write(buf, 0, i);
            }
            output = arrayOutputStream.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                arrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        decompresser.end();
        return output;
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
}
