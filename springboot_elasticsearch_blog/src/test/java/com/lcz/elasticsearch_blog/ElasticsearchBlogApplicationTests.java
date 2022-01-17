package com.lcz.elasticsearch_blog;

import com.lcz.elasticsearch_blog.entity.es.EsBlog;
import com.lcz.elasticsearch_blog.respository.es.EsBlogRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchBlogApplicationTests {
    @Autowired
    EsBlogRepository blogRepository;

    @Test
    public void testES() {
        Iterable<EsBlog> all = blogRepository.findAll();
        Iterator<EsBlog> iterator = all.iterator();
        if (iterator.hasNext()) {
            EsBlog next = iterator.next();
            System.out.println("------" + next.getTitle());
        }
    }
}
