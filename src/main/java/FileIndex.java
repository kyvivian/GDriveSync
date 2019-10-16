import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileIndex {
    private File root_dir;

    public FileIndex(String root_dir) {
        this.root_dir = new File(root_dir);
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
        File index = new File(cur_dir, ".index.txt");

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

    public void syncIndex(File syncFolder) {
        String[] fileList = syncFolder.list();

        for(String f : fileList) {
            File cur_file = new File(syncFolder, f);

            if(cur_file.isDirectory()) {
                //System.out.println(cur_file.getName() + " is a directory");
                syncIndex(cur_file);
            }
            else {
                //System.out.println(cur_file.getName() + " is a file");
                System.out.println(cur_file.getName());
            }

        }
    }

    public static void main(String[] args) throws IOException {
        File root = new File("C:\\Users\\Vivian Ky\\Desktop\\X1");
        FileIndex ind = new FileIndex("C:\\Users\\Vivian Ky\\Desktop\\X1");
        //ind.syncIndex(new File("C:\\Users\\Vivian Ky\\Desktop\\X1\\3+"));
        ind.createIndex(root);
    }

}