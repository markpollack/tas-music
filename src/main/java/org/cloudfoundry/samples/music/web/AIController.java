package org.cloudfoundry.samples.music.web;

import org.cloudfoundry.samples.music.config.ai.MessageRetriever;
import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.domain.Message;
import org.cloudfoundry.samples.music.domain.MessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Profile("llm")
public class AIController {
    private static final Logger logger = LoggerFactory.getLogger(AIController.class);
    private MessageRetriever messageRetriever;
    private VectorStore vectorStore;

    @Autowired
    public AIController(VectorStore vectorStore, MessageRetriever messageRetriever) {
        this.messageRetriever = messageRetriever;
        this.vectorStore = vectorStore;
    }
    @RequestMapping(value = "/ai/rag", method = RequestMethod.POST)
    public Generation generate(@RequestBody MessageRequest messageRequest) {
        Message[] messages = messageRequest.getMessages();
        logger.info("Getting Messages " + messages);

        return messageRetriever.retrieve(messages[messages.length - 1].getText());
    }

    @RequestMapping(value = "/ai/addDoc", method = RequestMethod.POST)
    public String addDoc(@RequestBody Album album) {
        String text = "artist: " + album.getArtist() + "\n" + 
            "title: " + album.getTitle() + "\n" + 
            "releaseYear: " + album.getReleaseYear() + "\n" +
            "genre: " + album.getGenre() + "\n" +
            "userReview: " + album.getUserReview() + "\n" +
            "userScore: " + album.getUserScore() + "\n";

        List<Document> documents = new ArrayList<>();
        Document doc = new Document(text);
        logger.info("Adding Album " + doc.toString());
        documents.add(doc);
        this.vectorStore.add(documents);
        return text;
    }

}