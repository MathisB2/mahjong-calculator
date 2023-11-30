package ImageService;

import ImageMatching.ImageTile;

import java.io.File;
import java.util.ArrayList;

public class DataSet {
    private String name;
    private ArrayList<ImageTile> tiles;

    public DataSet() {
        tiles = new ArrayList<>();
    }

    public DataSet(String name) {
        this.tiles = new ArrayList<>();
        this.name = name;
        loadFolder(name);
    }
    private void loadFolder(String folderName){
        if(folderName.equals(name)){
            System.err.println("dataSet "+folderName+" already loaded");
            return;
        }

        File folder = new File("src/img/dataSet/"+folderName);
        File[] listOfFiles = folder.listFiles();
        String fileName = "";

        for(File file : listOfFiles){
            fileName = file.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));

            this.tiles.add(new ImageTile(fileName,file.toString()));
        }
    }
}
