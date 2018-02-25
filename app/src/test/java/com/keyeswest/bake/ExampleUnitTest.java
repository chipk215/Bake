package com.keyeswest.bake;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void loadResourceFileTest() throws IOException{
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("recipe.json");
        assertTrue(resource.getPath().endsWith("recipe.json"));
      // File file = new File(resource.getPath());

        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String recipeJson = new String(encoded, StandardCharsets.UTF_8 );

        assertTrue(recipeJson!= null);
    }
}

