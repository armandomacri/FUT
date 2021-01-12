package levelDB;

public class StoreImage extends LevelDBConnection{


    public boolean isPresent(String imgUrl) {
        openDB();
        boolean cheak = true;
        if(getValue(imgUrl)==null)
            cheak = false;
        closeDB();
        return cheak;
    }

    public void saveImage(String url, String encodedImg){
        openDB();
        putValue(url, encodedImg);
        closeDB();
    }

    public String getImage(String url){
        openDB();
        String image = getValue(url);
        closeDB();
        return image;
    }

}
