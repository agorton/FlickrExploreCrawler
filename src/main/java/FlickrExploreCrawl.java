import java.util.Properties;

public class FlickrExploreCrawl {

    public static void main(String[] args) {
        Properties properties = null;

        if (args.length > 0){
            if (args.length < 3){
                System.out.println("Usage: FlickrExploreCrawl <apiKey> <secret> <filePath>");
            } else {
                properties.put("apiKey", args[0]);
                properties.put("secret", args[1]);
                properties.put("path", args[2]);
            }
        } else {
            properties = new PropertiesLoader().getProperties();
        }

        new InterestingnessFetcher(properties).fetch();

    }

}