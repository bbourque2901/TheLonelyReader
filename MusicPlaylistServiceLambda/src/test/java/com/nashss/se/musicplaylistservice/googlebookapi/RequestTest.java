package com.nashss.se.musicplaylistservice.googlebookapi;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.books.v1.model.Volume;
import com.nashss.se.musicplaylistservice.googlebookapi.helper.VolumeInfoHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class RequestTest {
    JsonFactory jsonFactory;
    Request googleBookApi;
    VolumeInfoHelper helper;

    @BeforeEach
    void setup() {
        this.jsonFactory = GsonFactory.getDefaultInstance();
        this.googleBookApi = new Request();
        this.helper = new VolumeInfoHelper();
    }

//    @Test
//    public void testGoogleBookApi() throws GeneralSecurityException, IOException {
//        String searchTerm = "game of thrones";
//
//        List<Volume> volumes = googleBookApi.queryBooks(jsonFactory, searchTerm);
//        for (int i = 0; i < volumes.size(); i++) {
//            System.out.println("Result " + (i + 1) + " of " + volumes.size());
//            helper.viewVolumeFromList(volumes, i);
//            System.out.println(googleBookApi.extractAttributes(volumes, i) + '\n');
//        }
//    }
}
