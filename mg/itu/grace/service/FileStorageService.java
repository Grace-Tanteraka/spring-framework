package mg.itu.grace.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import mg.itu.grace.dto.MapFile;
import mg.itu.grace.utils.ConfigLoader;

public class FileStorageService {

    public static void saveFileToDisk(MapFile mapFile) {
        if (mapFile == null || mapFile.getFileContent() == null || mapFile.getFileContent().length == 0) {
            return;
        }

        String uploadDir = ConfigLoader.getProperty("FILE_UPLOAD_PATH", "uploads/");

        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(mapFile.getFileName());

            // Écrire les octets
            Files.write(filePath, mapFile.getFileContent());

            System.out.println("Fichier enregistré avec succès dans : " + filePath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement du fichier : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
