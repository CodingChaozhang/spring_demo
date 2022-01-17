package com.lcz.elasticsearch_blog.respository.es;

import com.lcz.elasticsearch_blog.entity.es.EsBlog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsBlogRepository extends ElasticsearchRepository<EsBlog,Integer> {

}
