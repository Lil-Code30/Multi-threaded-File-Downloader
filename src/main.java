import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

        input.nextLine(); // consume leftover newline

        // prompting the user to enter the url link of the file to be downloaded
        for(int i = 0; i < n; i++){
            String url;

            while(true){
                System.out.println("Please enter the URL of the file you want to download: ");
                url = input.nextLine().trim();

                try{
                    // validate URL format
                    new URL(url);

                    // validate PDF extension
                    if(!url.toLowerCase().endsWith(".pdf")){
                        System.out.println("The Link must end with .pdf");
                        continue;
                    }

                    // if everything is OK, break the loop
                    break;
                }catch(Exception e){
                    System.out.println("Try again (must be a valid .pdf URL)");
                }
            }

            fileUrls.add(url);
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
                // using try-with-resources to automatically close the InputStream
                try{
                    long startTime = System.nanoTime();

                    InputStream in = URI.create(fileUrl).toURL().openStream();
                    String filename = System.currentTimeMillis() + ".pdf";

                    // size in bytes
                    long size = Files.copy(in, Paths.get("./downloads/"+filename));

                    long endTime = System.nanoTime();

                    double  timeInSeconds = (endTime - startTime) / 1_000_000_000.0;

                    sizeDic.put(filename, size / (1024.0 * 1024.0));
                    System.out.printf("%s downloaded %s (%d bytes) in %.3f seconds %n", Thread.currentThread().getName(), filename, size,  timeInSeconds);
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
        System.out.println("FileName  => Size (approximate)");
        for(Map.Entry<String, Double> entry: sizeDic.entrySet()){
            System.out.printf("%s => %.2f MB%n", entry.getKey(), entry.getValue());
        }
        System.out.println("=================================");

    }
}
