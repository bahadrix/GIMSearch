package me.bahadir.gimsearch;

import java.util.List;

/**
 * Created by bahadir on 26/01/15.
 */
public class Main {

    //User-Agent	Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/600.2.5 (KHTML, like Gecko) Version/8.0.2 Safari/600.2.5

    public static void main(String[] args) {


        GIMSearch gimSearch = new GIMSearch();

        try {
            List<GIMSearch.Image> images = gimSearch.getSinglePage("jammer");
        } catch (GIMSearch.GIMSearchParseException e) {
            e.printStackTrace();
        }

        System.out.println();

    }
}
