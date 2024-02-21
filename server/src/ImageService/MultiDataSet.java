package ImageService;

import ImageService.Tiles.DataSet;

/**
 * allow to group datasets to be compared with the tiles found
 */
public class MultiDataSet extends DataSet {
    /**
     * loads all requested datasets
     * @param dataSets contains the names of the datasets to use
     */
    MultiDataSet(String[] dataSets){
        for(var dataSet : dataSets)
            loadFolder(dataSet);
    }
}