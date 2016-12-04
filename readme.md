#Flickr Explore Crawler
 A simple Java 8 application that fetches all the top pictures from flickr's explore page and filters them based on if 
 they are an acceptable size for a wallpaper, then saves them to a directory supplied.  

The application can take parameters for the api key, secret and file path as follows: 
 ```
 FlickrExploreCrawl <apiKey> <secret> <filePath>
```
###setup.properties File
If you choose to use a properties file the keys are as follows: 
````
apiKey = FLICKR API KEY

secret = FLICKR SECRET

path = SAVE LOCATION