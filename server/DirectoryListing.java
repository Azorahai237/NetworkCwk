import java.io.File;
import java.util.ArrayList;
import java.util.List;
// made class in order to return all filenames of a directory
public class DirectoryListing {
    public static List<String> FileNamesInDirectory(String directoryPath) {
        List<String> fileList = new ArrayList<>();

        // Create a File object representing the directory
        File directory = new File(directoryPath);

        // Check if the specified path exists and is a directory
        if (directory.exists() && directory.isDirectory()) {
            // List all files in the directory
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    fileList.add(file.getName());
                }
            }
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }

        return fileList;
    }

    public static void main(String[] args) {}

}