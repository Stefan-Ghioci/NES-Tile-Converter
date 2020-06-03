package io.github.stefan_ghioci.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

public class StringUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class.getSimpleName());

    public static String loadText(String code)
    {

        URL resource = StringUtils.class.getResource("text/" + code + ".txt");

        try (Scanner scanner = new Scanner(resource.openStream(),
                                           StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
        catch (IOException e)
        {
            LOGGER.info("Could not load text with code \"{}\". Cause: {}", code, e.getMessage());
            return "Error loading text.";
        }
    }
}
