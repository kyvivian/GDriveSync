import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class FileIndex {
    private final static String INDEX = ".index.txt";
    private File root_dir;

    public FileIndex(String root_dir) {
        this.root_dir = new File(root_dir);
    }

    public File getRoot() {
        return root_dir;
    }

    private boolean hideFile(File f) throws IOException {
        boolean hidden = f.isHidden();

        if(hidden) {
            return hidden;
        }

        Path path = Paths.get(f.getPath());

        Files.setAttribute(path, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);

        hidden = f.isHidden();

        return hidden;
    }

    /*
     * Creates a hidden index file that stores all the files/dirs of the current
     * directory
     * 
     * Parameters: cur_dir = directory that we want to index
     * 
     * Returns: false if cur_dir is not a directory or there already exists
     * ".index.txt" true if ".index.txt" is successfully created
     */
    public boolean createIndex(File cur_dir) throws IOException {
        System.out.println("Creating index for " + cur_dir.getPath() + "\n");
        if(!cur_dir.isDirectory()) {
            return false;
        }
        File index = new File(cur_dir, INDEX);

        if(index.exists()) {
            System.out.println("index already exists");
            return true;
        }

        //get the index list 
        String[] fileList = root_dir.list();

        //create the file .index.txt
        boolean createdFile = index.createNewFile();

        if(!createdFile) {
            return false;
        }

        Writer writeFile = null;

        //write to .index.txt and then close the file
        try {
            writeFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(index.getPath()), "utf-8"));
            for (String a: fileList) {
                writeFile.write(a + "\n");
            }
        }
        catch (IOException ex) {
            System.out.println("failed to write to index");
            return false;
        }
        finally {
            try {
                writeFile.close();
            }
        catch (Exception ex) {System.out.println("couldn't close index properly");}
        }

        //hide the index file
        boolean hide = hideFile(index);
        String h = hide ? "true" : "false";
        System.out.println("hide index is " + h);

        return true;
        
    }
    
    public void listFiles() {
        if(!root_dir.isDirectory()) {
            System.out.println("root is not a directory");
        }

        String[] fileList = root_dir.list();

        for (String a : fileList) {
            System.out.println(a);
        }
    }

    public void syncAllIndex(File syncFolder) throws IOException {
        String[] fileList = syncFolder.list();

        for(String f : fileList) {
            File cur_file = new File(syncFolder, f);

            if(cur_file.isDirectory()) {
                //System.out.println(cur_file.getName() + " is a directory");
                syncIndex(cur_file);
            }
        }
    }

    public void syncIndex(File syncFolder) throws IOException {
        File index = new File(syncFolder,  INDEX);

        boolean haveIndex = index.exists();

        if(!haveIndex) {
            createIndex(syncFolder);
        }


    }

    public static void main(String[] args) throws IOException {
        File root = new File("C:\\Users\\Vivian Ky\\Desktop\\X1");
        FileIndex ind = new FileIndex("C:\\Users\\Vivian Ky\\Desktop\\X1");
        ind.createIndex(ind.getRoot());
        ind.listFiles();
        //ind.syncIndex(new File("C:\\Users\\Vivian Ky\\Desktop\\X1\\3+"));
        /*String[]  list = root.list();
        for(String a : list) {
            System.out.println(a);
        }*/
    }

}