import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class GameProgress implements Serializable {
    private static final long serialVersionUID = 1L;

    private int health;
    private int weapons;
    private int lvl;
    private double distance;

    public GameProgress(int health, int weapons, int lvl, double distance) {
        this.health = health;
        this.weapons = weapons;
        this.lvl = lvl;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "GameProgress{" +
                "health=" + health +
                ", weapons=" + weapons +
                ", lvl=" + lvl +
                ", distance=" + distance +
                '}';
    }

    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            System.out.println("Игра сохранена: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void packFilesToZip(String zipFilePath, List<String> filesToZip) {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String filePath : filesToZip) {
                File file = new File(filePath);
                if (file.exists()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zos.putNextEntry(zipEntry);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                        zos.closeEntry();
                        System.out.println("Файл " + filePath + " добавлен в архив");
                    } catch (IOException e) {
                        System.err.println("Файл не добавлен в архив: " + e.getMessage());
                    }
                } else {
                    System.err.println("Такого файла не существует" + filePath);
                }
            }
            System.out.println("Архив создан: " + zipFilePath);
        } catch (IOException e) {
            System.err.println("Архив не создан: " + e.getMessage());
        }
    }

    public static void deleteSavedFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.getName().equals("savegames.zip")) {
                    file.delete();
                    System.out.println("Файл удален: " + file.getName());
                }
            }
        }
    }

    public static void openZip(String zipFilePath,String extractFolderPath) {
        try (FileInputStream fis = new FileInputStream(zipFilePath);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String filePath = extractFolderPath + File.separator + entry.getName();
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                    System.out.println("Файл извлечен: " + entry.getName());
                } catch (IOException e) {
                    System.err.println("Файл не извлечен: " + e.getMessage());
                }
            }
            System.out.println("Все файлы извлечены в : " + extractFolderPath);
        } catch (IOException e) {
            System.err.println("Файлы не извлечены: " + e.getMessage());
        }
    }

    public static GameProgress openProgress(String saveFile) {
        try (FileInputStream fis = new FileInputStream(saveFile);
        ObjectInputStream ois = new ObjectInputStream(fis)){
            GameProgress gameProgress = (GameProgress) ois.readObject();
            System.out.println("Игровой прогресс загружен из: " + saveFile);
            return  gameProgress;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Не удалось загрузить игровой прогесс" + e.getMessage());
            return null;
        }
    }
}