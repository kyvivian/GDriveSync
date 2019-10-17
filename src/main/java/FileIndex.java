import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.nio.file.Files;

public class FileIndex {
    private final static String INDEX = ".index.txt";
    private File root_dir;

    /**  Constructor
     * Takes in a String that indicates the absolute file path of the directory
     * that syncs the index file
     */
    public FileIndex(String root_dir) {
        this.root_dir = new File(root_dir);
    }

    /** 
     * Returns the root File 
     */
    public File getRoot() {
        return root_dir;
    }

    /**
     * Hides File f
     * 
     * Returns true if able to hide, false if some error occurred.
     */
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

    /**
     * Returns true if index file exists; false otherwise
     * @param cur_dir
     * @return
     * @throws IOException
     */
    public boolean indexExists(File f) {
        File cur = new File(f, INDEX);

        return cur.exists();
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

        if(indexExists(cur_dir)) {
            Files.delete(index.toPath());
        }

        //create the file .index.txt
        boolean createdFile = index.createNewFile();

        if(!createdFile) {
            return false;
        }

        //get the index list 
        String[] fileList = root_dir.list();

        Writer writeFile = null;

        //write to .index.txt and then close the file
        try {
            writeFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(index.getPath()), "utf-8"));
            writeFile.write(fileList.length + "\n\n");
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
        hideFile(index);

        return true;
        
    }

    /**
     * Prints out all the files in the directory.
     */
    public void listFiles() {
        if(!root_dir.isDirectory()) {
            System.out.println("root is not a directory");
        }

        String[] fileList = root_dir.list();

        for (String a : fileList) {
            System.out.println(a);
        }
    }

    /**
     * Syncs the current directory and directories inside it until all 
     * folder/files are synced 
     * @param syncFolder
     * @throws IOException
     */
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

    /**
     * Syncs only the current directory
     */
    public void syncIndex(File syncFolder) throws IOException {
        createIndex(syncFolder);

        List<String> files = getFiles(syncFolder);

        List<String> diff = indexDiff(syncFolder);

        if(diff.size() != 0) {
            files.removeAll(intersection(files, diff));
            if (files.size() != 0) {
                uploadFile(syncFolder, files);
            }
        }

    }

    /**
     * 
     */
    public String getExt(String s) {
        return s.substring(s.lastIndexOf('.'));
    }

    /**
     * 
     * @param f
     * @return
     */
    public void uploadFile(File cur_dir, List<String> l) {
        for(String a : l) {
            File cur_f = new File(cur_dir, a);

        }
    }

    /**
     * Get a list of files in the directory
     */
    public List<String> getFiles(File f) {
        String[] cur = f.list();
        List<String> rt = new ArrayList<String>();

        for(String a : cur) {
            File file = new File(f, a);
            if(file.isFile()) {
                rt.add(a);
            }
        }

        return rt;
    } 

    public List<String> union(List<String> list1, List<String> list2) {
        Set<String> set = new HashSet<String>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<String>(set);
    }

    public List<String> intersection(List<String> list1, List<String> list2) {
        List<String> list = new ArrayList<String>();

        for(String a : list1) {
            if(list2.contains(a)) {
                list.add(a);
            }
        }

        return list;
    }

    /**
     * Gets the difference between the index and the current list of files
     * in the directory and then updates the index
     * 
     * @param args
     * @throws IOException
     */
    public List<String> indexDiff(File cur_dir) throws IOException {
        String [] cur_list = cur_dir.list();

        File ind = new File(cur_dir, INDEX);

        List<String> oldInd = Files.readAllLines(ind.toPath());
        List<String> newInd = new ArrayList<String>(Arrays.asList(cur_list));
         
        int oldCnt = Integer.parseInt(oldInd.get(0));
        System.out.println("cnt is " + oldCnt);

        if(cur_list.length > oldCnt) {
            newInd.removeAll(intersection(oldInd, newInd));
        }
        else if (cur_list.length == oldCnt) {
            return new ArrayList<String>();
        }
        
        return newInd;

    }

    public static void main(String[] args) throws IOException {
        File root = new File("C:\\Users\\Vivian Ky\\Desktop\\X1");
        FileIndex ind = new FileIndex("C:\\Users\\Vivian Ky\\Desktop\\X1");

        System.out.println("ext is " + ind.getExt(".index.txt"));
/*
        List<String> f = ind.getFiles(root);        
        for(String a : f) {
            System.out.println(a);
        }
*/
        //List<String> diff= ind.indexDiff(root);
        //System.out.println("diff has " + diff.size() + " items");
        /*
        if(diff.size() != 0) {
        for(String a: diff) {
            System.out.println(a);
        }
        }
        else {
            System.out.println("no diff");
        }
*/
       // System.out.println(MimeType.type.get(".txt"));

        //ind.createIndex(ind.getRoot());
        //ind.listFiles();

        //ind.syncIndex(new File("C:\\Users\\Vivian Ky\\Desktop\\X1\\3+"));
        /*String[]  list = root.list();
        for(String a : list) {
            System.out.println(a);
        }*/
    }

}