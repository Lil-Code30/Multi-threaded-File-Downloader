import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class main {
    public static void main(String[] args) {
        System.out.println("===== Multi-threaded File Downloader ===== ");
        System.out.println("NOTE: just '.pdf' files are acceptable");

        ArrayList<String> fileUrls = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        ConcurrentHashMap<String, Double> sizeDic = new ConcurrentHashMap<>();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        System.out.print("How many files do you want to download? ");
        int n = input.nextInt();

        // prompting the user to enter the url link of the file to be downloaded
        // TODO: add a verification for the url entered by the user
        for(int i = 0; i < n; i++){
            System.out.println("Please enter the URL of the file you want to download: ");
            fileUrls.add(input.next());
        }

        // test Link :
        // https://raw.githubusercontent.com/wususu/effective-resourses/master/Java/Java%20Concurrency%20in%20Practice.pdf
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
        // TODO: Progress tracking uses AtomicInteger and ConcurrentHashMap
        // downloading all files from the URLs provided (FileUrls)
        for(String fileUrl : fileUrls){
            executor.execute(() -> {
                try{
                    InputStream in = URI.create(fileUrl).toURL().openStream();

                    // Note: It can happen that two threads create the new Date() at the same time
                    String filename = System.currentTimeMillis() + ".pdf";

                    // size in bytes
                    Long size = Files.copy(in, Paths.get("./downloads/"+filename));

                    // in MB
                    double sizeMB = size / (1024.0 * 1024.0);

                    sizeDic.put(filename, sizeMB);
                    System.out.println(Thread.currentThread().getName() + " is downloading " + filename);
                }catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }

        executor.shutdown();

        // wait for all the threads to finish executing
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        // getting the total size of every file downloaded
        System.out.println("=================================");
        System.out.println("FileName  => Size");
        for(Map.Entry<String, Double> entry: sizeDic.entrySet()){
            System.out.println(entry.getKey() + " => " + entry.getValue() + "MB");
        }
        System.out.println("=================================");

    }
}
