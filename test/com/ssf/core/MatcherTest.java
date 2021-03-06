package com.ssf.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Test;

import com.ssf.WorldFilter;
import com.ssf.utils.IOUtils;

/**
 * @class MatcherTest
 * @author lzxz1234<lzxz1234@gmail.com>
 * @version v1.0
 */
public class MatcherTest {

    WorldFilter Matcher = new WorldFilter();
    
    @Test
    public void test() {
        
        Matcher.addKeyWord("大江");
        Matcher.addKeyWord("大学");
        Matcher.addKeyWord("成");
        
        Assert.assertFalse(Matcher.isIllegal("习近平走访哥国农户摘花给彭丽媛闻香"));
        
        Assert.assertTrue(Matcher.isIllegal("大江习近平走访哥国农户摘花给彭丽媛闻香"));
        Assert.assertTrue(Matcher.isIllegal("习近平走访哥国农户摘花给彭丽媛闻香大江"));
        Assert.assertTrue(Matcher.isIllegal("习近平走访哥国农户大江摘花给彭丽媛闻香"));
        Assert.assertTrue(Matcher.isIllegal("大学习近平走访哥国农户摘花给彭丽媛闻香"));
        Assert.assertTrue(Matcher.isIllegal("习近平走访哥国农户摘花给彭丽媛闻香大学"));
        Assert.assertTrue(Matcher.isIllegal("习近平走访哥国农户大学摘花给彭丽媛闻香"));
        Assert.assertTrue(Matcher.isIllegal("成习近平走访哥国农户摘花给彭丽媛闻香"));
        Assert.assertTrue(Matcher.isIllegal("习近平走访哥国农户摘花给彭丽媛闻香成"));
        Assert.assertTrue(Matcher.isIllegal("习近平走访哥国农户成摘花给彭丽媛闻香"));
        
        Assert.assertTrue(Matcher.matches("大江习近平走访哥国农户摘花给彭丽媛闻香").maxHit().equals("大江"));
        Assert.assertTrue(Matcher.matches("习近平走访哥国农户摘花给彭丽媛闻香大江").maxHit().equals("大江"));
        Assert.assertTrue(Matcher.matches("习近平走访哥国农户大江摘花给彭丽媛闻香").maxHit().equals("大江"));
        Assert.assertTrue(Matcher.matches("大学习近平走访哥国农户摘花给彭丽媛闻香").maxHit().equals("大学"));
        Assert.assertTrue(Matcher.matches("习近平走访哥国农户摘花给彭丽媛闻香大学").maxHit().equals("大学"));
        Assert.assertTrue(Matcher.matches("习近平走访哥国农户大学摘花给彭丽媛闻香").maxHit().equals("大学"));
        Assert.assertTrue(Matcher.matches("成习近平走访哥国农户摘花给彭丽媛闻香").maxHit().equals("成"));
        Assert.assertTrue(Matcher.matches("习近平走访哥国农户摘花给彭丽媛闻香成").maxHit().equals("成"));
        Assert.assertTrue(Matcher.matches("习近平走访哥国农户成摘花给彭丽媛闻香").maxHit().equals("成"));
        
        Assert.assertFalse(Matcher.isIllegal("大连习近平走访哥国农户摘花给彭丽媛闻香"));
        Assert.assertFalse(Matcher.isIllegal("习近平走访哥国农户摘花给彭丽媛闻香大连"));
        Assert.assertFalse(Matcher.isIllegal("习近平走访哥国农户大连摘花给彭丽媛闻香"));
        
        Matcher.clear();
    }
    
    @Test
    public void test2() {
        
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("keywords");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String tmp = null;
            while((tmp = br.readLine()) != null) {
                Matcher.addKeyWord(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
        
        String s = "大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽，" +
                "千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东" +
                "去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流" +
                "人物,大江东去浪淘尽，千古风流人物" + new Object().toString();
        long start = System.nanoTime();
        for(int i = 0; i < 1000000; i ++) {
            Matcher.isIllegal(s);
        }
        System.out.println((System.nanoTime() - start) / 1000000.0);
        Assert.assertFalse(Matcher.isIllegal("大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽"));
        Assert.assertFalse(Matcher.isIllegal("王涵"));
        Assert.assertFalse(Matcher.isIllegal("王涵大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽"));
        Assert.assertFalse(Matcher.isIllegal("大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽王涵"));
        Assert.assertFalse(Matcher.isIllegal("王涵大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽王涵"));
        Assert.assertTrue(Matcher.isIllegal("'"));
        Assert.assertTrue(Matcher.isIllegal("大江东去浪淘尽，千古风'流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽"));
        Assert.assertTrue(Matcher.isIllegal("'大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽"));
        Assert.assertTrue(Matcher.isIllegal("大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽'"));
        Assert.assertTrue(Matcher.isIllegal("'大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽'"));
        Assert.assertTrue(Matcher.isIllegal("王涵万"));
        Assert.assertTrue(Matcher.isIllegal("王涵万大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽"));
        Assert.assertTrue(Matcher.isIllegal("大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽王涵万"));
        Assert.assertTrue(Matcher.isIllegal("王涵万大江东去浪淘尽，千古风流人物,大江东去浪淘尽，千古风流人物,大江东去浪淘尽王涵万"));
    }

}
