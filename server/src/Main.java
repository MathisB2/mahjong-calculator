import java.awt.*;
import java.io.File;

public class Main {

    /**
     * import and rename images named 0000.png, 0001.png, ...
     * @param folderName name of the folder containing dataSet
     */
    public static void importDataSet(String folderName) {

        File folder = new File("src/img/dataSet/" + folderName);


        String[] fileNames={

                "bamboo_1", "bamboo_2", "bamboo_3", "bamboo_4", "bamboo_5", "bamboo_6", "bamboo_7", "bamboo_8", "bamboo_9",
                "dot_1", "dot_2", "dot_3", "dot_4", "dot_5", "dot_6", "dot_7", "dot_8", "dot_9",
                "character_1", "character_2", "character_3", "character_4", "character_5", "character_6", "character_7", "character_8", "character_9",
                "wind_east", "wind_south", "wind_west", "wind_north",
                "dragon_red", "dragon_green", "dragon_white",
                "flower_1_plum_blossom_east", "flower_2_orchid_south", "flower_3_chrysanthemum_west", "flower_4_bamboo_north",
                "season_1_spring_east", "season_2_summer_south", "season_3_autumn_west", "season_4_winter_north"
        };




        File[] fileList=folder.listFiles();


        int i=0;
        String ext;
        if (folder.exists() && fileList.length<=fileNames.length) {

            for (final File fileEntry : fileList) {
                if (fileEntry.isFile()) {
                    System.out.println(fileEntry.getName());
                    ext=getFileExtension(fileEntry.getName());
                    System.out.println("src/img/dataSet/"+folderName+"/"+fileNames[i]+ext);
                    fileEntry.renameTo(new File("src/img/dataSet/"+folderName+"/"+fileNames[i]+ext));
                    i++;
                }
            }
        }


    }


    public static String getFileExtension(String fileName){
        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if (i > p) {
            return fileName.substring(i);
        }
        return "";
    }



    public static double calculateAngle(Point a, Point b, Point c) {
        // Calcul des vecteurs AB et BC
        double vectorABx = b.x - a.x;
        double vectorABy = b.y - a.y;
        double vectorBCx = c.x - b.x;
        double vectorBCy = c.y - b.y;

        // Calcul des normes des vecteurs
        double normAB = Math.sqrt(vectorABx * vectorABx + vectorABy * vectorABy);
        double normBC = Math.sqrt(vectorBCx * vectorBCx + vectorBCy * vectorBCy);

        // Calcul du produit scalaire
        double dotProduct = vectorABx * vectorBCx + vectorABy * vectorBCy;

        // Calcul de l'angle en radians
        double angleInRadians = Math.acos(dotProduct / (normAB * normBC));

        // Conversion de l'angle en degrés
        double angleInDegrees = Math.toDegrees(angleInRadians);

        return angleInDegrees;
    }

    public static boolean isClockwise(Point[] points) {
        int numPoints = points.length;

        if (numPoints < 3) {
            return false;
        }

        double sum = 0;
        for (int i = 0; i < numPoints; i++) {
            Point currentPoint = points[i];
            Point nextPoint = points[(i + 1) % numPoints];
            sum += (nextPoint.x - currentPoint.x) * (nextPoint.y + currentPoint.y);
        }

        // Si la somme est négative, les points sont disposés dans le sens horaire
        return sum < 0;
    }



    public static void main(String[] args) {
        importDataSet("data2");
    }

}