import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by andrewgorton on 04/12/2016.
 */
public class InterestingnessFetcher {
    static String apikey, secret, path;
    Properties properties;

    public InterestingnessFetcher(Properties properties) {
        this.properties = properties;
        apikey = properties.getProperty("apiKey");
        secret = properties.getProperty("secret");
        path = properties.getProperty("path");

    }

    // convert filename to clean filename
    private static String convertToFileSystemChar(String name) {
        String erg = "";
        Matcher m = Pattern.compile("[a-z0-9 _#&@\\[\\(\\)\\]\\-\\.]", Pattern.CASE_INSENSITIVE).matcher(name);
        while (m.find()) {
            erg += name.substring(m.start(), m.end());
        }
        if (erg.length() > 200) {
            erg = erg.substring(0, 200);
            System.out.println("cut filename: " + erg);
        }
        return erg;
    }

    private static boolean saveImage(Flickr f, Photo p) {

        String cleanTitle = convertToFileSystemChar(p.getTitle());

        File orgFile = new File(path + File.separator + cleanTitle + "_" + p.getId() + "_o." + p.getOriginalFormat());
        File largeFile = new File(path + File.separator + cleanTitle + "_" + p.getId() + "_b." + p.getOriginalFormat());

        if (orgFile.exists() || largeFile.exists()) {
            System.out.println(p.getTitle() + "\t" + p.getLargeUrl() + " skipped!");
            return false;
        }

        try {
            Photo nfo = f.getPhotosInterface().getInfo(p.getId(), null);
            if (nfo.getOriginalSecret().isEmpty()) {
                ImageIO.write(p.getLargeImage(), p.getOriginalFormat(), largeFile);
                System.out.println(p.getTitle() + "\t" + p.getLargeUrl() + " was written to " + largeFile.getName());
            } else {
                p.setOriginalSecret(nfo.getOriginalSecret());
                ImageIO.write(p.getOriginalImage(), p.getOriginalFormat(), orgFile);
                System.out.println(p.getTitle() + "\t" + p.getOriginalUrl() + " was written to " + orgFile.getName());
            }
        } catch (FlickrException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void fetch(){
        fetchInterestingnessPictures();
    }

    private void fetchInterestingnessPictures() {

        Flickr flickr = new Flickr(apikey, secret, new REST());
        new File(path).mkdirs();

        try {
            PhotoList<Photo> photos = flickr.getInterestingnessInterface().getList();
            photos.stream().
                    filter((Photo p) -> p.getOriginalWidth() > 2000 && p.getOriginalHeight() > 1500).
                    forEach((Photo p) -> saveImage(flickr, p));

        } catch (FlickrException e){
            e.printStackTrace();
        }
    }
}
