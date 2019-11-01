package top.afan.encryption.common.utils;


import top.afan.encryption.common.model.KeyFileModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fan
 * @blame fan
 * @date 2019-10-31 11:16
 **/
public class FileUtil {


    /**
     * 对文件加密
     *
     * @param filePath 文件路径
     * @throws FileNotFoundException 文件不存在
     */
    public static boolean encryptedFile(String filePath) throws IOException {
        File sourceFile = checkFile(filePath);
        File targetFile = changeFileType(sourceFile, ".fan");
        File keyFile = changeFileType(sourceFile, ".key");

        try (FileInputStream sourceIn = new FileInputStream(sourceFile);
             FileOutputStream targetOut = new FileOutputStream(targetFile);
             FileOutputStream keyOut = new FileOutputStream(keyFile)
        ) {
            List<Integer> encodeLens = encryptedFileStream(sourceIn, targetOut);
            targetOut.flush();
            writeKeyFile(sourceFile, keyOut, encodeLens);
            keyOut.flush();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return sourceFile.delete();
    }

    /**
     * 对文件进行解密
     *
     * @param filePath 加密后的文件
     * @param keyPath  密匙文件
     * @throws FileNotFoundException 文件不存在或已损坏
     */
    public static boolean decryptFile(String filePath, String keyPath) throws IOException {
        File sourceFile = checkFile(filePath);
        File keyFile = checkFile(keyPath);
        KeyFileModel keyFileModel = keyFile2KeyModel(keyFile);
        File targetFile = changeFileType(sourceFile, keyFileModel.getFileType());
        try (FileInputStream sourceIn = new FileInputStream(sourceFile);
             FileOutputStream targetOut = new FileOutputStream(targetFile)
        ) {
            List<Integer> encodeLens = keyFileModel.getEncodeLens();
            decryptFileStream(encodeLens, sourceIn, targetOut);
            targetOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sourceFile.delete() && keyFile.delete();
    }

    /**
     * 解密文件流
     *
     * @param encodeLens 每次写入的加密字节数量
     * @param sourceIn   加密后的文件流
     * @param targetOut  解密的文件流
     */
    private static void decryptFileStream(List<Integer> encodeLens, FileInputStream sourceIn, FileOutputStream targetOut) throws IOException {
        byte[] buf;
        for (Integer encodeLen :
                encodeLens) {
            buf = new byte[encodeLen];
            if (sourceIn.read(buf) == -1) {
                throw new RuntimeException("加密文件已损坏！");
            }
            byte[] decode = EncryptionUtil.obtainDecode(buf);
            byte[] decompress = EncryptionUtil.decompress(decode);
            targetOut.write(decompress);
        }
    }

    /**
     * 密匙文件转为密匙模型对象
     *
     * @param keyFile 密匙文件
     * @return 密匙模型
     */
    private static KeyFileModel keyFile2KeyModel(File keyFile) {

        ArrayList<Integer> encodeLens = new ArrayList<>();
        KeyFileModel keyFileModel = new KeyFileModel(encodeLens);
        String fileType = "";
        try (FileInputStream keyIn = new FileInputStream(keyFile)
        ) {
            byte[] intBytes = new byte[4];
            keyIn.read(intBytes);
            int typeLen = byteArr2Int(intBytes);
            byte[] typeBytes = new byte[typeLen];
            keyIn.read(typeBytes);
            fileType = new String(typeBytes);
            keyFileModel.setFileType(fileType);

            while (keyIn.read(intBytes) != -1) {
                encodeLens.add(byteArr2Int(intBytes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (encodeLens.isEmpty() || fileType.isEmpty()) {
            throw new RuntimeException("密匙文件已损坏！");
        }
        return keyFileModel;
    }

    /**
     * 加密文件流
     *
     * @param in  输入流
     * @param out 输出流
     * @return 每次写入的加密字节数量
     * @throws IOException Io异常
     */
    private static List<Integer> encryptedFileStream(FileInputStream in, FileOutputStream out) throws IOException {
        final int bufSize = 1024;
        byte[] buf = new byte[bufSize];
        int len;
        List<Integer> encodeLens = new ArrayList<>();
        while ((len = in.read(buf)) != -1) {
            if (len != bufSize) {
                buf = Arrays.copyOfRange(buf, 0, len);
            }
            byte[] compress = EncryptionUtil.compress(buf);
            byte[] encode = EncryptionUtil.obtainEncode(compress);
            out.write(encode);
            encodeLens.add(encode.length);
        }
        return encodeLens;
    }

    /**
     * 写入源文件类型
     *
     * @param keyFileOut 目标文件输出流
     * @param encodeLens 每次写入的加密字节数量
     */
    private static void writeKeyFile(File sourceFile, FileOutputStream keyFileOut, List<Integer> encodeLens) throws IOException {
        String sourceFileType = readFileType(sourceFile);
        byte[] typeByteArr = sourceFileType.getBytes();
        byte[] lengthByteArr = intToByteArr(typeByteArr.length);
        keyFileOut.write(lengthByteArr);
        keyFileOut.write(typeByteArr);
        for (int encodeLen :
                encodeLens) {
            byte[] lenBytes = intToByteArr(encodeLen);
            keyFileOut.write(lenBytes);
        }
    }

    /**
     * byte数组转int
     *
     * @param bytes byte数组
     * @return int值
     */
    private static int byteArr2Int(byte[] bytes) {
        return (bytes[0] & 0xff) << 24 | (bytes[1] & 0xff) << 16 | (bytes[2] & 0xff) << 8 | (bytes[3] & 0xff);
    }

    /**
     * int转byte数组
     *
     * @param num int值
     * @return byte数组
     */
    private static byte[] intToByteArr(int num) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((num >> 24) & 0xff);
        bytes[1] = (byte) ((num >> 16) & 0xff);
        bytes[2] = (byte) ((num >> 8) & 0xff);
        bytes[3] = (byte) (num & 0xff);
        return bytes;
    }

    /**
     * 改变文件类型
     *
     * @param sourceFile 源文件
     * @return 目标文件
     */
    private static File changeFileType(File sourceFile, String fileSuffix) {
        String absolutePath = sourceFile.getAbsolutePath();
        String substring = absolutePath.substring(0, absolutePath.lastIndexOf("."));
        return new File(substring + fileSuffix);
    }

    /**
     * 读取源文件类型
     *
     * @param sourceFile 源文件
     * @return 文件类型后缀
     */
    private static String readFileType(File sourceFile) {
        String absolutePath = sourceFile.getAbsolutePath();
        return absolutePath.substring(absolutePath.lastIndexOf("."));
    }

    /**
     * 检查文件是否存在
     *
     * @param filePath 文件路径
     * @return 文件对象
     * @throws FileNotFoundException 文件不存在
     */
    private static File checkFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在，请检查参数！  " + filePath);
        }
        if (file.isDirectory()) {
            throw new IOException("输入参数为文件夹，请检查参数！  " + filePath);
        }
        return file;
    }

}
