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

        ArrayList<String> fileUrls = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        System.out.print("How many files do you want to download? ");
        int n = input.nextInt();

        // prompting the user to enter the url link of the file to be downloaded
        for(int i = 0; i < n; i++){
            System.out.println("Please enter the URL of the file you want to download: ");
            fileUrls.add(input.next());
        }

        // test Link : https://raw.githubusercontent.com/wususu/effective-resourses/master/Java/Java%20Concurrency%20in%20Practice.pdf
        // https://www.cs.cmu.edu/afs/cs.cmu.edu/user/gchen/www/download/java/LearnJava.pdf
        // https://www.uvm.edu/~cbcafier/itpacs/itpacs_cafiero.pdf

        // check if the directory exist, if not create the directory -> downloads
        Path path = Paths.get("./downloads");
        if(Files.notExists(path) && !Files.isDirectory(path)){
            try{
                Files.createDirectory(path);
            }catch(IOException e){
                e.getMessage();
            }
        }

        // downloading all files from the URLs provided (FileUrls)
        for(String fileUrl : fileUrls){
            try{
                InputStream in = URI.create(fileUrl).toURL().openStream();
                String filename = new Date().getTime() + ".pdf";
                Files.copy(in, Paths.get("./downloads/"+filename));
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
