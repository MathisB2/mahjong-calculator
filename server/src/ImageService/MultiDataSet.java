package ImageService;

public class MultiDataSet extends DataSet {
    private String[] dataSets;
    MultiDataSet(String[] dataSets) {
        this.dataSets = dataSets;

        for(var dataSet : dataSets){
            load(dataSet);
        }
    }

    public void save(){
        for(var dataSet : this.dataSets){
            save(dataSet);
        }
    }
}