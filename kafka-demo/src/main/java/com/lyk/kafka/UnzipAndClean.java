package com.lyk.kafka;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UnzipAndClean {

    public static void main(String[] args) {
        String rootPath = "D:\\MyWork\\0711"; // 替换为你的路径
        try {
            // 遍历路径下所有文件解压 然后删除压缩包
            Files.walk(Paths.get(rootPath))
                    .filter(path -> Files.isRegularFile(path) && path.toString().toLowerCase().endsWith(".zip")
                            && !path.toString().toLowerCase().contains("front")
                            && !path.toString().toLowerCase().contains("script"))
                    .forEach(UnzipAndClean::unzipAndDelete);

            dealFront(rootPath);
            // 遍历指定路径下的所有目录
            Files.walk(Paths.get(rootPath))
                    .filter(Files::isDirectory)
                    .filter(path -> path.endsWith("applib")) // 确保是 applib 目录
                    .forEach(UnzipAndClean::deleteNonKypFiles);

            // 压缩



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void dealFront(String rootPath) {
        // 解压前端包
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(rootPath))) {
            for (Path path : stream) {
                if (path.toString().toLowerCase().endsWith(".zip")
                        && path.toString().toLowerCase().contains("front-v")) {

                    // 创建以.zip文件名为基础的新目录，去掉.zip后缀
                    Path newDir = Paths.get(rootPath,
                            path.getFileName().toString().replace(".zip", ""));
                    Files.createDirectories(newDir);

                    // 解压到新目录
                    try (ZipFile zipFile = new ZipFile(path.toFile())) {
                        zipFile.stream()
                                .forEach(entry -> {
                                    try {
                                        Files.copy(zipFile.getInputStream(entry),
                                                newDir.resolve(entry.getName()),
                                                StandardCopyOption.REPLACE_EXISTING);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                    }

                    // 删除原始.zip文件
                    Files.delete(path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 单独处理前端包
        try (Stream<Path> stream = Files.list(Paths.get(rootPath))) {
            stream.filter(Files::isDirectory)
                    .filter(dir -> dir.getFileName().toString().toLowerCase().contains("front-v"))
                    .forEach(dir -> {
                        try {
                            deleteUnlistedFiles(dir);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteUnlistedFiles(Path directory) throws IOException {
        List<String> picNames = new ArrayList<>();
        picNames.add("front-layout.zip");
        picNames.add("front-frs.zip");
        picNames.add("front-pic.zip");
        picNames.add("front-platmng.zip");
        picNames.add("front-workflow.zip");
        picNames.add("front-layout.zip");
        try (Stream<Path> filesStream = Files.list(directory)) {
            filesStream.forEach(file -> {
                if (!picNames.contains(file.getFileName().toString()) && !Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException ex) {
                        System.out.println("Failed to delete: " + file);
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    private static void processDirectory(Path dir) throws IOException {
        String dirName = dir.getFileName().toString();
        List<String> picNames = new ArrayList<>();
        picNames.add("front-layout.zip");
        picNames.add("front-frs.zip");
        picNames.add("front-pic.zip");
        picNames.add("front-platmng.zip");
        picNames.add("front-workflow.zip");
        picNames.add("front-layout.zip");
        // 检查目录名是否在文件名列表中
        if (!picNames.contains(dirName.toLowerCase())) {
            // 如果目录名不在列表中，则尝试删除该目录
            Files.delete(dir);
        }
    }

    private static void deleteNonKypFiles(Path applibFolderPath) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(applibFolderPath)) {
            for (Path filePath : directoryStream) {
                if (Files.isRegularFile(filePath) && !filePath.getFileName().toString().startsWith("kyp")) {
                    Files.delete(filePath);
                    System.out.println("Deleted: " + filePath);
                }
            }
        } catch (IOException e) {
            System.err.println("Error deleting non-kyp files in " + applibFolderPath + ": " + e.getMessage());
        }
    }

    private static void unzipAndDelete(Path filePath) {
        try {
            // 获取压缩包的名称作为新目录名
            String fileName = filePath.getFileName().toString();
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex == -1) return; // 不是压缩文件，跳过
            String dirName = fileName.substring(0, dotIndex);
            Path targetDir = filePath.getParent().resolve(dirName);

            // 创建目录
            Files.createDirectories(targetDir);

            // 解压
            unzipFile(filePath);

            // 删除压缩包
            Files.delete(filePath);

            System.out.println("Unzipped and deleted: " + filePath);
        } catch (IOException e) {
            System.err.println("Error processing file: " + filePath + ". Reason: " + e.getMessage());
        }
    }

    private static void unzipFile(Path zipFilePath) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath.toFile()))) {
            ZipEntry entry;
            String zipFileName = zipFilePath.getFileName().toString();
            // 移除.zip后缀得到文件夹名
            String folderName = zipFileName.substring(0, zipFileName.length() - 4);
            Path targetFolderPath = zipFilePath.getParent().resolve(folderName);

            // 创建对应名称的文件夹
            Files.createDirectories(targetFolderPath);

            while ((entry = zis.getNextEntry()) != null) {
                Path filePath = targetFolderPath.resolve(entry.getName());
                if (!entry.isDirectory()) {
                    // 如果是文件，则创建文件及父目录
                    Files.createDirectories(filePath.getParent());
                    try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                } else {
                    // 如果是目录，则创建目录
                    Files.createDirectories(filePath);
                }
            }
            System.out.println("Unzipped to: " + targetFolderPath);
        } catch (IOException e) {
            System.err.println("Error unzipping " + zipFilePath + ": " + e.getMessage());
        }
    }

    private static void cleanApplibFolder(Path applibPath) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(applibPath)) {
            for (Path file : stream) {
                if (!file.getFileName().toString().startsWith("kyp") && Files.isRegularFile(file)) {
                    Files.delete(file);
                    System.out.println("Deleted from applib: " + file);
                }
            }
        } catch (IOException e) {
            System.err.println("Error cleaning applib folder: " + applibPath + ". Reason: " + e.getMessage());
        }
    }
}
