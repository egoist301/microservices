package com.epam.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.epam.model.SongMetadata;
import com.epam.service.ResourceProcessorService;
import com.epam.service.exception.ResourceBinaryParseException;

@Service
public class ResourceProcessorServiceImpl implements ResourceProcessorService {
  private static final String TITLE_PROPERTY_NAME = "dc:title";
  private static final String CREATOR_PROPERTY_NAME = "dc:creator";
  private static final String ALBUM_PROPERTY_NAME = "xmpDM:album";
  private static final String DURATION_PROPERTY_NAME = "xmpDM:duration";
  private static final String RELEASE_DATE_PROPERTY_NAME = "xmpDM:releaseDate";

  @Override
  public SongMetadata retrieveResourceMetadata(byte[] fileContent, Long resourceId) {
    try (InputStream input = new ByteArrayInputStream(fileContent)) {
      ContentHandler contentHandler = new DefaultHandler();
      Metadata metadata = new Metadata();
      Parser parser = new Mp3Parser();
      ParseContext parseContext = new ParseContext();
      parser.parse(input, contentHandler, metadata, parseContext);
      return SongMetadata.builder()
          .album(metadata.get(ALBUM_PROPERTY_NAME))
          .artist(metadata.get(CREATOR_PROPERTY_NAME))
          .length(metadata.get(DURATION_PROPERTY_NAME))
          .name(metadata.get(TITLE_PROPERTY_NAME))
          .resourceId(resourceId)
          .year(Integer.parseInt(metadata.get(RELEASE_DATE_PROPERTY_NAME)))
          .build();
    } catch (IOException | SAXException | TikaException e) {
      throw new ResourceBinaryParseException();
    }
  }
}
