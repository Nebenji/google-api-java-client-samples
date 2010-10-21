/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.sample.youtube;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.sample.youtube.model.Debug;
import com.google.api.client.sample.youtube.model.Video;
import com.google.api.client.sample.youtube.model.VideoFeed;
import com.google.api.client.sample.youtube.model.YouTubeUrl;

import java.io.IOException;

/**
 * @author Yaniv Inbar
 */
public class YouTubeSample {

  private static final int MAX_VIDEOS_TO_SHOW = 5;

  public static void main(String[] args) {
    Debug.enableLogging();
    try {
      try {
        HttpTransport transport = setUpTransport();
        showVideos(transport);
      } catch (HttpResponseException e) {
        System.err.println(e.response.parseAsString());
        throw e;
      }
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

  private static HttpTransport setUpTransport() {
    HttpTransport transport = GoogleTransport.create();
    GoogleHeaders headers = (GoogleHeaders) transport.defaultHeaders;
    headers.setApplicationName("Google-YouTubeSample/1.0");
    headers.gdataVersion = "2";
    transport.addParser(new JsonCParser());
    return transport;
  }

  private static VideoFeed showVideos(HttpTransport transport)
      throws IOException {
    // build URL for the video feed for "search stories"
    YouTubeUrl url = YouTubeUrl.relativeToRoot("videos");
    url.maxResults = MAX_VIDEOS_TO_SHOW;
    url.author = "searchstories";
    // execute GData request for the feed
    VideoFeed feed = VideoFeed.executeGet(transport, url);
    System.out.println("Total number of videos: " + feed.totalItems);
    for (Video video : feed.items) {
      showVideo(video);
    }
    return feed;
  }

  private static void showVideo(Video video) {
    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println("Video title: " + video.title);
    System.out.println("Description: " + video.description);
    System.out.println("Updated: " + video.updated);
    System.out.println("Tags: " + video.tags);
    System.out.println("Play URL: " + video.player.defaultUrl);
  }
}
