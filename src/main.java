import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        System.out.println("===== Multi-threaded File Downloader ===== ");

        ArrayList<String> FilesPath = new ArrayList<>();
        Scanner input = new Scanner(System.in);


//        System.out.print("How many files do you want to download? ");
//        int n = input.nextInt();
//
//        for(int i = 0; i < n; i++){
//            System.out.println("Please enter the URL of the file you want to download: ");
//            FilesPath.add(input.next());
//        }
        // test Link : https://raw.githubusercontent.com/wususu/effective-resourses/master/Java/Java%20Concurrency%20in%20Practice.pdf

        System.out.println("Enter the url of the file to be downloaded: ");
        String fileUrl = input.nextLine();

        try{

            // check if the directory exist, if not create the directory
            Path path = Paths.get("./downloads");
            if(Files.notExists(path) && !Files.isDirectory(path)){
                Files.createDirectory(path);
            }

            InputStream in = URI.create(fileUrl).toURL().openStream();

            String filename = new Date().getTime() + ".pdf";
            Files.copy(in, Paths.get("./downloads/"+filename));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
