import java.io.File;

public class FileIndex {
    private File root_dir;

    public FileIndex(String root_dir) {
        this.root_dir = new File(root_dir);
    }

    /* Creates a hidden index file that stores all the  
     * files/dirs of the current directory
     * 
     * Parameters:
     *    cur_dir = directory that we want to index
     * 
     * Returns:
     *    -1 if cur_dir is not a directory or there already exists ".index.txt"
     *     1 if ".index.txt" is successfully created
     */
    public int createIndex(File cur_dir){
        if(!cur_dir.isDirectory()) {
            return -1;
        }
        File index = new File(cur_dir, ".index.txt");

        return 1;
    }

}