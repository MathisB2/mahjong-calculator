package ImageService;

import ImageService.Tiles.DataSet;

public class MultiDataSet extends DataSet {
    MultiDataSet(String[] dataSets){
        for(var dataSet : dataSets)
            loadFolder(dataSet);
    }
}