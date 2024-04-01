import java.util.List;

public class Main {
    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(100, 1, 1, 110.5);
        GameProgress gameProgress2 = new GameProgress(90, 2, 10, 100.7);
        GameProgress gameProgress3 = new GameProgress(80, 3, 100, 10.76);

        gameProgress1.saveGame("C:\\f\\Games\\savegames\\save1.dat", gameProgress1);
        gameProgress2.saveGame("C:\\f\\Games\\savegames\\save2.dat", gameProgress2);
        gameProgress3.saveGame("C:\\f\\Games\\savegames\\save3.dat", gameProgress3);

        GameProgress.packFilesToZip("C:\\f\\Games\\savegames\\savegames.zip",
                List.of("C:\\f\\Games\\savegames\\save1.dat", "C:\\f\\Games\\savegames\\save2.dat", "C:\\f\\Games\\savegames\\save3.dat"));

        GameProgress.deleteSavedFiles("C:\\f\\Games\\savegames");

        GameProgress.openZip("C:\\f\\Games\\savegames\\savegames.zip", "C:\\f\\Games\\savegames");

        GameProgress loadedProgress = GameProgress.openProgress("C:\\f\\Games\\savegames\\save3.dat");
        if (loadedProgress != null) {
            System.out.println("GameProgress загружен: " + loadedProgress);
        }
    }
}