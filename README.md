# Multi-threaded File Downloader

A concurrent PDF downloader written in Java that uses a fixed thread pool to download multiple files simultaneously, saving them to a local `downloads/` directory and reporting their sizes on completion.
 
---

## Features
 
- Downloads multiple `.pdf` files concurrently using a **4-thread fixed thread pool**
- Validates URLs at input time (format + `.pdf` extension enforcement)
- Auto-creates a `downloads/` directory if it doesn't exist
- Reports per-file download progress with the thread name that handled it
- Displays a final summary table of all downloaded files and their sizes in MB
  
---

## Test URLs
 
You can use these publicly available PDFs to test the downloader:
 
```
https://raw.githubusercontent.com/wususu/effective-resourses/master/Java/Java%20Concurrency%20in%20Practice.pdf
https://www.cs.cmu.edu/afs/cs.cmu.edu/user/gchen/www/download/java/LearnJava.pdf
https://www.uvm.edu/~cbcafier/itpacs/itpacs_cafiero.pdf
```
 
---

## How It Works
 
1. The user specifies how many files to download and provides a `.pdf` URL for each.
2. A `ConcurrentHashMap` tracks filenames and their sizes across threads.
3. An `ExecutorService` with 4 threads picks up download tasks concurrently.
4. Each task streams the file directly to disk using `Files.copy()` and names it by the current timestamp (milliseconds) to avoid collisions.
5. After all tasks finish (or after a 1-minute timeout), a summary is printed.
   
---
