package me.bahadir.gimsearch;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bahadir on 26/01/15.
 */
public class GIMSearch {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/600.2.5 (KHTML, like Gecko) Version/8.0.2 Safari/600.2.5";
    private static final String HOST_ROOT = "http://image.google.com";
    private static final String HEADER_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";


    public List<Image> getSinglePage(String searchQuery) throws GIMSearchParseException {
        List<Image> images = new LinkedList<Image>();
        try {
            Connection.Response mainResponse = Jsoup.connect(HOST_ROOT)
                    .userAgent(USER_AGENT)
                    .execute();

            Document mainPage = mainResponse.parse();

            //Find form
            Elements forms = mainPage.getElementsByTag("form");

            FormElement form = null;
            for(Element formElement : forms ) {

                if(formElement.attr("name").equals("f")) {
                    form = (FormElement) formElement;
                    break;
                }

            }

            if(form == null) {
                throw new GIMSearchParseException("Can not find form element");
            }

            //Find query field

            List<Connection.KeyVal> params = form.formData();

            for(Connection.KeyVal param : params) {
                if(param.key().equals("q")) {
                    param.value(searchQuery);
                    break;
                }
            }

            //Send form
            Connection.Response formSubmitResponse = form.submit()
                    .data(params)
                    .userAgent(USER_AGENT)
                    .header("Accept",HEADER_ACCEPT)
                    .header("Cache-Control", "max-age=0")
                    .cookies(mainResponse.cookies())
                    .followRedirects(true)
                    .execute();

            Document returnPage = formSubmitResponse.parse();

            Elements meta = returnPage.select("meta[http-equiv*=refresh]");

            if (!meta.isEmpty()) {

                String url =
                        HOST_ROOT
                                + meta.attr("content").split(";")[1].replace("url=","");

                returnPage = Jsoup
                        .connect(url)
                        .userAgent(USER_AGENT)
                        .header("Accept",HEADER_ACCEPT)
                        .header("Cache-Control","max-age=0")
                        .referrer(HOST_ROOT)
                        .cookies(formSubmitResponse.cookies())
                        .followRedirects(true)
                        .get();

            }

            //Find images:
            Elements imgs = returnPage.select("table.images_table img");

            for(Element img : imgs) {
                Image image = new Image();
                image.setThumbURL(img.attr("src"));
                images.add(image);
            }


            return images;

        } catch (IOException e) {
            throw new GIMSearchParseException("IO Exception", e);
        }

    }

    /**
     * Created by bahadir on 26/01/15.
     */
    public static class Image {
        private String host;
        private String thumbURL;
        private String fullURL;
        private String title;
        private int fullWidth;
        private int fullHeight;
        private String type;
        private String fileSize;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getThumbURL() {
            return thumbURL;
        }

        public void setThumbURL(String thumbURL) {
            this.thumbURL = thumbURL;
        }

        public String getFullURL() {
            return fullURL;
        }

        public void setFullURL(String fullURL) {
            this.fullURL = fullURL;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getFullWidth() {
            return fullWidth;
        }

        public void setFullWidth(int fullWidth) {
            this.fullWidth = fullWidth;
        }

        public int getFullHeight() {
            return fullHeight;
        }

        public void setFullHeight(int fullHeight) {
            this.fullHeight = fullHeight;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }
    }

    /**
     * Created by bahadir on 26/01/15.
     */
    public static class GIMSearchParseException  extends Exception{
        public GIMSearchParseException(String message) {
            super(message);
        }

        public GIMSearchParseException(String message, Throwable cause) {
            super(message, cause);
        }

        public GIMSearchParseException(Throwable cause) {
            super(cause);
        }


    }
}
