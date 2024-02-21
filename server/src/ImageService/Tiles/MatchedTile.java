package ImageService.Tiles;

import ImageService.ImageEncoder;
import ImageService.ImageTile;
import org.json.JSONObject;
import org.opencv.core.Mat;


public class MatchedTile extends ImageTile {
    private Mat extractedImage;
    public MatchedTile(ImageTile tile, ImageTile extractedTile) {
        super(tile.getName(), tile.getImg());

        this.extractedImage = extractedTile.getImg();
        this.x = extractedTile.x;
        this.y = extractedTile.y;
    }
    public Mat getExtractedImg(){
        return this.extractedImage;
    }
    @Override
    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("name", this.getName());
            obj.put("image", (new ImageEncoder()).encode(this.extractedImage));
        }catch (Exception e){};

        return obj;
    }
}
